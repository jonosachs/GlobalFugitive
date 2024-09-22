package com.example.globalfugitive

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.InputStreamReader
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class GameViewModel(application: Application) : AndroidViewModel(application) {

    var targets = mutableStateOf<List<String>>(emptyList())
        private set
    var countries = mutableStateOf<List<String>>(emptyList())
    var mysteryCountry = mutableStateOf<String?>(null)
        private set
    var mysteryLat = mutableStateOf<Double?>(null)
        private set
    var mysteryLong = mutableStateOf<Double?>(null)
    var guessesLatLng = mutableStateOf<List<LatLng>>(emptyList())
    var gameWon = mutableStateOf<Boolean?>(null)
        private set

    fun gameEnd(guess: String): Boolean {
        return when {
            guess.lowercase() == mysteryCountry.value.toString().lowercase() -> {
                gameWon.value = true
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            targets.value.size >= 5 -> {
                gameWon.value = false
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            else -> false
        }
    }

    fun validGuess(guess: String): Boolean {
        return countries.value.contains(guess.lowercase()) &&
                !targets.value.toString().lowercase().contains(guess.lowercase()) &&
                targets.value.size < 5
    }

    fun addGuess (guess: String, guessLatLng: LatLng) {
        val lat1 = guessLatLng.latitude
        val long1 = guessLatLng.longitude
        val lat2 = mysteryLat.value!!
        val long2 = mysteryLong.value!!
        val distance = haversineDistance(lat1, long1, lat2, long2)

        if(validGuess(guess)) {
            targets.value += "$guess ${distance}km"
            guessesLatLng.value += guessLatLng
        }
    }

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double) : String {
        // Radius of the Earth in kilometers
        val earthRadius = 6371.0

        // Convert latitude and longitude from degrees to radians
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val radLat1 = Math.toRadians(lat1)
        val radLat2 = Math.toRadians(lat2)

        // Haversine formula
        val a = sin(dLat / 2).pow(2) + sin(dLon / 2).pow(2) * cos(radLat1) * cos(radLat2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Distance in kilometers
        val distance = (earthRadius * c).roundToInt()
        val formatter = NumberFormat.getNumberInstance(Locale.UK)
        return formatter.format(distance)
    }

    fun startNewGame() {
        val countryObject = getRandomCountry()
        mysteryLat.value = countryObject["latitude"].asDouble
        mysteryLong.value = countryObject["longitude"].asDouble
        mysteryCountry.value = countryObject["name"].asString
        targets.value = emptyList()
        gameWon.value = null
        guessesLatLng.value = emptyList()
        getCountries()
    }

    fun extractJson(): JsonArray {
        val inputStream = getApplication<Application>().resources.openRawResource(R.raw.countries)
        val reader = InputStreamReader(inputStream)
        val jsonArray = JsonParser.parseReader(reader).asJsonArray
        reader.close()
        return jsonArray
    }

    private fun getRandomCountry(): JsonObject {
        val jsonArray = extractJson()
        val randomIndex = (0 until jsonArray.size()).random()
        return jsonArray[randomIndex].asJsonObject
    }

    fun getCountries() {
        val jsonArray = extractJson()

        for (element in jsonArray) {
            val jsonObject = element.asJsonObject
            val name = jsonObject["name"].asString
            val shortName1 = jsonObject["iso2"].asString
            val shortName2 = jsonObject["iso3"].asString
            countries.value += name.lowercase()
            countries.value += shortName1.lowercase()
            countries.value += shortName2.lowercase()

        }
    }


}


