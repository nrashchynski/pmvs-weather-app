package com.example.weathertripplanner.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

    // Используем ключ напрямую, чтобы избежать проблем с BuildConfig
    private val API_KEY = "e74d803483883a40b4d7b2fde2737241"

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
                Log.d("WeatherDebug", "Fetching for $city using Key: $API_KEY")
                val response = weatherApi.getWeather(city, API_KEY)
                _weatherData.value = response
                Log.d("WeatherDebug", "Weather loaded successfully")
            } catch (t: Throwable) {
                Log.e("WeatherDebug", "Error fetching weather for $city", t)
                _weatherData.value = null
            } finally {
                _isLoadingWeather.value = false
            }
        }
    }
}
