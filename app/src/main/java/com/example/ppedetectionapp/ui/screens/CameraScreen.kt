package com.example.ppedetectionapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
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

    if (hasCameraPermission) {

        AndroidView(
            modifier = modifier.fillMaxSize(),
            factory = {

                val previewView =
                    CameraPreview.create(context)

                CameraManager.startCamera(
                    context,
                    lifecycleOwner,
                    previewView
                )

                previewView

            }
        )

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