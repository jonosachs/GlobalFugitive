package com.example.globalfugitive

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.tasks.await

// Function to fetch country predictions using Places API
suspend fun getCountryPredictions(
    placesClient: PlacesClient,
    query: String
): List<AutocompletePrediction> {
    val token = com.google.android.libraries.places.api.model.AutocompleteSessionToken.newInstance()
    val request = FindAutocompletePredictionsRequest.builder()
        .setTypesFilter(listOf(PlaceTypes.COUNTRY))
        .setSessionToken(token)
        .setQuery(query)
        .build()
    return try {
        val response = placesClient.findAutocompletePredictions(request).await()
        response.autocompletePredictions
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

// Helper function to get LatLng from AutocompletePrediction
suspend fun getLatLngFromPrediction(
    context: Context,
    prediction: AutocompletePrediction
): LatLng? {
    return try {
        val placeFields = listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG)
        val request = com.google.android.libraries.places.api.net.FetchPlaceRequest.newInstance(prediction.placeId, placeFields)
        val response = com.google.android.libraries.places.api.Places.createClient(context).fetchPlace(request).await()
        response.place.latLng
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}