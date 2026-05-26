package com.example.weathertripplanner.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertripplanner.BuildConfig
import com.example.weathertripplanner.data.database.AppDatabase
import com.example.weathertripplanner.data.model.TripEntity
import com.example.weathertripplanner.data.network.WeatherApiService
import com.example.weathertripplanner.data.network.WeatherResponse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(application: Application) : AndroidViewModel(application) {

    private val tripDao = AppDatabase.getDatabase(application).tripDao()
    private val weatherApi = WeatherApiService.create()

    val tripsState: StateFlow<List<TripEntity>> = tripDao.getAllTrips()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _weatherData = mutableStateOf<WeatherResponse?>(null)
    val weatherData: State<WeatherResponse?> = _weatherData

    private val _isLoadingWeather = mutableStateOf(false)
    val isLoadingWeather: State<Boolean> = _isLoadingWeather

    fun addTrip(title: String, city: String, date: String) {
        viewModelScope.launch {
            val newTrip = TripEntity(title = title, city = city, date = date)
            tripDao.insertTrip(newTrip)
        }
    }

    fun deleteTrip(trip: TripEntity) {
        viewModelScope.launch {
            tripDao.deleteTrip(trip)
        }
    }

    suspend fun getTripById(id: Int): TripEntity? {
        return tripDao.getTripById(id)
    }

    fun fetchWeather(city: String) {
        viewModelScope.launch {
            _isLoadingWeather.value = true
            try {
                // Fetching weather using the API key from BuildConfig
                val response = weatherApi.getWeather(city, BuildConfig.OPENWEATHER_API_KEY)
                _weatherData.value = response
            } catch (t: Throwable) {
                // Log the error and prevent crash (e.g., SecurityException for missing permission)
                Log.e("TripViewModel", "Error fetching weather for $city", t)
                _weatherData.value = null
            } finally {
                _isLoadingWeather.value = false
            }
        }
    }
}
