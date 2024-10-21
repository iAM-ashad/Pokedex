package com.iamashad.pokedex.screens.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailScreen (
    navController: NavController,
    pokeCard: String? = null
) {
    Text(text = "Details Screen")
}