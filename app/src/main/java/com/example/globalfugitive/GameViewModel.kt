package com.example.globalfugitive

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonParser
import java.io.InputStreamReader

class GameViewModel(application: Application) : AndroidViewModel(application) {

    var targets = mutableStateOf<List<String>>(emptyList())
        private set
    var mysteryCountry = mutableStateOf<String?>(null)
        private set
    var gameWon = mutableStateOf<Boolean?>(null)
        private set

    fun gameEnd(guess: String): Boolean {
        return when {
            guess == mysteryCountry.value -> {
                gameWon.value = true
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            targets.value.size == 4 -> {
                gameWon.value = false
                println("gameWon value @ GameViewModel: $gameWon")
                true
            }
            else -> false
        }
    }

    fun addGuess (guess: String) {
        if(!targets.value.contains(guess) && targets.value.size < 5) {
            targets.value += guess
        }
    }

    fun startNewGame() {
        mysteryCountry.value = getRandomCountry()
        targets.value = emptyList()
        gameWon.value = null
    }

    private fun getRandomCountry(): String? {

        val inputStream = getApplication<Application>().resources.openRawResource(R.raw.countries)
        val reader = InputStreamReader(inputStream)
        val jsonArray = JsonParser.parseReader(reader).asJsonArray

        reader.close()

        val randomIndex = (0 until jsonArray.size()).random()
        val countryObject = jsonArray[randomIndex].asJsonObject

        return countryObject["name"].asString

    }

}

class GameViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


