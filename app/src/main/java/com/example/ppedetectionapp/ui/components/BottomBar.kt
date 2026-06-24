package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun BottomBar(

    onFlashClick: () -> Unit = {},
    onCaptureClick: () -> Unit = {},
    onDetectClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {}

) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(horizontal = 20.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {

            BottomButton(
                icon = Icons.Default.FlashOn,
                label = "Flash",
                onClick = onFlashClick
            )

            CaptureButton(
                onClick = onCaptureClick
            )

            BottomButton(
                icon = Icons.Default.PlayArrow,
                label = "Detect",
                onClick = onDetectClick
            )

            BottomButton(
                icon = Icons.Default.History,
                label = "History",
                onClick = onHistoryClick
            )

        }

    }

}

@Composable
private fun BottomButton(

    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit

) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = onClick
        ) {

            Icon(
                imageVector = icon,
                contentDescription = label
            )

        }

        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )

    }

}

@Composable
private fun CaptureButton(

    onClick: () -> Unit

) {

    IconButton(
        onClick = onClick
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .background(
                    color = Color(0xFF1976D2),
                    shape = CircleShape
                ),

            contentAlignment = Alignment.Center

        ) {

            Icon(
                imageVector = Icons.Default.Camera,
                contentDescription = "Capture",
                tint = Color.White
            )

        }

    }

}