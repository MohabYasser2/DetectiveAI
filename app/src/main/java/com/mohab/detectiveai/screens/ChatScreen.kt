package com.mohab.detectiveai.screens

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mohab.detectiveai.GameViewModel
import com.mohab.detectiveai.R
import com.mohab.detectiveai.data.VisibleMessageModel

@Composable
fun ChatScreen(navController: NavController, modifier: Modifier = Modifier) {

    val viewModel = GameViewModel(Application())
    val visibleMessages by viewModel.getVisibleMessages()
        .observeAsState(listOf(VisibleMessageModel(role = "model", message = "Typing...")))
    val messages by viewModel.getMessages().observeAsState(listOf())
    var text by remember { mutableStateOf("") }


    Column(
        modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {


        LazyColumn(
            modifier = modifier
                .weight(1f)
                .padding(top = 16.dp), reverseLayout = true
        ) {


            items(visibleMessages.reversed()) { message ->
                MessageCard(message = message)
            }

        }
        OutlinedTextField(value = text,
            onValueChange = { text = it },
            modifier = modifier
                .padding(16.dp)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = {
                viewModel.continueChat(messages, text)
                text = ""
            }),
            singleLine = true,
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
            ),
            placeholder = {
                Text(text = "Type your message here...")
            },
            trailingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "Send",
                    modifier = modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.continueChat(messages, text)
                            text = ""


                        })
            })
    }

}

@Composable
fun MessageCard(message: VisibleMessageModel, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (message.role == "user") Arrangement.End else Arrangement.Start
    ) {
        if (message.role != "user") {
            Icon(
                painter = painterResource(id = R.drawable.ic_character_foreground),
                contentDescription = "Character",
                modifier = modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }

        Card(
            modifier = modifier
                .padding(8.dp)
                .weight(1f),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                text = message.message,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = modifier.padding(16.dp)
            )
        }


        if (message.role == "user") {
            Icon(
                painter = painterResource(id = R.drawable.ic_character_foreground),
                contentDescription = "Character",
                modifier = modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}