package com.example.ppedetectionapp.utils

import android.graphics.Bitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TensorUtils {

    fun bitmapToTensor(
        bitmap: Bitmap
    ): ByteBuffer {

        val input =
            ByteBuffer.allocateDirect(
                1 * 640 * 640 * 3 * 4
            )

        input.order(
            ByteOrder.nativeOrder()
        )

        val pixels =
            IntArray(640 * 640)

        bitmap.getPixels(
            pixels,
            0,
            640,
            0,
            0,
            640,
            640
        )

        for (pixel in pixels) {

            val r =
                ((pixel shr 16) and 0xFF) / 255f

            val g =
                ((pixel shr 8) and 0xFF) / 255f

            val b =
                (pixel and 0xFF) / 255f

            input.putFloat(r)
            input.putFloat(g)
            input.putFloat(b)

        }

        return input

    }

}