package com.example.weathertripplanner.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathertripplanner.ui.screens.AddTripScreen
import com.example.weathertripplanner.ui.screens.WeatherScreen
import com.example.weathertripplanner.ui.screens.TripsScreen
import com.example.weathertripplanner.viewmodel.TripViewModel

enum class Screen {
    MAIN,
    TRIPS,
    ADD_TRIP
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val tripViewModel: TripViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.MAIN.name
    ) {
        composable(route = Screen.MAIN.name) {
            WeatherScreen(onNavigateToTrips = { navController.navigate(Screen.TRIPS.name) })
        }

        composable(route = Screen.TRIPS.name) {
            TripsScreen(
                viewModel = tripViewModel,
                onNavigateToId = { navController.navigate(Screen.ADD_TRIP.name) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(route = Screen.ADD_TRIP.name) {
            AddTripScreen(
                viewModel = tripViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}