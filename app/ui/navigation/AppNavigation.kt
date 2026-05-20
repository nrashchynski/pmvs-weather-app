package com.teamweather.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.teamweather.app.ui.screens.AddTripScreen
import com.teamweather.app.ui.screens.TripsScreen
import com.teamweather.app.ui.screens.WeatherScreen

enum class Screen {
    WEATHER, TRIPS, ADD_TRIP
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WEATHER.name // Стартовый экран
    ) {
        composable(route = Screen.WEATHER.name) {
            WeatherScreen(onNavigateToTrips = { navController.navigate(Screen.TRIPS.name) })
        }
        composable(route = Screen.TRIPS.name) {
            TripsScreen(onNavigateToId = { navController.navigate(Screen.ADD_TRIP.name) })
        }
        composable(route = Screen.ADD_TRIP.name) {
            AddTripScreen(onBack = { navController.popBackStack() })
        }
    }
}