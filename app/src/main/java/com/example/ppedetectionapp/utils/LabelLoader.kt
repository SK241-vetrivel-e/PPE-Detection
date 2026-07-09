package com.example.ppedetectionapp.utils

import android.content.Context

object LabelLoader {

    fun loadLabels(context: Context): List<String> {
        return try {
            val text = context.assets.open("labels.txt").bufferedReader().use { it.readText() }
            text.split(Regex("\\s+")).filter { it.isNotBlank() }
        } catch (e: Exception) {
            emptyList()
        }
    }

}