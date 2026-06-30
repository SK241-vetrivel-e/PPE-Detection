package com.example.ppedetectionapp.camera

import android.content.Context
import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.ppedetectionapp.detector.Detector
import com.example.ppedetectionapp.detector.NMS
import com.example.ppedetectionapp.detector.OutputParser
import com.example.ppedetectionapp.ui.components.OverlayView
import com.example.ppedetectionapp.utils.ImageUtils
import com.example.ppedetectionapp.utils.LabelLoader
import com.example.ppedetectionapp.utils.TensorUtils
import com.example.ppedetectionapp.detector.PPEMatcher
import com.example.ppedetectionapp.model.PersonStatus

class FrameAnalyzer(
    private val context: Context,
    private val overlayView: OverlayView
) : ImageAnalysis.Analyzer {

    private val detector = Detector(context)

    private var frameCount = 0

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(image: ImageProxy) {

        frameCount++

        // Analyze every 3rd frame
        if (frameCount % 3 != 0) {
            image.close()
            return
        }

        val bitmap = ImageUtils.imageProxyToBitmap(
            context,
            image
        )

        if (bitmap != null) {

            val resized = ImageUtils.resize(bitmap)

            val tensor = TensorUtils.bitmapToTensor(resized)

            val result = detector.detect(tensor)

            val labels = LabelLoader.loadLabels(context)

            val boxes = OutputParser.parse(
                output = result.output,
                labels = labels
            )
            val filteredBoxes = NMS.apply(boxes)
            for (box in filteredBoxes) {

                Log.d(
                    "PPE",
                    "Box -> Class='${box.className}'  Id=${box.classId}"
                )
            }
            for (box in filteredBoxes) {
                Log.d(
                    "YOLO",
                    "Class = ${box.className}  Confidence = ${box.confidence}"
                )
            }

// Draw bounding boxes
            overlayView.setResults(filteredBoxes)

// Match PPE to each detected person
            val workers = PPEMatcher.match(filteredBoxes)
            Log.d("PPE", "Workers Found = ${workers.size}")

            for (worker in workers) {

                Log.d("PPE", "-----------")
                Log.d("PPE", "Helmet : ${worker.helmet}")
                Log.d("PPE", "Vest : ${worker.vest}")
                Log.d("PPE", "Gloves : ${worker.gloves}")
                Log.d("PPE", "Boots : ${worker.boots}")
                Log.d("PPE", "Goggles : ${worker.goggles}")
            }

// Print PPE status
            for (worker in workers) {

                Log.d("PPE", "========================")
                Log.d("PPE", "Worker Detected")
                Log.d("PPE", "Helmet  : ${worker.helmet}")
                Log.d("PPE", "Vest    : ${worker.vest}")
                Log.d("PPE", "Gloves  : ${worker.gloves}")
                Log.d("PPE", "Boots   : ${worker.boots}")
                Log.d("PPE", "Goggles : ${worker.goggles}")
                Log.d("PPE", "========================")
            }

            Log.d(
                "YOLO",
                "Total Boxes = ${filteredBoxes.size}"
            )
        }

        image.close()
    }
}