package com.example.ppedetectionapp.model

import com.example.ppedetectionapp.detector.BoundingBox

object DummyData {

    val workers = listOf(

        PersonStatus(
            person = BoundingBox(
                x1 = 0f,
                y1 = 0f,
                x2 = 0f,
                y2 = 0f,
                confidence = 1f,
                classId = 6,
                className = "Person"
            )
        ).apply {

            helmet = true
            vest = true
            gloves = false
            boots = true
            goggles = false

        },

        PersonStatus(
            person = BoundingBox(
                x1 = 0f,
                y1 = 0f,
                x2 = 0f,
                y2 = 0f,
                confidence = 1f,
                classId = 6,
                className = "Person"
            )
        ).apply {

            helmet = true
            vest = true
            gloves = true
            boots = true
            goggles = true

        }

    )

}