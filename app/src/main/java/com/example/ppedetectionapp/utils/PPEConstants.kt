package com.example.ppedetectionapp.utils

object PPEConstants {
    val CLASS_CONF_THRESH = mapOf(
        6 to 0.25f,   // Person (Lowered for better long-distance detection)
        0 to 0.15f,   // helmet
        1 to 0.12f,   // gloves
        2 to 0.15f,   // vest
        3 to 0.12f,   // boots
        4 to 0.12f,   // goggles
        7 to 0.20f,   // no_helmet
        8 to 0.15f,   // no_goggle
        9 to 0.15f,   // no_gloves
        10 to 0.15f,  // no_boots
        5 to 0.30f    // none
    )

    const val DEFAULT_CONF = 0.15f
    const val NMS_IOU = 0.45f

    val PPE_PAIRS = mapOf(
        "Helmet" to Pair(0, 7),
        "Gloves" to Pair(1, 9),
        "Vest" to Pair(2, null),
        "Boots" to Pair(3, 10),
        "Goggles" to Pair(4, 8)
    )

    val SKIP_CLASSES = setOf(5, 6) // none, Person

    const val SMOOTH_WINDOW = 20 // Larger window for video stability
    const val SMOOTH_VOTE_THRESHOLD = 0.20f // Lower threshold to maintain detections at distance
}