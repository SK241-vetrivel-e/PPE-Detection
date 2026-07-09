package com.example.ppedetectionapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ppedetectionapp.ui.screens.HomeScreen
import com.example.ppedetectionapp.ui.screens.SettingsScreen
import com.example.ppedetectionapp.viewmodel.PPEViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    // Create the ViewModel here to share it between Home and Settings
    val ppeViewModel: PPEViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                viewModel = ppeViewModel,
                onSettingsClick = {
                    navController.navigate("settings")
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = ppeViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
