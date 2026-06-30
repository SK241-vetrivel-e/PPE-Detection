package com.example.ppedetectionapp.ui.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.ppedetectionapp.detector.BoundingBox

class OverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var boxes = emptyList<BoundingBox>()

    private val boxPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 6f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 38f
        isFakeBoldText = true
        isAntiAlias = true
    }

    private val textBackgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun setResults(results: List<BoundingBox>) {
        boxes = results
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (box in boxes) {

            val left = box.x1 * width
            val top = box.y1 * height
            val right = box.x2 * width
            val bottom = box.y2 * height

            val color = getColor(box.className)

            boxPaint.color = color
            textBackgroundPaint.color = color

            // Draw box
            canvas.drawRoundRect(
                RectF(left, top, right, bottom),
                12f,
                12f,
                boxPaint
            )

            val label =
                "${box.className} ${(box.confidence * 100).toInt()}%"

            val textWidth = textPaint.measureText(label)

            val rect = RectF(
                left,
                top - 50,
                left + textWidth + 30,
                top
            )

            canvas.drawRoundRect(
                rect,
                10f,
                10f,
                textBackgroundPaint
            )

            canvas.drawText(
                label,
                left + 15,
                top - 15,
                textPaint
            )
        }
    }

    private fun getColor(name: String): Int {

        return when (name.lowercase()) {

            "person" -> Color.BLUE

            "helmet" -> Color.GREEN

            "vest" -> Color.rgb(255,140,0)

            "gloves" -> Color.MAGENTA

            "boots" -> Color.CYAN

            "goggles" -> Color.YELLOW

            "no_helmet" -> Color.RED

            "no_gloves" -> Color.RED

            "no_boots" -> Color.RED

            "no_goggle" -> Color.RED

            else -> Color.WHITE
        }
    }
}