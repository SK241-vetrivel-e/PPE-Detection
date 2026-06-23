package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomBar() {

    NavigationBar(
        modifier = androidx.compose.ui.Modifier.fillMaxWidth()
    ) {

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.FlashOn, "Flash") },
            label = { Text("Flash") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Camera, "Capture") },
            label = { Text("Capture") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.PlayArrow, "Detect") },
            label = { Text("Detect") }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.History, "History") },
            label = { Text("History") }
        )

    }
}