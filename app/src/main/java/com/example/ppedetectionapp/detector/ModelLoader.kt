package com.example.ppedetectionapp.detector

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class ModelLoader(private val context: Context) {

    lateinit var interpreter: Interpreter

    fun loadModel(): Interpreter {

        val fileDescriptor =
            context.assets.openFd("best_float16.tflite")

        val inputStream =
            fileDescriptor.createInputStream()

        val fileChannel =
            inputStream.channel

        val modelBuffer: MappedByteBuffer =
            fileChannel.map(
                FileChannel.MapMode.READ_ONLY,
                fileDescriptor.startOffset,
                fileDescriptor.declaredLength
            )

        interpreter = Interpreter(modelBuffer)

        return interpreter

    }

}