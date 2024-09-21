package com.example.globalfugitive

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainMenu(
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize()
    )
    {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Image(
                painter = painterResource(id = R.drawable.global_fugitive_text),
                contentDescription = "Global Fugitive text",
                modifier = Modifier
                    .height(100.dp)
                    .offset(y = 25.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { navController.navigate("GamePlayScreen") },
                modifier = Modifier.width(200.dp),
            ) {
                Text("Single Player")
            }
            Button(
                onClick = { navController.navigate("") },
                modifier = Modifier.width(200.dp),
            ) {
                Text("2-Player")
            }
            Button(
                onClick = { navController.navigate("") },
                modifier = Modifier.width(200.dp),
            ) {
                Text("Quit")
            }
        }
        Image(
            painter = painterResource(id = R.drawable.heatmap_footer),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = 1.1f,
                    scaleY = 1.1f
                )
                .align(Alignment.BottomCenter)
        )
    }
}