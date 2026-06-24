package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun CameraOverlay(

    showPersonBox: Boolean = false

) {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        if (showPersonBox) {

            drawRect(

                color = Color.Green,

                topLeft = Offset(
                    size.width * 0.25f,
                    size.height * 0.20f
                ),

                size = Size(
                    size.width * 0.50f,
                    size.height * 0.60f
                ),

                style = Stroke(
                    width = 6f
                )

            )

        }

    }

}