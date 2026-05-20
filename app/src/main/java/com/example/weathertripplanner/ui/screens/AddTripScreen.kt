package com.example.weathertripplanner.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(onBack: () -> Unit) {
    // Состояния для хранения текста, который вводит пользователь
    var title by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    // Простая валидация: кнопка "Сохранить" будет активна, только если все поля заполнены
    val isFormValid = title.isNotBlank() && city.isNotBlank() && date.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Новая поездка", fontWeight = FontWeight.Bold) },
                // Кнопка "Назад" в левом верхнем углу
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Поле ввода названия поездки
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название поездки") },
                placeholder = { Text("Например: Отпуск у моря") },
                leadingIcon = { Icon(Icons.Default.CardTravel, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле ввода города
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Город назначения") },
                placeholder = { Text("Например: Брест") },
                leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Поле ввода даты
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Дата поездки") },
                placeholder = { Text("Например: 25.05.2026") },
                leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка сохранения
            Button(
                onClick = {
                    // Возвращаем пользователя назад после "сохранения"
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Сохранить", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
