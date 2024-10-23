package com.iamashad.pokedex.utils

import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.iamashad.pokedex.model.Stat
import com.iamashad.pokedex.model.Type
import com.iamashad.pokedex.ui.theme.AtkColor
import com.iamashad.pokedex.ui.theme.DefColor
import com.iamashad.pokedex.ui.theme.HPColor
import com.iamashad.pokedex.ui.theme.SpAtkColor
import com.iamashad.pokedex.ui.theme.SpDefColor
import com.iamashad.pokedex.ui.theme.SpdColor
import com.iamashad.pokedex.ui.theme.TypeBug
import com.iamashad.pokedex.ui.theme.TypeDark
import com.iamashad.pokedex.ui.theme.TypeDragon
import com.iamashad.pokedex.ui.theme.TypeElectric
import com.iamashad.pokedex.ui.theme.TypeFairy
import com.iamashad.pokedex.ui.theme.TypeFighting
import com.iamashad.pokedex.ui.theme.TypeFire
import com.iamashad.pokedex.ui.theme.TypeFlying
import com.iamashad.pokedex.ui.theme.TypeGhost
import com.iamashad.pokedex.ui.theme.TypeGrass
import com.iamashad.pokedex.ui.theme.TypeGround
import com.iamashad.pokedex.ui.theme.TypeIce
import com.iamashad.pokedex.ui.theme.TypeNormal
import com.iamashad.pokedex.ui.theme.TypePoison
import com.iamashad.pokedex.ui.theme.TypePsychic
import com.iamashad.pokedex.ui.theme.TypeRock
import com.iamashad.pokedex.ui.theme.TypeSteel
import com.iamashad.pokedex.ui.theme.TypeWater
import java.util.Locale

fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
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

fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.toLowerCase(Locale.ROOT)) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.toLowerCase(Locale.ROOT)) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.Black
    }
}