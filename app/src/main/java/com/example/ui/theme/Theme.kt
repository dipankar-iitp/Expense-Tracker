package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
  primary = BentoBluePrimary,
  onPrimary = BentoSurface,
  primaryContainer = BentoBlueContainer,
  onPrimaryContainer = BentoOnBlueContainer,
  secondary = BentoGreenPrimary,
  onSecondary = BentoSurface,
  secondaryContainer = BentoGreenContainer,
  onSecondaryContainer = BentoOnGreenContainer,
  tertiary = BentoDarkBlue,
  error = BentoRedAlert,
  errorContainer = BentoRedAlertContainer,
  onErrorContainer = BentoRedAlert,
  background = BentoBackground,
  onBackground = BentoTextPrimary,
  surface = BentoSurface,
  onSurface = BentoTextPrimary,
  surfaceVariant = BentoCardSecondary,
  onSurfaceVariant = BentoTextSecondary,
  outline = BentoBorder
)

private val DarkColorScheme = darkColorScheme(
  primary = BentoBlueContainer,
  onPrimary = BentoDarkBlue,
  primaryContainer = BentoBluePrimary,
  onPrimaryContainer = BentoSurface,
  secondary = BentoGreenContainer,
  onSecondary = BentoGreenPrimary,
  secondaryContainer = BentoDarkGreenCard,
  onSecondaryContainer = BentoSurface,
  background = BentoDarkBlue,
  onBackground = BentoBackground,
  surface = BentoDarkBlue,
  onSurface = BentoBackground,
  surfaceVariant = BentoTextSecondary,
  onSurfaceVariant = BentoCardSecondary,
  outline = BentoBorder
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Force light Bento Grid theme by default as per design specs
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
