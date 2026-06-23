package com.example.ppedetectionapp.camera

import android.content.Context
import androidx.camera.view.PreviewView

object CameraPreview {

    fun create(context: Context): PreviewView {

        return PreviewView(context).apply {

            implementationMode = PreviewView.ImplementationMode.COMPATIBLE

            scaleType = PreviewView.ScaleType.FILL_CENTER

        }

    }

}