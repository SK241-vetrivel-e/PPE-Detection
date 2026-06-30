package com.example.ppedetectionapp.utils

import android.util.Log
import org.tensorflow.lite.Interpreter

object ModelInfo {

    fun printModelInfo(interpreter: Interpreter) {

        val inputTensor = interpreter.getInputTensor(0)
        val outputTensor = interpreter.getOutputTensor(0)

        Log.d("YOLO", "========== MODEL INFO ==========")
        Log.d("YOLO", "Input Shape : ${inputTensor.shape().contentToString()}")
        Log.d("YOLO", "Input Type  : ${inputTensor.dataType()}")

        Log.d("YOLO", "Output Shape : ${outputTensor.shape().contentToString()}")
        Log.d("YOLO", "Output Type  : ${outputTensor.dataType()}")

        Log.d("YOLO", "================================")
    }
}