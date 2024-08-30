package com.mohab.detectiveai.routes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mohab.detectiveai.routes.Route.CHARACTER_SELECTOR
import com.mohab.detectiveai.routes.Route.CHAT
import com.mohab.detectiveai.routes.Route.END_GAME
import com.mohab.detectiveai.routes.Route.HOME
import com.mohab.detectiveai.routes.Route.LOAD_GAME
import com.mohab.detectiveai.routes.Route.NEW_GAME
import com.mohab.detectiveai.screens.CharacterSelectorScreen
import com.mohab.detectiveai.screens.ChatScreen
import com.mohab.detectiveai.screens.EndGameScreen
import com.mohab.detectiveai.screens.HomeScreen
import com.mohab.detectiveai.screens.LoadGameScreen
import com.mohab.detectiveai.screens.NewGameScreen

object Route {

    const val HOME = "home"
    const val NEW_GAME = "new_game"
    const val LOAD_GAME = "load_game"
    const val CHARACTER_SELECTOR = "character_selector"
    const val CHAT = "chat"
    const val END_GAME = "end_game"

}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = HOME) {

        composable(HOME) { HomeScreen(navController) }
        composable(NEW_GAME) { NewGameScreen(navController) }
        composable(LOAD_GAME) { LoadGameScreen(navController) }
        composable(CHARACTER_SELECTOR) { CharacterSelectorScreen(navController) }
        composable(CHAT) { ChatScreen(navController) }
        composable(
            route = "$END_GAME/{isCorrect}",
            arguments = listOf(
                navArgument("isCorrect") { type = NavType.BoolType }
            )
        ) {
            val isCorrect = it.arguments?.getBoolean("isCorrect") !!
            EndGameScreen(navController, isCorrect = isCorrect)
        }


    }
}