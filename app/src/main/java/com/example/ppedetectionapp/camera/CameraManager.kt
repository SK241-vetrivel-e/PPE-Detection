package com.example.ppedetectionapp.camera

import android.content.Context
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.ppedetectionapp.ui.components.OverlayView
import java.util.concurrent.Executors

object CameraManager {

    private var camera: Camera? = null

    // Background thread for YOLO inference
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    val imageCapture = ImageCapture.Builder().build()

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        overlayView: OverlayView
    ) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()

            preview.setSurfaceProvider(
                previewView.surfaceProvider
            )

            val imageAnalysis =
                ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                    )
                    .build()

            // Run YOLO on background thread
            imageAnalysis.setAnalyzer(
                cameraExecutor,
                FrameAnalyzer(
                    context,
                    overlayView
                )
            )

            val cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalysis
                )

            } catch (e: Exception) {

                e.printStackTrace()

            }

        },
            // IMPORTANT:
            // CameraX setup MUST be on Main Thread
            ContextCompat.getMainExecutor(context)
        )

    }
}