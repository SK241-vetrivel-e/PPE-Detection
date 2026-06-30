package com.example.ppedetectionapp.model

import com.example.ppedetectionapp.detector.BoundingBox

data class GearStatus(
    val name: String,
    val status: String, // "present" | "absent"
    val confidence: Float = 0.0f
)

data class WorkerResult(
    val personBox: BoundingBox,
    val gearStatuses: List<GearStatus> = emptyList()
) {
    val compliant: Boolean
        get() = gearStatuses.all { it.status == "present" }

    val complianceScore: Float
        get() {
            if (gearStatuses.isEmpty()) return 0.0f
            val presentCount = gearStatuses.count { it.status == "present" }
            return presentCount.toFloat() / gearStatuses.size
        }

    val missing: List<String>
        get() = gearStatuses.filter { it.status == "absent" }.map { it.name }
}