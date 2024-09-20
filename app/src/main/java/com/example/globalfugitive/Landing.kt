package com.example.globalfugitive

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Landing (navController: NavController) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.global_fugitive_text),
            contentDescription = "Global Fugitive text",
            modifier = Modifier
                .align(AbsoluteAlignment.Left)
                .offset(25.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Image(
            painter = painterResource(id = R.drawable.logo2),
            contentDescription = "Global Fugitive image",
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(
                    scaleX = 1.1f,
                    scaleY = 1.1f
                )
        )
        Spacer(modifier = Modifier.size(30.dp))
        Button(
            onClick = { navController.navigate("MainMenu") },
            modifier = Modifier.width(200.dp),
        ) {
            Text("Start")
        }

    }
}