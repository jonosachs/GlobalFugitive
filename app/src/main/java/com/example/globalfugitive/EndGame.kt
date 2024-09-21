package com.example.globalfugitive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun EndGame(navController: NavController, viewModel: GameViewModel) {
    val mysteryCountry = viewModel.mysteryCountry.value
    val gameWon = viewModel.gameWon.value

    println("gameWon value @ EndGame: $gameWon")

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        if (gameWon == true) {
            Text(
                text = "You won! The country was $mysteryCountry",
            )
        } else {
            Text(
                text = "You lost.. The country was $mysteryCountry",
            )
        }
    }
}