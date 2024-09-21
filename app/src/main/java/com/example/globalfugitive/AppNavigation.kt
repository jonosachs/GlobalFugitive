package com.example.globalfugitive

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(viewModel: GameViewModel) {
    val navController = rememberNavController()
    val rememberedViewModel = remember { viewModel }

    NavHost(
        navController = navController,
        startDestination = "Landing"
    ) {
        composable("Landing") { Landing(navController) }
        composable("SignIn") { SignIn(navController) }
        composable("MainMenu") { MainMenu(navController) }
        composable("GamePlayScreen") { GamePlayScreen(navController, rememberedViewModel) }
        composable("EndGame") { EndGame(navController, rememberedViewModel) }
    }


}