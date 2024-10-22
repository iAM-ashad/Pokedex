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
import com.iamashad.pokedex.screens.details.DetailsViewModel
import com.iamashad.pokedex.screens.home.HomeScreen
import com.iamashad.pokedex.screens.home.PokemonListViewModel
import java.util.Locale

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
       composable(
           route = "${PokedexScreens.DETAILSCREEN.name}/{pokemonName}/{dominantColor}",
           arguments = listOf(
               navArgument("pokemonName") {
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
           val pokemonName = remember {
               navBackStackEntry.arguments?.getString("pokemonName")
           }
           val viewModel = hiltViewModel<DetailsViewModel>()
           DetailScreen (
               dominantColor = dominantColor,
               pokemonName = pokemonName?.toLowerCase(Locale.ROOT) ?: "",
               navController = navController,
               viewModel = viewModel
           )
       }
    }
}