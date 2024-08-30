package com.mohab.detectiveai.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohab.detectiveai.GameViewModel
import com.mohab.detectiveai.partials.CustomHeader
import com.mohab.detectiveai.partials.DifficultyDropDownTextField
import com.mohab.detectiveai.partials.GameButton
import com.mohab.detectiveai.routes.Route.CHARACTER_SELECTOR
import com.mohab.detectiveai.ui.theme.AppTheme

@Composable
fun NewGameScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column {

        CustomHeader(title = "New Game", navController = navController)

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            val viewModel = GameViewModel(Application())
            var theme by remember { mutableStateOf("") }
            var difficulty by remember { mutableStateOf("") }
            var isOn by remember { mutableStateOf(false) }
            val context = LocalContext.current
            viewModel.clear()
            Text(
                text = "Desired Theme:",
                modifier = modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                fontSize = 20.sp
            )
            OutlinedTextField(
                value = theme,
                onValueChange = { theme = it },
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black,
                    focusedTextColor = Color.Black,
                )
            )

            Text(
                text = "Desired Difficulty",
                modifier = modifier
                    .padding(16.dp)
                    .align(Alignment.Start),
                fontSize = 20.sp
            )
            val difficulties = arrayOf("Easy", "Medium", "Hard")
            DifficultyDropDownTextField(difficulties = difficulties,
                onValueChange = { difficulty = it })
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {


                Text(
                    text = "Game Instructions", modifier = modifier.padding(16.dp), fontSize = 24.sp
                )
                Text(
                    text = "1. Choose a theme and difficulty.",
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
                Text(
                    text = "2. Start The Game.",
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
                Text(
                    text = "3. Select Character to chat with.",
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
                Text(
                    text = "4. Start questioning.",
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
                Text(
                    text = "5. Accuse a character.",
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                )
            }

            GameButton(
                onClick = {
                    if (theme.isEmpty() || difficulty.isEmpty()) {
                        Toast.makeText(
                            context, "Please enter a theme and difficulty", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.clear()
                        viewModel.changeTheme(difficulty, theme)
                        viewModel.startGame(difficulty, theme)
                        navController.navigate(CHARACTER_SELECTOR)
                    }
                }, text = "Start Game"
            )


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        NewGameScreen(navController = NavController(Application()))
    }

}
