package com.example.ppedetectionapp.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.ppedetectionapp.detector.BoundingBox
import com.example.ppedetectionapp.detector.Detector
import com.example.ppedetectionapp.detector.NMS
import com.example.ppedetectionapp.detector.OutputParser
import com.example.ppedetectionapp.detector.PPEProcessor
import com.example.ppedetectionapp.model.WorkerResult
import com.example.ppedetectionapp.utils.ImageUtils
import com.example.ppedetectionapp.utils.LabelLoader
import com.example.ppedetectionapp.utils.PPEConstants
import com.example.ppedetectionapp.utils.TensorUtils
import kotlin.math.max

class FrameAnalyzer(
    context: Context,
    private val onResults: (List<WorkerResult>, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector = Detector(context)
    private val ppeProcessor = PPEProcessor()
    private val labels = LabelLoader.loadLabels(context)
    private val converter = YuvToRgbConverter(context)
    
    // Cache for bitmaps
    private var rgbBitmap: Bitmap? = null

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        try {
            val mediaImage = image.image ?: return
            
            // 1. INFERENCE ROTATION: Rotation from sensor to current physical orientation
            val inferenceRotation = image.imageInfo.rotationDegrees
            
            // 2. DISPLAY ROTATION: Rotation from sensor to UI (Portrait = 90 deg for most devices)
            // We assume a Portrait-locked app where UI rotation is always ROTATION_0 relative to device natural.
            // Sensor orientation is usually 90 degrees offset from Portrait.
            // Note: In a production app, we would query CameraCharacteristics.SENSOR_ORIENTATION.
            val displayRotation = 90 

            // 3. Convert YUV to RGB (Sensor space)
            if (rgbBitmap == null || rgbBitmap!!.width != image.width || rgbBitmap!!.height != image.height) {
                rgbBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            }
            converter.yuvToRgb(mediaImage, rgbBitmap!!)

            // 4. Create Rotated Bitmap for YOLO (Model sees an upright world)
            val rotatedBitmap = ImageUtils.rotateBitmap(rgbBitmap!!, inferenceRotation.toFloat())

            // 5. Run YOLO Inference
            val (letterboxed, info) = ImageUtils.letterbox(rotatedBitmap)
            val tensor = TensorUtils.bitmapToTensor(letterboxed)
            val result = detector.detect(tensor)

            // 6. Parse Detections (Relative to rotatedBitmap)
            var boxes = OutputParser.parse(result.output, labels, info)
            boxes = NMS.apply(boxes, PPEConstants.NMS_IOU)

            // 7. TRANSFORM COORDINATES: Map from Inference Space -> UI Space
            val deltaRotation = (displayRotation - inferenceRotation + 360) % 360
            val correctedBoxes = boxes.map { rotateBoundingBox(it, deltaRotation) }

            if (image.imageInfo.timestamp % 60 == 0L) {
                Log.d("ORIENTATION_FIX", "InfRot: $inferenceRotation | UI: $displayRotation | Delta: $deltaRotation")
            }

            // 8. Compliance & Smoothing (on corrected boxes)
            val workerResults = ppeProcessor.process(correctedBoxes)
            
            // 9. Deliver Results with Portrait Dimensions
            val uiWidth = image.height
            val uiHeight = image.width
            
            onResults(workerResults, uiWidth, uiHeight)

        } catch (e: Exception) {
            Log.e("YOLO", "Analysis Error", e)
        } finally {
            image.close()
        }
    }

    /**
     * Rotates normalized bounding box coordinates clockwise.
     * Maps from current inference coordinate system to the target UI coordinate system.
     */
    private fun rotateBoundingBox(
        box: BoundingBox,
        rotationDegrees: Int
    ): BoundingBox {
        return when (rotationDegrees) {
            90 -> BoundingBox(
                x1 = 1f - box.y2,
                y1 = box.x1,
                x2 = 1f - box.y1,
                y2 = box.x2,
                confidence = box.confidence,
                classId = box.classId,
                className = box.className
            )
            180 -> BoundingBox(
                x1 = 1f - box.x2,
                y1 = 1f - box.y2,
                x2 = 1f - box.x1,
                y2 = 1f - box.y1,
                confidence = box.confidence,
                classId = box.classId,
                className = box.className
            )
            270 -> BoundingBox(
                x1 = box.y1,
                y1 = 1f - box.x2,
                x2 = box.y2,
                y2 = 1f - box.x1,
                confidence = box.confidence,
                classId = box.classId,
                className = box.className
            )
            else -> box
        }
    }
}