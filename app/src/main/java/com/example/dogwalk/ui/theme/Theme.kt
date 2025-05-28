package com.example.dogwalk.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.dogwalk.data.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)
val GreenColorScheme = lightColorScheme(
    primary = Color(0xFF388E3C),
    secondary = Color(0xFF81C784),
    background = Color(0xFFE8F5E9),
    surface = Color(0xFFC8E6C9),
    onPrimary = Color.White
)

val PurpleColorScheme = lightColorScheme(
    primary = Color(0xFF7E57C2),
    secondary = Color(0xFFB39DDB),
    background = Color(0xFFF3E5F5),
    surface = Color(0xFFE1BEE7),
    onPrimary = Color.White
)


@Composable
fun DogWalkTheme(
    selectedTheme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (selectedTheme) {
        AppTheme.DARK -> DarkColorScheme
        AppTheme.GREEN -> GreenColorScheme
        AppTheme.PURPLE -> PurpleColorScheme
        AppTheme.LIGHT -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
