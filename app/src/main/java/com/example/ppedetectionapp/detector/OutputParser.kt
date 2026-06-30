package com.example.ppedetectionapp.detector

import com.example.ppedetectionapp.utils.ImageUtils
import com.example.ppedetectionapp.utils.LetterboxInfo
import com.example.ppedetectionapp.utils.PPEConstants

object OutputParser {

    fun parse(
        output: Array<Array<FloatArray>>,
        labels: List<String>,
        letterboxInfo: LetterboxInfo
    ): List<BoundingBox> {

        val detections = mutableListOf<BoundingBox>()
        val predictions = output[0]

        for (i in 0 until 8400) {
            val cx = predictions[0][i]
            val cy = predictions[1][i]
            val w = predictions[2][i]
            val h = predictions[3][i]

            var bestScore = 0f
            var bestClass = -1

            for (c in labels.indices) {
                val score = predictions[c + 4][i]
                if (score > bestScore) {
                    bestScore = score
                    bestClass = c
                }
            }

            val thresh = PPEConstants.CLASS_CONF_THRESH[bestClass] ?: PPEConstants.DEFAULT_CONF
            if (bestScore < thresh) continue

            val box = ImageUtils.reverseLetterbox(cx, cy, w, h, letterboxInfo)
            
            detections.add(
                BoundingBox(
                    x1 = box[0],
                    y1 = box[1],
                    x2 = box[2],
                    y2 = box[3],
                    confidence = bestScore,
                    classId = bestClass,
                    className = labels[bestClass]
                )
            )
        }

        return detections
    }
}