package com.example.ppedetectionapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ppedetectionapp.model.WorkerResult

class PPEViewModel : ViewModel() {

    var workerResults by mutableStateOf<List<WorkerResult>>(emptyList())
        private set

    var sourceImageWidth by mutableStateOf(1)
        private set
    var sourceImageHeight by mutableStateOf(1)
        private set

    fun updateResults(results: List<WorkerResult>, width: Int, height: Int) {
        workerResults = results
        sourceImageWidth = width
        sourceImageHeight = height
    }

}