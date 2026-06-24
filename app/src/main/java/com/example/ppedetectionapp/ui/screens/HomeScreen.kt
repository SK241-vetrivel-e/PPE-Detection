package com.example.ppedetectionapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ppedetectionapp.ui.components.CameraOverlay
import com.example.ppedetectionapp.ui.components.PPEStatusRow
import com.example.ppedetectionapp.ui.components.TopBar

@Composable
fun HomeScreen() {

    // Later YOLO will update this
    val personDetected = false

    Scaffold(

        topBar = {

            TopBar(

                onMenuClick = {},

                onSettingsClick = {}

            )

        }

    ) { innerPadding ->

        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {

            // Live Camera Preview
            CameraScreen(
                modifier = Modifier.fillMaxSize()
            )

            // Person Bounding Box
            CameraOverlay(
                showPersonBox = personDetected
            )

            // Waiting Message
            if (!personDetected) {

                Box(

                    modifier = Modifier.fillMaxSize(),

                    contentAlignment = Alignment.Center

                ) {

                    Text(
                        text = "Waiting for Person..."
                    )

                }

            }

            // PPE Status Card
            if (personDetected) {

                Box(

                    modifier = Modifier.fillMaxSize(),

                    contentAlignment = Alignment.BottomCenter

                ) {

                    PPEStatusRow(

                        helmet = "✅",

                        vest = "❌",

                        gloves = "✅",

                        shoes = "✅",

                        score = "75%"

                    )

                }

            }

        }

    }

}