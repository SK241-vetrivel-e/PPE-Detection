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

    fun rotateAndLetterbox(
        bitmap: Bitmap,
        rotation: Float,
        targetSize: Int = 640
    ): Pair<Bitmap, LetterboxInfo> {
        val srcW = bitmap.width
        val srcH = bitmap.height
        
        // Calculate dimensions after rotation
        val rotatedW = if (rotation % 180 != 0f) srcH else srcW
        val rotatedH = if (rotation % 180 != 0f) srcW else srcH
        
        val scale = targetSize.toFloat() / Math.max(rotatedW, rotatedH)
        val finalW = (rotatedW * scale).toInt()
        val finalH = (rotatedH * scale).toInt()
        
        val padX = (targetSize - finalW) / 2f
        val padY = (targetSize - finalH) / 2f
        
        val output = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(output)
        canvas.drawColor(android.graphics.Color.rgb(114, 114, 114))
        
        val matrix = Matrix()
        // 1. Center the source image
        matrix.postTranslate(-srcW / 2f, -srcH / 2f)
        // 2. Rotate
        matrix.postRotate(rotation)
        // 3. Scale
        matrix.postScale(scale, scale)
        // 4. Move to padded position in target canvas
        matrix.postTranslate(targetSize / 2f, targetSize / 2f)
        
        // Use Paint with filterBitmap = true for sharpest possible downscaling
        val paint = android.graphics.Paint(android.graphics.Paint.FILTER_BITMAP_FLAG)
        canvas.drawBitmap(bitmap, matrix, paint)
        
        return Pair(output, LetterboxInfo(scale, padX, padY, rotatedW, rotatedH))
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
