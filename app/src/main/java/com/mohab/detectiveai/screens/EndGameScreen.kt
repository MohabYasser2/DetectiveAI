package com.mohab.detectiveai.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.mohab.detectiveai.R
import com.mohab.detectiveai.partials.GameButton
import com.mohab.detectiveai.routes.Route.HOME

@Composable
fun EndGameScreen(
    navController: NavController, modifier: Modifier = Modifier, isCorrect: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = if (isCorrect) painterResource(id = R.drawable.ic_check)
            else painterResource(
                id = R.drawable.ic_failed
            ), contentDescription = "Result", modifier = modifier
                .size(248.dp)
                .padding(32.dp)

        )

        Text(
            text = if (isCorrect) "Congratulations !" else "Failed !",
            modifier = modifier.padding(16.dp),
            fontSize = 32.sp,
            color = if (isCorrect) Color.Green.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isCorrect) "You caught the killer.. or did you"
            else "the real killer is still free... not very المفتش كروبو of you",
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(bottom = 32.dp),
            fontSize = 16.sp,
            color = if (isCorrect) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
        )
        if (!isCorrect) GameButton(onClick = { navController.popBackStack() }, text = "Guess Again")
        GameButton(onClick = {
            navController.navigate(HOME) {
                popUpTo(HOME) {
                    inclusive = true
                }
            }
        }, text = "Main Menu")


    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {

    EndGameScreen(navController = NavHostController(LocalContext.current))
}
