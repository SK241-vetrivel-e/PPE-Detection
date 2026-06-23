package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CameraOverlay() {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        drawRect(
            color = Color.Green,
            topLeft = androidx.compose.ui.geometry.Offset(150f, 250f),
            size = androidx.compose.ui.geometry.Size(300f, 350f),
            style = Stroke(width = 6f)
        )

    }

}