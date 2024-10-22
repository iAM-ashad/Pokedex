package com.iamashad.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.iamashad.pokedex.screens.details.DetailScreen
import com.iamashad.pokedex.screens.home.HomeScreen
import com.iamashad.pokedex.screens.home.PokemonListViewModel

@Composable
fun PokedexNavigation (
    navController: NavHostController
) {
    NavHost (
        navController = navController,
        startDestination = PokedexScreens.HOMESCREEN.name
    ) {
       composable(PokedexScreens.HOMESCREEN.name) {
           val viewModel = hiltViewModel<PokemonListViewModel>()
           HomeScreen (navController, viewModel)
       }
        val detailsRoute = PokedexScreens.DETAILSCREEN.name
       composable(
           route = "$detailsRoute/{pokeName}/{dominantColor}",
           arguments = listOf(
               navArgument("pokeName") {
                   type = NavType.StringType
               },
               navArgument("dominantColor") {
                   type = NavType.IntType
               }
           )
       ) {navBackStackEntry ->
           val dominantColor = remember {
               val color = navBackStackEntry.arguments?.getInt("dominantColor")
               color?.let { Color(it) } ?: Color.White
           }
           val pokeName = remember {
               navBackStackEntry.arguments?.getString("pokeName")
           }
           DetailScreen(navController, dominantColor, pokeName)
       }
    }
}