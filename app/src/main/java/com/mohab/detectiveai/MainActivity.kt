package com.mohab.detectiveai

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.mohab.detectiveai.routes.AppNavHost
import com.mohab.detectiveai.ui.theme.AppTheme
import com.mohab.detectiveai.ui.theme.DarkColorScheme


class MainActivity : ComponentActivity() {
    private val viewModel = GameViewModel(Application())
    private var forcedTheme by mutableStateOf(DarkColorScheme)

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            AppTheme(
                forcedScheme = forcedTheme
            ) {
                val games = viewModel.getGames().observeAsState(listOf()).value
                if (games.isNotEmpty()) {
                    forcedTheme = darkColorScheme(
                        primary = Color(games[0].scheme.primary.hexToInt(HexFormat {
                            number.prefix = "0x"
                        })),
                        secondary = Color(games[0].scheme.secondary.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        tertiary = Color(games[0].scheme.tertiary.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        background = Color(games[0].scheme.background.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        onPrimary = Color(games[0].scheme.onPrimary.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        onSecondary = Color(games[0].scheme.onSecondary.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        onTertiary = Color(games[0].scheme.onTertiary.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                        onBackground = Color(games[0].scheme.onBackground.hexToInt(HexFormat {

                            number.prefix = "0x"
                        })),
                    )
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        AppNavHost()
                    }

                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewGameScreenOne() {

}


