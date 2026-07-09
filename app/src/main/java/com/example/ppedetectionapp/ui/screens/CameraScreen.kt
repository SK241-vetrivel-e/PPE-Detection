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

    // Initialize detector once on start
    LaunchedEffect(Unit) {
        viewModel.initializeDetector(context, viewModel.currentDevice)
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    if (hasCameraPermission) {
        Box(modifier = modifier.fillMaxSize()) {
            
            // We no longer use key(currentDevice) to avoid camera restarts
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = CameraPreview.create(ctx)
                    CameraManager.startCamera(
                        context = ctx,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        viewModel = viewModel
                    ) { results, width, height, time ->
                        viewModel.updateResults(results, width, height, time)
                    }
                    previewView
                },
                onRelease = {
                    CameraManager.stopCamera()
                }
            )

            if (viewModel.showBoundingBoxes) {
                CameraOverlay(
                    workerResults = viewModel.workerResults,
                    sourceWidth = viewModel.sourceImageWidth,
                    sourceHeight = viewModel.sourceImageHeight
                )
            }
        }
    } else {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Grant Camera Permission")
            }
        }
    }
}
