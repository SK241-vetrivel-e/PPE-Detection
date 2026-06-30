package com.example.ppedetectionapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ppedetectionapp.camera.CameraManager
import com.example.ppedetectionapp.camera.CameraPreview
import com.example.ppedetectionapp.ui.components.OverlayView

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasCameraPermission = granted
        }

    // Create OverlayView only once
    val overlayView = remember {
        OverlayView(context)
    }

    if (hasCameraPermission) {

        Box(
            modifier = modifier.fillMaxSize()
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {

                    val previewView =
                        CameraPreview.create(context)

                    CameraManager.startCamera(
                        context,
                        lifecycleOwner,
                        previewView,
                        overlayView
                    )

                    previewView
                }
            )

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    overlayView
                }
            )

        }

    } else {

        Button(
            onClick = {
                permissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        ) {

            Text("Grant Camera Permission")

        }

    }

}