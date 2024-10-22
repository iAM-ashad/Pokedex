package com.iamashad.pokedex.screens.home

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.imageLoader
import coil.request.ImageRequest
import com.iamashad.pokedex.R
import com.iamashad.pokedex.model.PokedexListEntry
import com.iamashad.pokedex.navigation.PokedexScreens
import com.iamashad.pokedex.ui.theme.RobotoCondensed

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: PokemonListViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Image (
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon Logo",
                alignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp, top = 25.dp)
            )
            val keyboardController = LocalSoftwareKeyboardController.current
            SearchBar (
                modifier = Modifier
                    .padding(bottom = 25.dp)
            ) {
                //Log.d("Search Made", "Searched for: $it")
            }
            Spacer(modifier = Modifier.height(20.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    OutlinedTextField (
        value = searchQuery,
        onValueChange = {
            searchQuery = it
            onSearch(it)
        },
        shape = RoundedCornerShape(30.dp),
        label = {
            Text(text = "Pika Pi?")
        },
        placeholder = {
            Text(text = "Pikachu")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}

@Composable
fun PokedexListEntry(
    modifier: Modifier = Modifier,
    entry: PokedexListEntry,
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colorScheme.surfaceVariant
    var dominantColor by remember { mutableStateOf(defaultDominantColor) }
    var isLoading by remember { mutableStateOf(true) }
    var imageDrawable by remember { mutableStateOf<Drawable?>(null) } // Hold the loaded image

    Box(
        modifier = modifier
            .padding(10.dp)
            .shadow(10.dp, RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(dominantColor, defaultDominantColor)
                )
            )
            .clickable {
                navController.navigate(
                    PokedexScreens.DETAILSCREEN.name + "/{${dominantColor.toArgb()}}/{${entry.pokemonName}}"
                )
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            // Use the Coil ImageLoader to fetch the image with a target
            val context = LocalContext.current
            val imageLoader = context.imageLoader
            val request = ImageRequest.Builder(context)
                .data(entry.imgUrl)
                .crossfade(true)
                .target(
                    onStart = {
                        isLoading = true // Image loading started
                    },
                    onSuccess = { drawable ->
                        isLoading = false // Image loading succeeded
                        imageDrawable = drawable // Update image drawable state
                        drawable?.let {
                            // Extract the dominant color from the drawable
                            viewModel.calcDominantColor(it) { color ->
                                dominantColor = color
                            }
                        }
                    },
                    onError = {
                        isLoading = false // Image loading failed
                    }
                )
                .build()

            // Trigger the image request
            DisposableEffect(entry.imgUrl) {
                val disposable = imageLoader.enqueue(request)
                onDispose { disposable.dispose() }
            }

            // Show CircularProgressIndicator while loading
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .size(120.dp)
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .scale(0.5f)
                            .align(Alignment.Center)
                    )
                }
            }

            // Display the loaded image using the Drawable
            imageDrawable?.let { drawable ->
                val painter = remember { androidx.compose.ui.graphics.painter.BitmapPainter((drawable as BitmapDrawable).bitmap.asImageBitmap()) }
                Image(
                    painter = painter,
                    contentDescription = entry.pokemonName,
                    modifier = Modifier.size(120.dp)
                )
            }

            // Display Pokemon Name
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp
            )
        }
    }
}





@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController
) {
    Column {
        Row {
            PokedexListEntry(
                entry = entries[rowIndex*2],
                navController = navController,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (entries.size >= rowIndex*2+2) {
                PokedexListEntry(
                    entry = entries[rowIndex*2+1],
                    navController = navController,
                    modifier = Modifier
                        .weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadingError }
    val endReached by remember { viewModel.endReached }

    LazyColumn {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }

        items(itemCount) { items ->
            if (items >= itemCount - 1 && !endReached) {
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(
                rowIndex = items,
                entries = pokemonList,
                navController = navController
            )
        }
    }
}