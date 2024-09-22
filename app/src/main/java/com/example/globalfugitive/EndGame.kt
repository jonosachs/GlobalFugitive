package com.example.globalfugitive

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.unit.dp
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
            Image(
                painter = painterResource(id = R.drawable.fugitive_found),
                contentDescription = "Fugitive found image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You won! The country was $mysteryCountry",
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.fugitive_escaped),
                contentDescription = "Fugitive escaped image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You lost.. The country was $mysteryCountry",
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { navController.navigate("MainMenu") },
            modifier = Modifier.width(200.dp),
        ) {
            Text("Play again")

        }
        Button(
            onClick = { /*TODO quit action*/ },
            modifier = Modifier.width(200.dp),
        ) {
            Text("Quit")
        }
    }
}