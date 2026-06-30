package com.example.ppedetectionapp.utils

object PPEConstants {
    val CLASS_CONF_THRESH = mapOf(
        6 to 0.40f,   // Person
        0 to 0.20f,   // helmet
        1 to 0.15f,   // gloves
        2 to 0.20f,   // vest
        3 to 0.15f,   // boots
        4 to 0.15f,   // goggles
        7 to 0.20f,   // no_helmet
        8 to 0.15f,   // no_goggle
        9 to 0.15f,   // no_gloves
        10 to 0.15f,  // no_boots
        5 to 0.30f    // none
    )

    const val DEFAULT_CONF = 0.20f
    const val NMS_IOU = 0.45f

    val PPE_PAIRS = mapOf(
        "Helmet" to Pair(0, 7),
        "Gloves" to Pair(1, 9),
        "Vest" to Pair(2, null),
        "Boots" to Pair(3, 10),
        "Goggles" to Pair(4, 8)
    )

    val SKIP_CLASSES = setOf(5, 6) // none, Person

    const val SMOOTH_WINDOW = 12
}