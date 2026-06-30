package com.example.ppedetectionapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ppedetectionapp.utils.PPEConstants

@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "PPE Monitor Settings",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text("System Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(8.dp))

                Text("Person Confidence: ${(PPEConstants.CLASS_CONF_THRESH[6]!! * 100).toInt()}%")
                Text("Gear Confidence: ${(PPEConstants.DEFAULT_CONF * 100).toInt()}%")
                Text("Smoothing Window: ${PPEConstants.SMOOTH_WINDOW} frames")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Model Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("YOLOv8-PPE Optimized")
                Text("Classes: 11")
                Text("App Version: 1.1")

            }

        }

    }

}