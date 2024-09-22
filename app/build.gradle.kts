import com.android.build.api.variant.BuildConfigField
import java.io.FileInputStream
import java.util.Properties

// Load secrets.properties file
//val secretsPropertiesFile = rootProject.file("secrets.properties")
//val secretsProperties = Properties()
//
//if (secretsPropertiesFile.exists()) {
//    secretsProperties.load(FileInputStream(secretsPropertiesFile))
//}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
    compileSdk = 34

    // Enable Build Features
    android.buildFeatures.buildConfig = true

    defaultConfig {
        applicationId = "com.example.globalfugitive"
        minSdk = 24
        targetSdk = 34
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
//    implementation(libs.androidx.ui.desktop)
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

    implementation("com.google.android.libraries.places:places:3.1.0")

    // Kotlin Coroutines Core Library
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    // Kotlin Coroutines for Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    // Kotlin Coroutines Play Services (for Google APIs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")

    implementation("com.google.maps.android:maps-ktx:3.3.0")

    //Gson
    implementation("com.google.code.gson:gson:2.8.8")

}