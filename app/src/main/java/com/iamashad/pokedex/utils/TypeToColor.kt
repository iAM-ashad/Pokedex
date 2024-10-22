package com.iamashad.pokedex.utils

import androidx.compose.ui.graphics.Color
import com.iamashad.pokedex.model.Type
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