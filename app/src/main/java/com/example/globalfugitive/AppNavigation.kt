package com.example.globalfugitive

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    gameViewModel: GameViewModel,
    userViewModel: UserViewModel,
    startDestination: String,
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("Landing") { Landing(navController) }
        composable("SignInScreen") { SignInScreen(userViewModel, navController) }
        composable("DrawerMenu") {
            // Create a nested NavController for DrawerMenu's internal navigation
            val nestedNavController = rememberNavController()
            DrawerNavigation(
                parentNavController = navController, // Pass parent NavController
                nestedNavController = nestedNavController, // Nested NavController for the drawer menu
                userViewModel = userViewModel
            )
        }
        composable("GamePlayScreen") { GamePlayScreen(navController, gameViewModel, userViewModel) }
        composable("EndGame") { EndGame(navController, gameViewModel, userViewModel) }
    }


}


