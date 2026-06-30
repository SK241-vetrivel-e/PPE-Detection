package com.example.ppedetectionapp.camera

import android.content.Context
import android.view.OrientationEventListener
import android.view.Surface
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.ppedetectionapp.model.WorkerResult
import java.util.concurrent.Executors

object CameraManager {

    private var camera: Camera? = null
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    val imageCapture = ImageCapture.Builder().build()
    
    private var orientationEventListener: OrientationEventListener? = null

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        onResults: (List<WorkerResult>, Int, Int) -> Unit
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

            imageAnalysis.setAnalyzer(
                cameraExecutor,
                FrameAnalyzer(context, onResults)
            )

            // Dynamic Target Rotation for Analysis:
            // We update targetRotation to match the physical orientation of the device.
            // This ensures imageInfo.rotationDegrees correctly reflects the sensor-to-physical rotation,
            // allowing the YOLO model to receive an upright image for better detection.
            orientationEventListener = object : OrientationEventListener(context) {
                override fun onOrientationChanged(orientation: Int) {
                    if (orientation == ORIENTATION_UNKNOWN) return
                    val rotation = when (orientation) {
                        in 45 until 135 -> Surface.ROTATION_270
                        in 135 until 225 -> Surface.ROTATION_180
                        in 225 until 315 -> Surface.ROTATION_90
                        else -> Surface.ROTATION_0
                    }
                    imageAnalysis.targetRotation = rotation
                    imageCapture.targetRotation = rotation
                    // We specifically DO NOT update preview.targetRotation here
                    // to keep the camera preview natural and upright in the Portrait UI.
                }
            }
            orientationEventListener?.enable()

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

        }, ContextCompat.getMainExecutor(context))

    }

    fun stopCamera() {
        orientationEventListener?.disable()
        orientationEventListener = null
    }

}