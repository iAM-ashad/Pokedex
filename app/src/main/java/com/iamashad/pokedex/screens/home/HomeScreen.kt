package com.iamashad.pokedex.screens.home

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.Toast
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
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
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
            horizontalAlignment = CenterHorizontally
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
            SearchBar (
                modifier = Modifier
                    .padding(bottom = 25.dp)
            ) {
                viewModel.searchPokemon(it)
            }
            Spacer(modifier = Modifier.height(20.dp))
            PokemonList(navController = navController)
        }
    }
    Box (
        contentAlignment = Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (viewModel.isLoading.value) {
            CircularProgressIndicator()
        }
        if (viewModel.loadingError.value.isNotEmpty()) {
            RetrySection(error = viewModel.loadingError.value) {
                viewModel.loadPokemonPaginated()
            }
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
        maxLines = 1,
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
    var imageDrawable by remember { mutableStateOf<Drawable?>(null) }

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
                    "${PokedexScreens.DETAILSCREEN.name}/${entry.pokemonName}/${dominantColor.toArgb()}"
                )
            }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current
            val imageLoader = context.imageLoader
            val request = ImageRequest.Builder(context)
                .data(entry.imgUrl)
                .crossfade(true)
                .target(
                    onStart = {
                        isLoading = true
                    },
                    onSuccess = { drawable ->
                        isLoading = false
                        imageDrawable = drawable
                        drawable.let {
                            viewModel.calcDominantColor(it) { color ->
                                dominantColor = color
                            }
                        }
                    },
                    onError = {
                        isLoading = false
                    }
                )
                .build()

            DisposableEffect(entry.imgUrl) {
                val disposable = imageLoader.enqueue(request)
                onDispose { disposable.dispose() }
            }

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
                            .align(Center)
                    )
                }
            }

            imageDrawable?.let { drawable ->
                val painter = remember { androidx.compose.ui.graphics.painter.BitmapPainter((drawable as BitmapDrawable).bitmap.asImageBitmap()) }
                Image(
                    painter = painter,
                    contentDescription = entry.pokemonName,
                    modifier = Modifier.size(120.dp)
                )
            }

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
        Row {
            PokedexListEntry(
                entry = entries[rowIndex*2],
                navController = navController,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (entries.size > rowIndex*2+1) {
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

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val isSearching by remember {
        viewModel.isSearching
    }

    LazyColumn {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }

        items(itemCount, key = {items -> items}) { items ->
            if (items >= itemCount - 1 && !endReached && !isSearching) {
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

@Composable
fun RetrySection (
    error: String,
    onRetry: () -> Unit
) {
    Column (
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
    ) {
        Toast.makeText(LocalContext.current, error, Toast.LENGTH_SHORT).show()
        Spacer(modifier = Modifier.height(8.dp))
        Button (
            modifier = Modifier
                .align(CenterHorizontally),
            onClick = onRetry
        ) {
            Text (
                text = "Retry"
            )
        }
    }
}