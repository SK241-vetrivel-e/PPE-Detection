package com.example.ppedetectionapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.ppedetectionapp.model.PPEStatus

class PPEViewModel : ViewModel() {

    var ppeStatus by mutableStateOf(
        PPEStatus()
    )
        private set

    fun updateStatus(
        status: PPEStatus
    ) {
        ppeStatus = status
    }

}