package com.example.globalfugitive

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


//class PlacesViewModel(application: Application) : AndroidViewModel(application) {
//    private val placesClient: PlacesClient = Places.createClient(application)
//    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
//    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions
//
//    fun getCountryPredictions(query: String) {
//        val token = AutocompleteSessionToken.newInstance()
//        val request = FindAutocompletePredictionsRequest.builder()
////            .setCountries("US") // Filter by specific country if needed
////            .setTypeFilter(com.google.android.libraries.places.api.model.TypeFilter.COUNTRY)
//            .setSessionToken(token)
//            .setQuery(query)
//            .build()
//
//        viewModelScope.launch {
//            val response = placesClient.findAutocompletePredictions(request).await()
//            _predictions.value = response.autocompletePredictions
//        }
//    }
//}
