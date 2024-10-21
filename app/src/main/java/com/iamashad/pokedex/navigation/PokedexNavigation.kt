package com.iamashad.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.iamashad.pokedex.screens.details.DetailScreen
import com.iamashad.pokedex.screens.home.HomeScreen

@Composable
fun PokedexNavigation (
    navController: NavHostController
) {
    NavHost (
        navController = navController,
        startDestination = PokedexScreens.HOMESCREEN.name
    ) {
       composable(PokedexScreens.HOMESCREEN.name) {
           HomeScreen (navController)
       }
        val detailsRoute = PokedexScreens.DETAILSCREEN.name
       composable(
           route = "$detailsRoute/{pokeCard}",
           arguments = listOf(
               navArgument("pokeCard") {
                   type = NavType.StringType
               }
           )
       ) {navBackStackEntry ->
           navBackStackEntry.arguments?.getString("pokeCard").let { pokeCard->
               DetailScreen(navController = navController, pokeCard)
           }
       }
    }
}