package com.example.globalfugitive

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(
    private val countryDao: CountryDao,
    application: Application
) : AndroidViewModel(application) {

    private val appContext = getApplication<Application>()

    private val _countries = MutableLiveData<List<String>>()
    val countries: LiveData<List<String>> = _countries

    var guesses = mutableStateOf<List<String>>(emptyList())
        private set
//    var countries = mutableStateOf<List<String>>(emptyList())
    var mysteryCountry = mutableStateOf<String?>(null)
        private set
    var mysteryLat = mutableStateOf<Double?>(null)
        private set
    var mysteryLong = mutableStateOf<Double?>(null)
    var guessDistance = mutableStateOf<Int?>(null)
    var guessesLatLng = mutableStateOf<List<LatLng>>(emptyList())
    var gameWon = mutableStateOf<Boolean?>(null)
        private set



    fun gameEnd(guess: String): Boolean {
        return when {
            correctGuess(guess) -> {
                gameWon.value = true
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            guesses.value.size >= 5 -> {
                gameWon.value = false
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            else -> false
        }
    }

    fun correctGuess(guess: String): Boolean {
        return guess.lowercase() == mysteryCountry.value.toString().lowercase()
    }

    fun validGuess(guess: String): Boolean {
        return countries.value?.contains(guess.lowercase()) == true &&
                !guesses.value.toString().lowercase().contains(guess.lowercase()) &&
                guesses.value.size < 5
    }

    fun addGuess (guess: String, guessLatLng: LatLng) {
        val lat1 = guessLatLng.latitude
        val long1 = guessLatLng.longitude
        val lat2 = mysteryLat.value!!
        val long2 = mysteryLong.value!!
        haversineDistance(lat1, long1, lat2, long2)

        val formatter = NumberFormat.getNumberInstance(Locale.UK)
        val distanceString = formatter.format(guessDistance.value)

        if(validGuess(guess)) {
            if (correctGuess(guess)) {
                guesses.value += "$guess ✅"
            }
            else {
                guesses.value += "$guess ${distanceString}km  ❌"
                guessesLatLng.value += guessLatLng
            }

        }
    }

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double) {
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

        guessDistance.value = distance

//        val formatter = NumberFormat.getNumberInstance(Locale.UK)
//        return formatter.format(distance)
    }

    suspend fun startNewGame() {

        val randomCountry = getRandomCountry()
        mysteryLat.value = randomCountry?.latitude
        mysteryLong.value = randomCountry?.longitude
        mysteryCountry.value = randomCountry?.name
        guesses.value = emptyList()
        gameWon.value = null
        guessesLatLng.value = emptyList()
        getCountries()

    }

//    fun extractJson(): JsonArray {
//        val inputStream = appContext.resources.openRawResource(R.raw.countries)
//        val reader = InputStreamReader(inputStream)
//        val jsonArray = JsonParser.parseReader(reader).asJsonArray
//        reader.close()
//        return jsonArray
//    }

    private suspend fun getRandomCountry(): Country? {
        val countryCount = countryDao.getCountryCount()

        if (countryCount == 0) return null

        val randomIndex = (0 until ( countryCount) ).random()
        return countryDao.getCountryById(randomIndex)
    }

    // Function to fetch countries from the Room database
    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch all countries from the database
                val listOfCountries = countryDao.getAllCountries()
                val countryNames = mutableListOf<String>()

                // Iterate through the countries and extract names
                for (country in listOfCountries) {
                    countryNames.add(country.name.lowercase())
                    country.iso2?.let { countryNames.add(it.lowercase()) }
                    country.iso3?.let { countryNames.add(it.lowercase()) }
                }

                // Post the extracted country names to LiveData
                _countries.postValue(countryNames)
            } catch (e: Exception) {
                // Handle the exception appropriately
                e.printStackTrace()
            }
        }
    }


}


