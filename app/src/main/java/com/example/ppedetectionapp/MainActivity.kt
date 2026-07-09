package com.example.ppedetectionapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ppedetectionapp.navigation.AppNavigation
import com.example.ppedetectionapp.ui.theme.PPEDetectionAppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PPEDetectionAppTheme {
                AppNavigation()
            }
        }
    }
}