package com.mysticbyte.counter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mysticbyte.counter.data.model.AppTheme

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BlueContainerLight,
    onPrimaryContainer = BlueDark,
    secondary = RedSecondary,
    onSecondary = Color.White,
    secondaryContainer = RedContainerLight,
    onSecondaryContainer = RedDark,
    background = Color(0xFFFDFBFF),
    onBackground = Color(0xFF1C1B1F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurfaceVariant = Color(0xFF44474F),
    outline = Color(0xFF74777F),
    outlineVariant = Color(0xFFC4C6D0),
    error = RedSecondary,
    onError = Color.White,
    errorContainer = RedContainerLight,
    onErrorContainer = RedDark
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = BlueDark,
    primaryContainer = BlueContainerDark,
    onPrimaryContainer = BlueLight,
    secondary = RedLight,
    onSecondary = RedDark,
    secondaryContainer = RedContainerDark,
    onSecondaryContainer = RedLight,
    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE3E2E6),
    surface = Color(0xFF2B2930),
    onSurface = Color(0xFFE3E2E6),
    surfaceVariant = Color(0xFF44474F),
    onSurfaceVariant = Color(0xFFC4C6CF),
    outline = Color(0xFF8E9099),
    outlineVariant = Color(0xFF44474F),
    error = RedLight,
    onError = RedDark,
    errorContainer = RedContainerDark,
    onErrorContainer = RedLight
)

private val CompanyLightColorScheme = lightColorScheme(
    primary = CompanyBluePrimary,
    onPrimary = Color.White,
    primaryContainer = CompanyBlueContainerLight,
    onPrimaryContainer = CompanyBlueDark,
    secondary = RedSecondary,
    onSecondary = Color.White,
    secondaryContainer = RedContainerLight,
    onSecondaryContainer = RedDark,
    background = CompanyBackgroundLight,
    onBackground = Color(0xFF0A1929),
    surface = CompanySurfaceLight,
    onSurface = Color(0xFF0A1929),
    surfaceVariant = Color(0xFFBBDEFB),
    onSurfaceVariant = Color(0xFF2C4A6E),
    outline = Color(0xFF5E92F3),
    outlineVariant = Color(0xFF90CAF9),
    error = RedSecondary,
    onError = Color.White,
    errorContainer = RedContainerLight,
    onErrorContainer = RedDark
)

private val CompanyDarkColorScheme = darkColorScheme(
    primary = CompanyBlueLight,
    onPrimary = CompanyBlueDark,
    primaryContainer = CompanyBlueContainerDark,
    onPrimaryContainer = CompanyBlueLight,
    secondary = RedLight,
    onSecondary = RedDark,
    secondaryContainer = RedContainerDark,
    onSecondaryContainer = RedLight,
    background = CompanyBackgroundDark,
    onBackground = Color(0xFFE3F2FD),
    surface = CompanySurfaceDark,
    onSurface = Color(0xFFE3F2FD),
    surfaceVariant = Color(0xFF1B3556),
    onSurfaceVariant = Color(0xFF90CAF9),
    outline = Color(0xFF5E92F3),
    outlineVariant = Color(0xFF2C4A6E),
    error = RedLight,
    onError = RedDark,
    errorContainer = RedContainerDark,
    onErrorContainer = RedLight
)

private val GameColorScheme = darkColorScheme(
    primary = GamePrimary,
    onPrimary = GameOnPrimary,
    primaryContainer = GamePrimary.copy(alpha = 0.15f),
    onPrimaryContainer = GamePrimary,
    secondary = GameSecondary,
    onSecondary = GameOnSecondary,
    secondaryContainer = GameSecondary.copy(alpha = 0.15f),
    onSecondaryContainer = GameSecondary,
    background = GameBackground,
    onBackground = GameBackgroundText,
    surface = GameSurface,
    onSurface = GameBackgroundText,
    surfaceVariant = GameSurfaceElevated,
    onSurfaceVariant = GameSurfaceText,
    outline = GameOutlineColor,
    outlineVariant = GameOutlineVariant,
    error = GameError,
    onError = GameOnError,
    errorContainer = GameErrorContainer.copy(alpha = 0.2f),
    onErrorContainer = GameError
)

@Composable
fun CounterTheme(
    appTheme: AppTheme = AppTheme.LIGHT,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (appTheme) {
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.COMPANY -> if (darkTheme) CompanyDarkColorScheme else CompanyLightColorScheme
        AppTheme.GAME -> GameColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
