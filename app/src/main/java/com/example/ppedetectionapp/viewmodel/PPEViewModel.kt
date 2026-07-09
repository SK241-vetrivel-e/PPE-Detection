package com.example.ppedetectionapp.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ppedetectionapp.detector.Detector
import com.example.ppedetectionapp.model.WorkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PPEViewModel : ViewModel() {

    // Detection Settings
    var currentDevice by mutableStateOf(Detector.Device.GPU)
        private set
    var confidenceThreshold by mutableStateOf(0.40f)
    var smoothingEnabled by mutableStateOf(true)
    var showBoundingBoxes by mutableStateOf(true)

    // Results
    var workerResults by mutableStateOf<List<WorkerResult>>(emptyList())
        private set

    var sourceImageWidth by mutableStateOf(1)
        private set
    var sourceImageHeight by mutableStateOf(1)
        private set

    var inferenceTime by mutableStateOf(0L)
        private set

    // Cached Detector
    var detector by mutableStateOf<Detector?>(null)
        private set

    fun initializeDetector(context: Context, device: Detector.Device) {
        if (detector != null && currentDevice == device) return

        viewModelScope.launch(Dispatchers.IO) {
            val oldDetector = detector
            val newDetector = Detector(context.applicationContext, device)
            
            launch(Dispatchers.Main) {
                detector = newDetector
                currentDevice = device
                oldDetector?.close()
            }
        }
    }

    fun updateResults(results: List<WorkerResult>, width: Int, height: Int, time: Long = 0) {
        workerResults = results
        sourceImageWidth = width
        sourceImageHeight = height
        inferenceTime = time
    }
    
    fun setDevice(device: Detector.Device, context: Context) {
        initializeDetector(context, device)
    }

    override fun onCleared() {
        super.onCleared()
        detector?.close()
    }
}
