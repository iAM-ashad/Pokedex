@file:Suppress("DEPRECATION")

package com.iamashad.pokedex.screens.details

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import androidx.navigation.NavController
import com.iamashad.pokedex.R
import com.iamashad.pokedex.model.Pokemon
import com.iamashad.pokedex.model.Type
import com.iamashad.pokedex.utils.LoadImageWithGlide
import com.iamashad.pokedex.utils.Resource
import com.iamashad.pokedex.utils.parseStatToAbbr
import com.iamashad.pokedex.utils.parseStatToColor
import com.iamashad.pokedex.utils.parseTypeToColor
import java.util.Locale
import kotlin.math.round

@Composable
fun DetailScreen (
    navController: NavController,
    dominantColor: Color,
    pokemonName: String,
    topPadding: Dp = 20.dp,
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
                    top = (topPadding + pokemonImageSize) / 2f,
                    start = 16.dp,
                    bottom = 16.dp,
                    end = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface),
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
                .fillMaxSize()
                .size(pokemonImageSize),
            contentAlignment = Alignment.TopCenter
        ) {
            if(pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    val img = pokemonInfo.data.sprites.front_default
                           LoadImageWithGlide (
                               imageUrl = img,
                               modifier = Modifier
                                   .size(pokemonImageSize)
                                   .offset(y = topPadding)
                                   .padding(bottom=20.dp)
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
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    errorModifier: Modifier = Modifier
) {
    when (pokemonInfo) {
        is Resource.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
            ) {
                PokemonDetailSection(
                    pokemonInfo = pokemonInfo.data!!,
                    modifier = modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp, max = 600.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(10.dp)
                        )
                )
            }
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = errorModifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = loadingModifier,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PokemonDetailSection (
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(top = 80.dp)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.capitalize(Locale.ROOT)}",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight,
            pokemonHeight = pokemonInfo.height
        )
        PokemonBaseStat(pokemonInfo)
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
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.error
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

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 35.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            if (statMaxValue > 0) statValue / statMaxValue.toFloat() else 0f
        } else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        ), label = ""
    )

    LaunchedEffect(true) {
        animationPlayed = true
    }

    Box (
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if(isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {Color.LightGray}
            )
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(curPercent.value)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(statColor)
                .padding(8.dp),
        ){
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PokemonBaseStat(
    pokemonInfo: Pokemon,
    animDelayPerItem: Int = 100
) {
    val maxBaseStat = remember {
        pokemonInfo.stats.maxOf { it.base_stat }
    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text (
            text = "Base Stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(start = 10.dp, top = 10.dp, bottom = 7.dp)
        )

        for (i in pokemonInfo.stats.indices) {
            val stat = pokemonInfo.stats[i]
            PokemonStat(
                statName = parseStatToAbbr(stat),
                statMaxValue = maxBaseStat,
                statValue = stat.base_stat,
                statColor = parseStatToColor(stat),
                animDelay = i * animDelayPerItem,
            )
        }
    }
}