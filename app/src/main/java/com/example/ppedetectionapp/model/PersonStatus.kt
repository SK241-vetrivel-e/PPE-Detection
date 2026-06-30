package com.example.ppedetectionapp.model

import com.example.ppedetectionapp.detector.BoundingBox

data class PersonStatus(

    val person: BoundingBox,

    var helmet: Boolean = false,

    var vest: Boolean = false,

    var gloves: Boolean = false,

    var boots: Boolean = false,

    var goggles: Boolean = false

)