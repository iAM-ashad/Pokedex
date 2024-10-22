package com.iamashad.pokedex.screens.details

import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.iamashad.pokedex.R
import com.iamashad.pokedex.model.Pokemon
import com.iamashad.pokedex.model.Type
import com.iamashad.pokedex.utils.Resource
import com.iamashad.pokedex.utils.parseTypeToColor
import java.util.Locale
import kotlin.math.round

@Composable
fun DetailScreen (
    navController: NavController,
    dominantColor: Color,
    pokemonName: String,
    topPadding: Dp = 60.dp,
    pokemonImageSize: Dp = 275.dp,
    viewModel: DetailsViewModel
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
    ) {
        PokemonDetailTopSection(
            navController,
            modifier = Modifier
                .fillMaxHeight(.2f)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
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
                    val img = pokemonInfo.data.sprites.front_default
                    when (img) {
                        null -> {
                            CircularProgressIndicator()
                        }
                        else -> {
                           LoadImageWithGlide (
                               imageUrl = img,
                               modifier = Modifier
                                   .size(pokemonImageSize)
                                   .offset(y = topPadding)
                           )
                        }
                    }
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
                .offset(16.dp, 16.dp),
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
            PokemonDetailSection(
                pokemonInfo = pokemonInfo.data!!,
                modifier = Modifier
                    .offset(y = (-20).dp)
            )
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

@Composable
fun LoadImageWithGlide(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        modifier = modifier,
        update = { imageView ->
            // Use Glide to load the image
            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)
        }
    )
}

@Composable
fun PokemonDetailSection (
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = modifier
                .fillMaxWidth()
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight,
            pokemonHeight = pokemonInfo.height
        )
    }
}

@Composable
fun PokemonTypeSection(
    types: List<Type>
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (type in types) {
            Box (
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text (
                    text = type.type.name.capitalize(Locale.ROOT),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight/10f)
    }
    val pokemonHeightInM = remember {
        round(pokemonHeight/10f)
    }
    Row (
        modifier = Modifier
            .fillMaxWidth()
    ){
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(R.drawable.ic_weight),
            modifier = Modifier
                .weight(1f)
        )

        VerticalDivider(
            color = MaterialTheme.colorScheme.inverseOnSurface
        )

        PokemonDetailDataItem(
            dataValue = pokemonHeightInM,
            dataUnit = "m",
            dataIcon = painterResource(R.drawable.ic_height),
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ){
        Icon (
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}