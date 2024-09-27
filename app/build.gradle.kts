import com.android.build.api.variant.BuildConfigField
//import sun.awt.FontConfiguration.verbose
import java.io.FileInputStream
import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    //Firebase
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")


//    id("com.google.dagger.hilt.android")
////    id("com.android.application")
//    id("kotlin-android")
//    id("kotlin-kapt")

}


secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"
}



android {
    namespace = "com.example.globalfugitive"
    compileSdk = 35

    // Enable Build Features
    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.globalfugitive"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

//        manifestPlaceholders["MAPS_API_KEY"] = secretsProperties["MAPS_API_KEY"] as String
//        buildConfigField("String", "MAPS_API_KEY", "\"${secretsProperties["MAPS_API_KEY"]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.services.maps)
    implementation(libs.maps)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
//    implementation(libs.androidx.media3.common.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.volley)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)
    implementation(libs.material.v190)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.play.services.maps.v1802) // Latest version
    implementation(libs.play.services.location)

    implementation(libs.maps.compose.v2110) // latest version

    implementation(libs.places.v310)

    // Kotlin Coroutines Core Library
    implementation(libs.kotlinx.coroutines.core)

    // Kotlin Coroutines for Android
    implementation(libs.kotlinx.coroutines.android)

    // Kotlin Coroutines Play Services (for Google APIs)
    implementation(libs.kotlinx.coroutines.play.services)

    implementation(libs.maps.ktx)

    //Gson
    implementation(libs.gson)

    //Credential Manager
//    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
//    implementation("androidx.credentials:credentials-play-services-auth:1.5.0-alpha05")

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)

//    implementation(libs.androidx.hilt.navigation.compose)
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)

    implementation(libs.androidx.lifecycle.livedata.ktx)

    // coil
    implementation(libs.coil.compose.v222)

}