package com.example.ppedetectionapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = SafetyYellow,
    onPrimary = DarkGrey,
    primaryContainer = SlateBlueDark,
    onPrimaryContainer = SafetyYellowLight,
    
    secondary = SlateBlueLight,
    onSecondary = Color.White,
    
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onBackground = Color.White,
    onSurface = Color.White,
    
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = SlateBlue,
    onPrimary = Color.White,
    primaryContainer = ProfessionalGrey,
    onPrimaryContainer = SlateBlueDark,
    
    secondary = SafetyYellow,
    onSecondary = DarkGrey,
    
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onBackground = DarkGrey,
    onSurface = DarkGrey,
    
    error = ErrorRed
)

@Composable
fun PPEDetectionAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
