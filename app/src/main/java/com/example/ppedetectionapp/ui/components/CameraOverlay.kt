package com.example.ppedetectionapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ppedetectionapp.model.WorkerResult
import kotlin.math.max

@OptIn(ExperimentalTextApi::class)
@Composable
fun CameraOverlay(
    workerResults: List<WorkerResult>,
    sourceWidth: Int,
    sourceHeight: Int,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        if (sourceWidth <= 0 || sourceHeight <= 0) return@Canvas

        val canvasW = size.width
        val canvasH = size.height

        // Calculate FILL_CENTER mapping to handle Portrait/Landscape aspect ratio differences
        val scale = max(canvasW / sourceWidth, canvasH / sourceHeight)
        val scaledW = sourceWidth * scale
        val scaledH = sourceHeight * scale
        val offsetX = (canvasW - scaledW) / 2f
        val offsetY = (canvasH - scaledH) / 2f

        workerResults.forEachIndexed { index, worker ->
            val box = worker.personBox
            val score = worker.complianceScore

            // Map normalized coordinates to canvas pixels
            val left = box.x1 * scaledW + offsetX
            val top = box.y1 * scaledH + offsetY
            val right = box.x2 * scaledW + offsetX
            val bottom = box.y2 * scaledH + offsetY

            val color = when {
                score >= 1.0f -> Color(0xFF32CD32) // GREEN
                score >= 0.6f -> Color(0xFFFFC800) // YELLOW
                else -> Color(0xFFDC3232) // RED
            }

            // 1. Draw Bounding Box
            drawRoundRect(
                color = color,
                topLeft = Offset(left, top),
                size = Size(max(0f, right - left), max(0f, bottom - top)),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                style = Stroke(width = 2.5.dp.toPx())
            )

            // 2. Worker Label (Top of box)
            val labelText = "WORKER ${index + 1} (${(score * 100).toInt()}%)"
            val labelLayout = textMeasurer.measure(
                text = AnnotatedString(labelText),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    background = color.copy(alpha = 0.8f)
                )
            )
            drawText(
                labelLayout,
                topLeft = Offset(left, (top - labelLayout.size.height).coerceAtLeast(0f))
            )

            // 3. PPE Status Panel (Always on the right, compact and consistent)
            val panelPadding = 4.dp.toPx()
            val itemH = 14.dp.toPx()
            val panelW = 85.dp.toPx()
            
            // Fixed position: Always to the right of the bounding box
            val panelX = right + 2.dp.toPx()
            var currentY = top + panelPadding

            // Background for the status group
            val panelH = (worker.gearStatuses.size * itemH) + (panelPadding * 2)
            
            // Only draw if there's any chance it's visible on screen
            if (panelX < canvasW) {
                drawRect(
                    color = Color(0x99000000),
                    topLeft = Offset(panelX, top),
                    size = Size(panelW.coerceAtMost(canvasW - panelX), panelH)
                )

                worker.gearStatuses.forEach { gear ->
                    val gearColor = if (gear.status == "present") Color(0xFF32CD32) else Color(0xFFDC3232)
                    val icon = if (gear.status == "present") "✓" else "✕"
                    val gearText = "$icon ${gear.name}"
                    
                    val gearLayout = textMeasurer.measure(
                        text = AnnotatedString(gearText),
                        style = TextStyle(
                            color = gearColor,
                            fontSize = 9.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    )
                    
                    if (currentY + gearLayout.size.height < canvasH) {
                        drawText(
                            gearLayout,
                            topLeft = Offset(panelX + panelPadding, currentY)
                        )
                        currentY += itemH
                    }
                }
            }
        }
    }
}