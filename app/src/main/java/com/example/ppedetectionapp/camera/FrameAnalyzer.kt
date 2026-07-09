package com.example.ppedetectionapp.camera

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
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
import com.example.ppedetectionapp.viewmodel.PPEViewModel

class FrameAnalyzer(
    context: Context,
    private val viewModel: PPEViewModel,
    private val onResults: (List<WorkerResult>, Int, Int, Long) -> Unit
) : ImageAnalysis.Analyzer {

    private val ppeProcessor = PPEProcessor()
    private val labels = LabelLoader.loadLabels(context)
    private val converter = YuvToRgbConverter(context)
    
    // Cache for bitmaps
    private var rgbBitmap: Bitmap? = null

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    @OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {
        val currentDetector = viewModel.detector
        if (currentDetector == null) {
            image.close()
            return
        }

        try {
            val mediaImage = image.image ?: return
            
            // 1. INFERENCE ROTATION
            val inferenceRotation = image.imageInfo.rotationDegrees
            
            // 2. DISPLAY ROTATION
            val displayRotation = 90 

            // 3. Convert YUV to RGB
            if (rgbBitmap == null || rgbBitmap!!.width != image.width || rgbBitmap!!.height != image.height) {
                rgbBitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
            }
            converter.yuvToRgb(mediaImage, rgbBitmap!!)

            // 4. Transform Bitmap
            val (letterboxed, info) = ImageUtils.rotateAndLetterbox(rgbBitmap!!, inferenceRotation.toFloat())

            // 5. Run YOLO Inference
            val tensor = TensorUtils.bitmapToTensor(letterboxed)
            
            val startTime = SystemClock.uptimeMillis()
            val result = currentDetector.detect(tensor)
            val inferenceTime = SystemClock.uptimeMillis() - startTime

            if (result == null) return

            // 6. Parse Detections
            var boxes = OutputParser.parse(result.output, labels, info)
            boxes = NMS.apply(boxes, PPEConstants.NMS_IOU)

            // 7. TRANSFORM COORDINATES
            val deltaRotation = (displayRotation - inferenceRotation + 360) % 360
            val correctedBoxes = boxes.map { rotateBoundingBox(it, deltaRotation) }

            // 8. Compliance & Smoothing
            val workerResults = ppeProcessor.process(correctedBoxes)
            
            // 9. Deliver Results
            val uiWidth = image.height
            val uiHeight = image.height // Fix: Use image.height for both or correct mapping if needed
            
            onResults(workerResults, image.height, image.width, inferenceTime)

        } catch (e: Exception) {
            Log.e("YOLO", "Analysis Error", e)
        } finally {
            image.close()
        }
    }

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
