package com.example.ppedetectionapp.model

data class DetectionResult(

    val left: Float,

    val top: Float,

    val width: Float,

    val height: Float,

    val helmet: Boolean,

    val vest: Boolean,

    val gloves: Boolean,

    val shoes: Boolean,

    val score: Int

)