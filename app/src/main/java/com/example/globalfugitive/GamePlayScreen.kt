package com.example.globalfugitive

import GoogleMapsScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp


@Composable
fun GamePlayScreen(
    navController: NavController,
    viewModel: GameViewModel
) {

    //start a new game
    LaunchedEffect(Unit) {
        viewModel.startNewGame()
    }

    val context = LocalContext.current
    val placesClient: PlacesClient = remember { Places.createClient(context) }
    var searchQuery by remember { mutableStateOf("") }
    var predictions by remember { mutableStateOf<List<AutocompletePrediction>>(emptyList()) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // Shared selected location
    val coroutineScope = rememberCoroutineScope()
    var lazyColumnVisible by remember { mutableStateOf(true) }
    val targets by viewModel.targets
    val mysteryCountry by viewModel.mysteryCountry

    // Debugging
    println("mysteryCountry = $mysteryCountry")

    // Define a CameraPositionState to control the map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(1.3521, 103.8198), 0f) // Initial position
    }

    // Update predictions whenever the search query changes
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            predictions = getCountryPredictions(placesClient, searchQuery)
        } else {
            predictions = emptyList()
        }
    }

    //Main content box
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        //Main content column
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            //Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.global_fugitive_text),
                    contentDescription = "Global Fugitive text",
                    modifier = Modifier
                        .height(100.dp)
                        .align(Alignment.Top)
                        .offset(y = 25.dp)
                )
            }

            //Graphic Panel Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom

            ) {
                // Graphic face response
                //TODO: Face response based on distance from mystery country
                var imgId: Int? = null
                when(targets.size) {
                1 -> imgId = R.drawable.authority_face_2
                2 -> imgId = R.drawable.authority_face_3
                3 -> imgId = R.drawable.authority_face_4
                4 -> imgId = R.drawable.authority_face_5
                5 -> imgId = R.drawable.authority_face_5
                    else -> {imgId = R.drawable.authority_face_1}
                }

                Image(
                    painter = painterResource(id = imgId),
                    contentDescription = "Authority face",
                    modifier = Modifier
                        .height(150.dp)
                        .align(Alignment.Bottom)
                        .offset(x=40.dp)
                )

                //Box for second image and overlay text
                Box(
                    modifier = Modifier
                        .height(175.dp)
                        .fillMaxWidth()
                        .align(Alignment.Bottom),
                ) {
                    //TODO: Larger notepad image required
                    Image(
                        painter = painterResource(id = R.drawable.notepad),
                        contentDescription = "Notepad",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .width(400.dp)
                    )

                    //Text column
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Targets",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                        )

                        targets.forEachIndexed{ index, target ->
                            Text(
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                text = "${index + 1}. $target",
                            )
                        }
                    }
                }

            }


            //Google maps row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
                    .height(350.dp)
                    .border(width = 1.dp, color = Color.Black),

                verticalAlignment = Alignment.Top,

                ) {

                //TODO: Fit google maps screen to canvas to show entire world
                GoogleMapsScreen(
                    cameraPositionState = cameraPositionState,
                    selectedLocation = selectedLocation,
                    viewModel = viewModel,
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            //Search bar row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newValue ->
                        lazyColumnVisible = true
                        searchQuery = newValue
                    },
                    placeholder = { Text("Select Country..") },
//                    modifier = Modifier.weight(1f)
                )


                Button(
                    onClick = {
                        coroutineScope.launch {
                            // Perform the search or update predictions based on searchQuery
                            if (searchQuery.isNotBlank()) {
                                predictions = getCountryPredictions(placesClient, searchQuery)
                                println("Search submitted with text: $searchQuery")

                                // Update the selected location with the first prediction (if available)
                                if (predictions.isNotEmpty()) {
                                    val firstPrediction = predictions[0]
                                    val latLng = getLatLngFromPrediction(context, firstPrediction)
                                    if (latLng != null) {
                                        println("Updating selected location to: $latLng")
                                        selectedLocation = latLng // Update the selected location

                                        // Add guess to list of prior guesses if valid
                                        viewModel.addGuess(searchQuery, latLng)

                                        // Check if game over conditions met
                                        if(viewModel.gameEnd(searchQuery)) {
                                            println("gameWon value @ GamePlayScreen: ${viewModel.gameWon.value}")
                                            navController.navigate("EndGame")
                                        }

                                    } else {
                                        println("Failed to get LatLng from prediction.")
                                    }
                                }

                                // Reset text field to blank after guess
                                searchQuery = ""

                            }
                        }

                    },
                    modifier = Modifier
                        .width(70.dp)
                ) {
                    Text("Go")
                }
            }

            //Display predictions
            if (lazyColumnVisible) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(70.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    items(predictions.size) { index ->
                        val prediction = predictions[index]
                        Text(
                            text = prediction.getFullText(null).toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable {
                                    coroutineScope.launch {
                                        // Handle the click event, e.g., update map position
                                        val latLng = getLatLngFromPrediction(context, prediction)
                                        println("Selected LatLng: $latLng")
                                        if (latLng != null) {
                                            //Update camera position
                                            selectedLocation = latLng
                                            searchQuery = prediction
                                                .getPrimaryText(null)
                                                .toString()
                                            lazyColumnVisible = false

                                        }

                                    }
                                }
                                .padding(3.dp),

                        )
                    }
                } //close lazy column
            } //close if statement
        } //close outer column
    } //close outer box

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        //Footer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {

            Image(
                painter = painterResource(id = R.drawable.heatmap_footer),
                contentDescription = "Footer image",
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(
                        scaleX = 1.1f,
                        scaleY = 1.1f
                    ),

                )
        } //close row
    } //close box
} //close composable



