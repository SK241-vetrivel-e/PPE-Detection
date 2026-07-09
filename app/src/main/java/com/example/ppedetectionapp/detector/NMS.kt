package com.example.ppedetectionapp.detector

import kotlin.math.max
import kotlin.math.min

object NMS {

    private const val IOU_THRESHOLD = 0.45f

    fun apply(boxes: List<BoundingBox>, iouThreshold: Float = 0.45f): List<BoundingBox> {
        val byClass = boxes.groupBy { it.classId }
        val kept = mutableListOf<BoundingBox>()

        for (clsDets in byClass.values) {
            val sortedBoxes = clsDets.sortedByDescending { it.confidence }.toMutableList()

            while (sortedBoxes.isNotEmpty()) {
                val current = sortedBoxes.removeAt(0)
                kept.add(current)

                val iterator = sortedBoxes.iterator()
                while (iterator.hasNext()) {
                    val box = iterator.next()
                    if (iou(current, box) > iouThreshold) {
                        iterator.remove()
                    }
                }
            }
        }

        return kept
    }

    private fun iou(a: BoundingBox, b: BoundingBox): Float {

        val left = max(a.x1, b.x1)
        val top = max(a.y1, b.y1)
        val right = min(a.x2, b.x2)
        val bottom = min(a.y2, b.y2)

        val width = max(0f, right - left)
        val height = max(0f, bottom - top)

        val intersection = width * height

        val areaA = (a.x2 - a.x1) * (a.y2 - a.y1)
        val areaB = (b.x2 - b.x1) * (b.y2 - b.y1)

        val union = areaA + areaB - intersection

        if (union <= 0f) return 0f

        return intersection / union
    }
}