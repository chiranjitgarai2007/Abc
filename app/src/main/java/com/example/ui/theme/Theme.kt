package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val MathColorScheme = darkColorScheme(
    primary = MathPrimary,
    background = MathBackground,
    surface = MathSurface,
    onPrimary = MathBackground,
    onBackground = MathPrimary,
    onSurface = MathPrimary
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force dark mode
  dynamicColor: Boolean = false, // Disable dynamic colors for custom look
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = MathColorScheme, typography = Typography, content = content)
}
