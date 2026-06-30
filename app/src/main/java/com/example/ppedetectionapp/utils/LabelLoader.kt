package com.example.ppedetectionapp.utils

import android.content.Context

object LabelLoader {

    fun loadLabels(context: Context): List<String> {

        return context.assets
            .open("labels.txt")
            .bufferedReader()
            .readLines()

    }

}