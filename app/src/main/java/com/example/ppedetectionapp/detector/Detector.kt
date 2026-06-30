package com.example.ppedetectionapp.detector

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class Detector(
    private val context: Context
) {

    private val interpreter: Interpreter

    init {

        val fileDescriptor =
            context.assets.openFd("best_float16.tflite")

        val inputStream =
            fileDescriptor.createInputStream()

        val fileChannel =
            inputStream.channel

        val model = fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )

        interpreter = Interpreter(model)

        // Print Model Information
        val inputTensor = interpreter.getInputTensor(0)
        val outputTensor = interpreter.getOutputTensor(0)

        Log.d(
            "YOLO",
            "=============================="
        )

        Log.d(
            "YOLO",
            "Input Shape : ${inputTensor.shape().contentToString()}"
        )

        Log.d(
            "YOLO",
            "Input Type  : ${inputTensor.dataType()}"
        )

        Log.d(
            "YOLO",
            "Output Shape : ${outputTensor.shape().contentToString()}"
        )

        Log.d(
            "YOLO",
            "Output Type  : ${outputTensor.dataType()}"
        )

        Log.d(
            "YOLO",
            "=============================="
        )
    }

    fun detect(input: ByteBuffer): DetectorResult {
        val output = Array(1) { Array(15) { FloatArray(8400) } }
        interpreter.run(input, output)
        return DetectorResult(output)
    }

    fun getInterpreter(): Interpreter {
        return interpreter
    }
}