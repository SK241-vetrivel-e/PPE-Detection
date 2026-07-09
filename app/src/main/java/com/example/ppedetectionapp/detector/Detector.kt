package com.example.ppedetectionapp.detector

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.gpu.GpuDelegate
import org.tensorflow.lite.nnapi.NnApiDelegate
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Detector(
    context: Context,
    private val device: Device = Device.GPU
) {
    private var interpreter: Interpreter? = null
    private var gpuDelegate: GpuDelegate? = null
    private var nnApiDelegate: NnApiDelegate? = null

    enum class Device {
        CPU, GPU, NNAPI
    }

    companion object {
        private var modelBuffer: MappedByteBuffer? = null

        fun getModelBuffer(context: Context): MappedByteBuffer {
            if (modelBuffer == null) {
                val fileDescriptor = context.assets.openFd("best_float16.tflite")
                val inputStream = fileDescriptor.createInputStream()
                val fileChannel = inputStream.channel
                modelBuffer = fileChannel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fileDescriptor.startOffset,
                    fileDescriptor.declaredLength
                )
            }
            return modelBuffer!!
        }
    }

    init {
        try {
            val buffer = getModelBuffer(context)
            val options = Interpreter.Options().apply {
                when (device) {
                    Device.GPU -> {
                        try {
                            gpuDelegate = GpuDelegate()
                            addDelegate(gpuDelegate)
                        } catch (e: Exception) {
                            Log.e("YOLO", "GPU Delegate failed, falling back to CPU", e)
                            setNumThreads(4)
                        }
                    }
                    Device.NNAPI -> {
                        try {
                            nnApiDelegate = NnApiDelegate()
                            addDelegate(nnApiDelegate)
                        } catch (e: Exception) {
                            Log.e("YOLO", "NNAPI Delegate failed, falling back to CPU", e)
                            setNumThreads(4)
                        }
                    }
                    Device.CPU -> {
                        setNumThreads(4)
                    }
                }
            }
            interpreter = Interpreter(buffer, options)
        } catch (e: Exception) {
            Log.e("YOLO", "Failed to initialize detector", e)
        }
    }

    fun detect(input: ByteBuffer): DetectorResult? {
        val currentInterpreter = interpreter ?: return null
        val output = Array(1) { Array(15) { FloatArray(8400) } }
        currentInterpreter.run(input, output)
        return DetectorResult(output)
    }

    fun getInterpreter(): Interpreter? {
        return interpreter
    }

    fun close() {
        interpreter?.close()
        gpuDelegate?.close()
        nnApiDelegate?.close()
        interpreter = null
    }
}
