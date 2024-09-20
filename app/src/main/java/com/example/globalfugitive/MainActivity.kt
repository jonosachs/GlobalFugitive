package com.example.globalfugitive

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.volley.BuildConfig
import com.example.globalfugitive.ui.theme.AppTheme
import com.google.android.libraries.places.api.Places



class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

        // Retrieve the API key from AndroidManifest.xml using meta-data
        val apiKey = appInfo.metaData.getString("com.google.android.geo.API_KEY")

        // Initialize Places SDK with the API key from meta-data
        if (!Places.isInitialized() && apiKey != null) {
            Places.initialize(applicationContext, apiKey)
        }

        enableEdgeToEdge()

        setContent {
            AppTheme(dynamicColor = false){
                AppNavigation()
            }

        }

    }
}

