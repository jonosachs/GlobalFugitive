package com.example.globalfugitive

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.globalfugitive.ui.theme.AppTheme
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase



class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialise places API
        initializePlacesAPI()

        // Create ViewModels and Repositories in the Activity scope
        val userViewModel = UserViewModel()
        val gameViewModel = GameViewModel(application)

        enableEdgeToEdge()

        setContent {
            AppTheme(dynamicColor = false){
                val navController = rememberNavController()

                // Check if the user is signed in
                val currentUser = auth.currentUser

                // Set start destination based on user authentication
                var startDestination = "Landing"
                if (currentUser != null) {
                    println("User @ MainActivity: $currentUser")
                    startDestination = "DrawerMenu" // Navigate directly to DrawerMenu if user is signed in
                }

                AppNavigation(
                    gameViewModel = gameViewModel,
                    userViewModel = userViewModel,
                    startDestination = startDestination,
                    navController = navController
                )
            }
        }
    }

    private fun initializePlacesAPI() {
        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")

        // Initialize Places SDK with the API key from meta-data
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }
    }


}

