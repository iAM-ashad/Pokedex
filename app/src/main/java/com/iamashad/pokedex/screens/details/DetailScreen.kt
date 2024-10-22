package com.iamashad.pokedex.screens.details

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.iamashad.pokedex.R
import com.iamashad.pokedex.model.Pokemon
import com.iamashad.pokedex.utils.Resource

@Composable
fun DetailScreen (
    navController: NavController,
    dominantColor: Color,
    pokemonName: String,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: DetailsViewModel
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(16.dp)
    ) {
        PokemonDetailTopSection(
            navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.2f)
                .align(Alignment.TopCenter)
        )
        PokemonStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                )
        )

        Box (
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            if(pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.frontDefault)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Pokemon Image",
                        modifier = Modifier
                            .size(pokemonImageSize)
                            .offset(y = topPadding),
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        onSuccess = {
                            Log.d("Coil", "Image Loaded Successfully")
                        },
                        onError = {
                            Log.d("Coil", "Error loading image")
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun PokemonDetailTopSection (
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(Color.Black, Color.Transparent)
                )
            )
    ) {
        IconButton (
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp,16.dp),
            onClick = {
                navController.popBackStack()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun PokemonStateWrapper(
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    errorModifier: Modifier = Modifier,
    loading: Boolean = false,
    error: String = "",
    pokemonInfo: Resource<Pokemon>,
) {
    when (pokemonInfo) {
        is Resource.Success -> {

        }
        is Resource.Error -> {
            Text (
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = errorModifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = loadingModifier,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}