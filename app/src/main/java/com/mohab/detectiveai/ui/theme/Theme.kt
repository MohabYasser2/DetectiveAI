package com.mohab.detectiveai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.mohab.detectiveai.data.ColourScheme
import com.mohab.detectiveai.screens.HomeScreen
import androidx.compose.runtime.SideEffect as SideEffect1

 val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF662020),
    secondary = Color(0xFFCE4040),
    tertiary = Color(0xFF461616),
    onBackground = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    background = Color(0xFF381A1A),


)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF662020),
    secondary = Color(0xFFCE4040),
    tertiary = Color(0xFF461616),
    onBackground = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    background = Color(0xFF381A1A),

)
/* Other default colors to override
background = Color(0xFFFFFBFE),
surface = Color(0xFFFFFBFE),
onPrimary = Color.White,
onSecondary = Color.White,
onTertiary = Color.White,
onBackground = Color(0xFF1C1B1F),
onSurface = Color(0xFF1C1B1F),
*/

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    forcedScheme: ColorScheme = DarkColorScheme,
    content: @Composable () -> Unit,
) {
    var colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    if (forcedScheme != DarkColorScheme) {
        colorScheme = forcedScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect1 {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

@Preview (showBackground = true)
@Composable
private fun Preview() {

    AppTheme(
       content = { HomeScreen(navController = NavController(LocalContext.current)) }
    )
    
}