package com.example.ppedetectionapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.ppedetectionapp.ui.screens.HomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen()
        }
    }

}