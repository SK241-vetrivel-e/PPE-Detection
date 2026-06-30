package com.example.ppedetectionapp.detector

import android.util.Log

object OutputParser {

    private const val CONFIDENCE_THRESHOLD = 0.40f

    fun parse(
        output: Array<Array<FloatArray>>,
        labels: List<String>
    ): List<BoundingBox> {

        val detections = mutableListOf<BoundingBox>()

        val predictions = output[0]

        val numClasses = labels.size
        val numPredictions = predictions[0].size

        Log.d("YOLO", "Predictions = $numPredictions")
        Log.d("YOLO", "Classes = $numClasses")

        for (i in 0 until numPredictions) {

            val cx = predictions[0][i]
            val cy = predictions[1][i]
            val w = predictions[2][i]
            val h = predictions[3][i]

            if (w <= 0f || h <= 0f)
                continue

            var bestScore = 0f
            var bestClass = -1

            for (c in 0 until numClasses) {

                val score = predictions[c + 4][i]

                if (score > bestScore) {
                    bestScore = score
                    bestClass = c
                }
            }

            if (bestClass == -1)
                continue

            if (bestScore < CONFIDENCE_THRESHOLD)
                continue

            val className = labels[bestClass]

            if (className.equals("none", true))
                continue

            val x1 = cx - w / 2f
            val y1 = cy - h / 2f
            val x2 = cx + w / 2f
            val y2 = cy + h / 2f

            detections.add(
                BoundingBox(
                    x1 = x1,
                    y1 = y1,
                    x2 = x2,
                    y2 = y2,
                    confidence = bestScore,
                    classId = bestClass,
                    className = className
                )
            )
        }

        Log.d("YOLO", "Boxes Before NMS = ${detections.size}")

        return detections
    }
}