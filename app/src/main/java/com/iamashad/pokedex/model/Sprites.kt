package com.iamashad.pokedex.model

data class Sprites(
    val backDefault: String,
    val backFemale: Any,
    val backShiny: String,
    val backShinyFemale: Any,
    val front_default: String,
    val frontFemale: Any,
    val frontShiny: String,
    val frontShinyFemale: Any,
    val other: Other,
    val versions: Versions
)