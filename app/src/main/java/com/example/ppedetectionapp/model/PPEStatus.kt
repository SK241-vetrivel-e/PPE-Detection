package com.example.ppedetectionapp.model

data class PPEStatus(

    val personDetected: Boolean = false,

    val helmet: Boolean = false,

    val vest: Boolean = false,

    val gloves: Boolean = false,

    val shoes: Boolean = false,

    val score: Int = 0

)