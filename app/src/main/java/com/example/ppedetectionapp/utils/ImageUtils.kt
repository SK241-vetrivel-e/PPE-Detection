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

    fun resize(bitmap: Bitmap): Bitmap {

        return Bitmap.createScaledBitmap(
            bitmap,
            640,
            640,
            true
        )
    }

    private fun rotateBitmap(
        bitmap: Bitmap,
        rotation: Float
    ): Bitmap {

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