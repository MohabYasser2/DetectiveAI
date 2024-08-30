package com.mohab.detectiveai.screens


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohab.detectiveai.GameViewModel
import com.mohab.detectiveai.R
import com.mohab.detectiveai.data.CharacterData
import com.mohab.detectiveai.partials.CustomHeader
import com.mohab.detectiveai.partials.GameButton
import com.mohab.detectiveai.routes.Route.CHAT
import com.mohab.detectiveai.routes.Route.END_GAME

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalFoundationApi::class, ExperimentalStdlibApi::class)
@Composable

fun CharacterSelectorScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column {
        var isLoading by remember { mutableStateOf(true) }
        var victim by remember { mutableStateOf("") }
        var showVictimAlert by remember { mutableStateOf(false) }
        val viewModel = GameViewModel(Application())
        val characters = viewModel.getCharacters().observeAsState(listOf()).value

        if (characters.isNotEmpty()) {
            isLoading = false
        }
        if (victim != "") {
            CustomHeader(title = "Select Character",
                navController = navController,
                hasInfo = true,
                onInfoClick = {
                    showVictimAlert = true
                })
        }
        if (showVictimAlert) {
            ShowVictimAlert(victim) {
                showVictimAlert = false
            }
        }

        var text by remember { mutableStateOf("") }
        val currentColor = MaterialTheme.colorScheme.primary
        val oldColor by remember { mutableStateOf(currentColor) }
        if (currentColor == oldColor) {
            text = "Loading Theme..."

        } else {
            text = "Loading Characters..."

        }

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(128.dp)
                        .padding(top = 32.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            }

        }



        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            val messages = viewModel.getMessages().observeAsState(listOf()).value
            var selectedCharacter: CharacterData by remember {
                mutableStateOf(
                    CharacterData(
                        name = "", personality = ""
                    )
                )
            }
            Log.d("TEST", "Characters: ${characters}")
            if (characters.isNotEmpty()) {
                isLoading = false
            }

            val killer = viewModel.getKiller()
                .observeAsState(CharacterData(name = "", personality = "")).value
            var isCorrect by remember { mutableStateOf(false) }
            val pagerState = rememberPagerState {
                characters.size
            }


            HorizontalPager(
                state = pagerState,
            ) {

                CharacterCard(
                    character = characters[it], modifier = Modifier.height(578.dp)
                )

                selectedCharacter = characters[pagerState.currentPage]

                //Log.d("TEST", "Selected Character: ${selectedCharacter.name}")
            }
            if (selectedCharacter.name != "") {

                for (character in characters) {
                    if (character.murderDetails?.victim != null) {
                        victim = character.murderDetails.victim
                    }
                }
                GameButton(
                    onClick = {
                        //Log.d("TEST", "Messages: ${messages}")
                        viewModel.startChat(messages, selectedCharacter.name)
                        navController.navigate(CHAT)

                    },
                    text = "Chat",
                )
                GameButton(onClick = {

                    isCorrect = selectedCharacter.name == killer.name
                    //Log.d("TEST", "Killer: ${killer.name}")
                    // Log.d("TEST", "Selected Character: ${selectedCharacter.name}")
                    //Log.d("TEST", "isCorrect: ${isCorrect}")
                    navController.navigate("$END_GAME/${isCorrect}")
                }, text = "Accuse")
            }

        }
    }


}


@Composable
fun CharacterCard(character: CharacterData, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_character_foreground),
                contentDescription = character.name,
                modifier = Modifier.size(256.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = character.name,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = character.personality,
                modifier = Modifier

                    .padding(horizontal = 32.dp)
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp
            )

        }
    }
}

@Composable
fun ShowVictimAlert(victimName: String, onDismiss: () -> Unit) {
    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(onDismissRequest = {
            showDialog = false
            onDismiss()
            // Call the provided onDismiss callback
        }, title = {
            Text(
                text = "$victimName was murdered !", fontWeight = FontWeight.Bold, fontSize = 24.sp
            )
        }, text = {
            Text(text = "And its your Job to find who did it ", fontSize = 16.sp)
        }, confirmButton = {

        }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(64.dp)
            )
        })
    }
}
