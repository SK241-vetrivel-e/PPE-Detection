package com.example.ppedetectionapp.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ppedetectionapp.camera.CameraManager
import com.example.ppedetectionapp.camera.CameraPreview
import com.example.ppedetectionapp.ui.components.CameraOverlay
import com.example.ppedetectionapp.viewmodel.PPEViewModel

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    viewModel: PPEViewModel = viewModel()
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

        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {

                    val previewView =
                        CameraPreview.create(context)

                    CameraManager.startCamera(
                        context,
                        lifecycleOwner,
                        previewView
                    ) { results, width, height ->
                        viewModel.updateResults(results, width, height)
                    }

                    previewView
                }
            )

            CameraOverlay(
                workerResults = viewModel.workerResults,
                sourceWidth = viewModel.sourceImageWidth,
                sourceHeight = viewModel.sourceImageHeight
            )
        }

    } else {

        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

}