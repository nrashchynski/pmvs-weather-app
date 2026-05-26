package com.example.weathertripplanner.data.network

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherDescriptionDto>,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("name") val cityName: String
)

data class MainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Int
)

data class WeatherDescriptionDto(
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindDto(
    @SerializedName("speed") val speed: Double
)
