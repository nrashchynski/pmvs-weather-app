package com.example.weathertripplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weathertripplanner.data.model.TripEntity
import com.example.weathertripplanner.viewmodel.TripViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: Int,
    viewModel: TripViewModel,
    onBack: () -> Unit
) {
    var trip by remember { mutableStateOf<TripEntity?>(null) }
    var isLoadingTrip by remember { mutableStateOf(true) }
    val weatherData by viewModel.weatherData
    val isLoadingWeather by viewModel.isLoadingWeather

    LaunchedEffect(tripId) {
        trip = viewModel.getTripById(tripId)
        isLoadingTrip = false
        trip?.let {
            viewModel.fetchWeather(it.city)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(trip?.title ?: "Детали поездки", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (isLoadingTrip) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (trip == null) {
                Text(
                    text = "Поездка не найдена",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                val currentTrip = trip!!
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Информация о маршруте",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "📍 Город: ${currentTrip.city}", style = MaterialTheme.typography.bodyLarge)
                            Text(text = "📅 Дата: ${currentTrip.date}", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Прогноз погоды",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            if (isLoadingWeather) {
                                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                            } else {
                                weatherData?.let { weather ->
                                    if (weather.weather.isNotEmpty()) {
                                        AsyncImage(
                                            model = "https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png",
                                            contentDescription = null,
                                            modifier = Modifier.size(64.dp)
                                        )
                                        Text(
                                            text = weather.weather[0].description.replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Text(
                                        text = "${weather.main.temp.toInt()}°C",
                                        fontSize = 48.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Ветер", style = MaterialTheme.typography.labelSmall)
                                            Text("${weather.wind.speed} м/с", fontWeight = FontWeight.Bold)
                                        }
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text("Влажность", style = MaterialTheme.typography.labelSmall)
                                            Text("${weather.main.humidity}%", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                } ?: Text("Не удалось загрузить данные о погоде")
                            }
                        }
                    }
                }
            }
        }
    }
}
