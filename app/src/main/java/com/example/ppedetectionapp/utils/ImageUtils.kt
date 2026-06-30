package com.example.ppedetectionapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.example.ppedetectionapp.camera.YuvToRgbConverter

@OptIn(ExperimentalGetImage::class)
object ImageUtils {

    @androidx.camera.core.ExperimentalGetImage
    fun imageProxyToBitmap(
        context: Context,
        image: ImageProxy
    ): Bitmap? {

        val mediaImage = image.image ?: return null

        val bitmap = Bitmap.createBitmap(
            mediaImage.width,
            mediaImage.height,
            Bitmap.Config.ARGB_8888
        )

        val converter = YuvToRgbConverter(context)

        converter.yuvToRgb(
            mediaImage,
            bitmap
        )

        return rotateBitmap(
            bitmap,
            image.imageInfo.rotationDegrees.toFloat()
        )
    }

    fun letterbox(bitmap: Bitmap): Pair<Bitmap, LetterboxInfo> {
        val origW = bitmap.width
        val origH = bitmap.height
        val targetSize = 640f

        val scale = (targetSize / Math.max(origW, origH).toFloat())
        val newW = (origW * scale).toInt()
        val newH = (origH * scale).toInt()

        val resized = Bitmap.createScaledBitmap(bitmap, newW, newH, true)

        val canvas = Bitmap.createBitmap(640, 640, Bitmap.Config.ARGB_8888)
        val androidCanvas = android.graphics.Canvas(canvas)
        androidCanvas.drawColor(android.graphics.Color.rgb(114, 114, 114))

        val padX = (640 - newW) / 2f
        val padY = (640 - newH) / 2f

        androidCanvas.drawBitmap(resized, padX, padY, null)

        return Pair(canvas, LetterboxInfo(scale, padX, padY, origW, origH))
    }

    fun reverseLetterbox(
        cx: Float, cy: Float, w: Float, h: Float,
        info: LetterboxInfo
    ): FloatArray {
        // Step 1: from normalized (0-1) -> 640px space
        val pxCx = cx * 640f
        val pxCy = cy * 640f
        val pxW = w * 640f
        val pxH = h * 640f

        // Step 2: remove letterbox padding -> unscaled space
        var x1 = (pxCx - pxW / 2f - info.padX) / info.scale
        var y1 = (pxCy - pxH / 2f - info.padY) / info.scale
        var x2 = (pxCx + pxW / 2f - info.padX) / info.scale
        var y2 = (pxCy + pxH / 2f - info.padY) / info.scale

        // Step 3: clamp to frame bounds
        x1 = Math.max(0f, Math.min(info.originalWidth - 1f, x1))
        y1 = Math.max(0f, Math.min(info.originalHeight - 1f, y1))
        x2 = Math.max(0f, Math.min(info.originalWidth - 1f, x2))
        y2 = Math.max(0f, Math.min(info.originalHeight - 1f, y2))

        // Return normalized coordinates (0.0 to 1.0) relative to rotated source
        return floatArrayOf(
            x1 / info.originalWidth,
            y1 / info.originalHeight,
            x2 / info.originalWidth,
            y2 / info.originalHeight
        )
    }

    fun rotateBitmap(
        bitmap: Bitmap,
        rotation: Float
    ): Bitmap {
        if (rotation == 0f) return bitmap
        val matrix = Matrix()
        matrix.postRotate(rotation)
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}