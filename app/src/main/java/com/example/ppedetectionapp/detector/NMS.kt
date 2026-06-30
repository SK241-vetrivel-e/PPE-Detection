package com.example.ppedetectionapp.detector

object NMS {

    private const val IOU_THRESHOLD = 0.30f

    fun apply(boxes: List<BoundingBox>): List<BoundingBox> {

        val result = mutableListOf<BoundingBox>()

        val sortedBoxes = boxes.sortedByDescending {
            it.confidence
        }.toMutableList()

        while (sortedBoxes.isNotEmpty()) {

            val first = sortedBoxes.removeAt(0)

            result.add(first)

            val iterator = sortedBoxes.iterator()

            while (iterator.hasNext()) {

                val box = iterator.next()

                if (first.classId == box.classId &&
                    iou(first, box) > IOU_THRESHOLD
                ) {
                    iterator.remove()
                }

            }

        }

        return result
    }

    private fun iou(
        a: BoundingBox,
        b: BoundingBox
    ): Float {

        val x1 = maxOf(a.x1, b.x1)
        val y1 = maxOf(a.y1, b.y1)

        val x2 = minOf(a.x2, b.x2)
        val y2 = minOf(a.y2, b.y2)

        val intersection =
            maxOf(0f, x2 - x1) *
                    maxOf(0f, y2 - y1)

        val areaA =
            (a.x2 - a.x1) *
                    (a.y2 - a.y1)

        val areaB =
            (b.x2 - b.x1) *
                    (b.y2 - b.y1)

        val union =
            areaA + areaB - intersection

        if (union <= 0f)
            return 0f

        return intersection / union
    }
}