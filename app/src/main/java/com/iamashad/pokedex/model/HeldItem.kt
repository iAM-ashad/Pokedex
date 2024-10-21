package com.iamashad.pokedex.model

data class HeldItem(
    val item: Item,
    val version_details: List<VersionDetail>
)