package com.iamashad.pokedex.screens.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController

@Composable
fun DetailScreen (
    navController: NavController,
    dominantColor: Color? = null,
    pokeName: String? = null
) {
    Text(text = "Details Screen")
}