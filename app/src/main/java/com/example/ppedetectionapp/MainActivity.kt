package com.example.ppedetectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ppedetectionapp.detector.Detector
import com.example.ppedetectionapp.navigation.AppNavigation
import com.example.ppedetectionapp.ui.theme.PPEDetectionAppTheme
import com.example.ppedetectionapp.utils.ModelInfo

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load YOLO TFLite Model
        val detector = Detector(this)

        // Print Model Information
        ModelInfo.printModelInfo(detector.getInterpreter())

        enableEdgeToEdge()

        setContent {

            PPEDetectionAppTheme {

                AppNavigation()

            }

        }
    }
}