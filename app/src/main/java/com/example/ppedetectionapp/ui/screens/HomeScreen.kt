package com.example.ppedetectionapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ppedetectionapp.ui.components.TopBar
import com.example.ppedetectionapp.viewmodel.PPEViewModel

@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit = {},
    viewModel: PPEViewModel = viewModel()
) {
    val workerResults = viewModel.workerResults
    val personDetected = workerResults.isNotEmpty()

    Scaffold(
        topBar = {
            TopBar(
                onMenuClick = {},
                onSettingsClick = onSettingsClick
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Live Camera Preview (Includes Overlay)
            CameraScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = viewModel
            )

            // HUD Panel
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
                    .background(Color(0x99000000), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = "PPE MONITOR",
                    color = Color.Yellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(
                    text = "Workers: ${workerResults.size}",
                    color = Color.White,
                    fontSize = 10.sp
                )
                val violations = workerResults.count { !it.compliant }
                Text(
                    text = "Violations: $violations",
                    color = if (violations > 0) Color.Red else Color.Green,
                    fontSize = 10.sp
                )
            }

            // Waiting Message
            if (!personDetected) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Waiting for Person...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}