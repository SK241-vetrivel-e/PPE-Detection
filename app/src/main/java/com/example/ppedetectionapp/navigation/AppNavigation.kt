package com.example.ppedetectionapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ppedetectionapp.ui.screens.HomeScreen
import com.example.ppedetectionapp.ui.screens.SettingsScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            HomeScreen(
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )

        }

        composable("settings") {

            SettingsScreen()

        }

    }

}