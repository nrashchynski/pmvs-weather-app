package com.example.weathertripplanner.domain.model

data class Weather(
    val cityName: String,
    val temperature: Double,
    val humidity: Int,
    val description: String,
    val iconUrl: String,
    val windSpeed: Double
)
