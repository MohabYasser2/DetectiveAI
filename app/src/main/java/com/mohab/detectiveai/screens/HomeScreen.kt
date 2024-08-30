package com.mohab.detectiveai.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mohab.detectiveai.GameViewModel
import com.mohab.detectiveai.R
import com.mohab.detectiveai.partials.GameButton
import com.mohab.detectiveai.routes.Route.CHARACTER_SELECTOR
import com.mohab.detectiveai.routes.Route.NEW_GAME
import com.mohab.detectiveai.ui.theme.AppTheme

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val viewModel = GameViewModel(Application())
        val games = viewModel.getGames().observeAsState(emptyList()).value
        val context = LocalContext.current

        Box(
            modifier = modifier
                .padding(top = 128.dp)
                .size(256.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
        )

        {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Game Logo",
                modifier = modifier.padding(16.dp)
            )
        }
        Text(
            text = "Welcome to Detective AI!",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = modifier.padding(16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        GameButton(onClick = {
            navController.navigate(NEW_GAME)
        }, text = "New Game")
        GameButton(onClick = {
            if (games.isNotEmpty()) navController.navigate(CHARACTER_SELECTOR)
            else Toast.makeText(context, "No games found", Toast.LENGTH_SHORT).show()
        }, text = "Continue Game")


    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    AppTheme {
        HomeScreen(navController = rememberNavController())
    }

}