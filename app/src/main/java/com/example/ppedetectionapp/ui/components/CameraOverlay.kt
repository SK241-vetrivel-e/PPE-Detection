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

    showPersonBox: Boolean,

    left: Float = 0f,

    top: Float = 0f,

    width: Float = 0f,

    height: Float = 0f

) {

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {

        if (showPersonBox) {

            drawRect(

                color = Color.Green,

                topLeft = Offset(left, top),

                size = Size(width, height),

                style = Stroke(width = 6f)

            )

        }

    }

}