package com.example.weathertripplanner

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weathertripplanner.ui.navigation.AppNavigation
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testNavigationToTrips() {
        // Verify we start on Weather Screen (Search city text)
        composeTestRule.onNodeWithText("Поиск города").assertExists()

        // Click button to go to Trips
        composeTestRule.onNodeWithText("Открыть «Мои поездки»").performClick()

        // Verify we are on Trips Screen
        composeTestRule.onNodeWithText("Мои Поездки").assertExists()
    }
}
