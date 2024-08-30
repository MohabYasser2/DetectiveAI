package com.mohab.detectiveai

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.mohab.detectiveai.data.CharacterData
import com.mohab.detectiveai.data.ColourScheme
import com.mohab.detectiveai.data.GameData
import com.mohab.detectiveai.data.MessageModel
import com.mohab.detectiveai.data.Response
import com.mohab.detectiveai.data.VisibleMessageModel
import com.mohab.detectiveai.partials.continueChatPrompt
import com.mohab.detectiveai.partials.extractBracketedContent
import com.mohab.detectiveai.partials.generateCharacters
import com.mohab.detectiveai.partials.generateColorScheme
import com.mohab.detectiveai.partials.startChatPrompt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(app: Application) : AndroidViewModel(app) {

    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash", apiKey ="API_KEY", safetySettings = listOf(
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.NONE),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.NONE),
        )
    )

    val characterDao = MainApllication.gameDatabase.characterDao()
    val messageDao = MainApllication.gameDatabase.messageDao()
    val visibleMesageDao = MainApllication.gameDatabase.visibleMessageDao()
    val gameDao = MainApllication.gameDatabase.gameDao()

    fun getCharacters() = characterDao.getCharacters()
    fun getMessages() = messageDao.getMessages()
    fun getVisibleMessages() = visibleMesageDao.getMessages()
    fun getKiller() = characterDao.getKiller()
    fun getGames() = gameDao.getGames()


    fun clear() {
        viewModelScope.launch(Dispatchers.IO) {
            characterDao.deleteAllCharacters()
            gameDao.deleteAllGames()
        }
    }


    fun startGame(difficulty: String, theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val chat = generativeModel.startChat(
            )
            val prompt = generateCharacters(theme, difficulty)
            messageDao.deleteAllMessages()
            messageDao.insertMessage(MessageModel(role = "user", message = prompt))
            var response: GenerateContentResponse? = null
            var charactersList: List<CharacterData> = listOf()
            var victim = ""

            while (true) {
                try {
                    response = chat.sendMessage(prompt)
                    Log.d("VIEWMODEL", "Response: ${response.text}")
                    charactersList = Gson().fromJson(
                        extractBracketedContent(response.text ?: "", true),
                        Array<CharacterData>::class.java
                    ).toList()
                    Log.d("VIEWMODEL", "Response: ${charactersList}")
                    for (character in charactersList) {
                        if (character.murderDetails?.victim != null) {
                            victim = character.murderDetails.victim
                            Log.d("VIEWMODEL", "Victim: ${victim}")
                        }
                    }
                    if (victim in charactersList.map { it.name }) {
                    } else {
                        break
                    }
                } catch (e: Exception) {
                    Log.d("VIEWMODEL", "Error: ${e.message}")
                }
            }



            for (character in charactersList) {
                characterDao.insertCharacter(
                    CharacterData(
                        name = character.name,
                        personality = character.personality,
                        killer = character.killer,
                        relationship = character.relationship,
                        murderDetails = character.murderDetails
                    )
                )
            }
            messageDao.insertMessage(
                MessageModel(
                    role = "model", message = response?.text.toString()
                )
            )
            visibleMesageDao.deleteAllMessages()

            Log.d("VIEWMODEL", "Response: ${charactersList}")
            Log.d("VIEWMODEL", "Response: ${getCharacters().value}")

        }

    }

    fun changeTheme(difficulty: String, theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var scheme: GenerateContentResponse? = null
            var colorScheme: ColourScheme
            val history =
                listOf(MessageModel(role = "user", message = generateCharacters(theme, difficulty)))
            val chat = generativeModel.startChat(
                history = history.map {
                    content(it.role) { text(it.message) }
                }.toList()

            )
            while (true) {
                try {


                    scheme = chat.sendMessage(generateColorScheme(theme, true))
                    Log.d("VIEWMODEL", "Scheme json: ${scheme.text}")
                    Log.d(
                        "VIEWMODEL",
                        "Inside Bracket: ${extractBracketedContent(scheme.text ?: "")}"
                    )
                    colorScheme = Gson().fromJson(
                        extractBracketedContent(scheme.text ?: ""),
                        ColourScheme::class.java
                    )
                    Log.d("VIEWMODEL", "Scheme gson: ${colorScheme}")
                    gameDao.insertGame(
                        GameData(
                            theme = theme, difficulty = difficulty, scheme = colorScheme
                        )
                    )
                    Log.d("VIEWMODEL", "Games: ${getGames().value}")
                    break

                } catch (e: Exception) {
                    Log.d("VIEWMODEL", "Error: ${e.message}")
                }
            }
        }
    }

    fun startChat(history: List<MessageModel>, character: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val chat = generativeModel.startChat(
                history = history.map {
                    content(it.role) { text(it.message) }
                }.toList()

            )
            var responseText = ""
            while (responseText == "") {
                try {
                    val response = chat.sendMessage(startChatPrompt(character))
                    Log.d("VIEWMODEL", "Start Chat Response: ${response.text}")

                    responseText = Gson().fromJson(
                        extractBracketedContent(response.text ?: ""),
                        Response::class.java
                    ).text
                } catch (e: Exception) {
                    Log.d("VIEWMODEL", "Error: ${e.message}")
                }
            }
            messageDao.insertMessage(
                MessageModel(
                    role = "model", message = responseText
                )
            )
            visibleMesageDao.deleteAllMessages()
            visibleMesageDao.insertMessage(
                VisibleMessageModel(
                    role = "model", message = responseText
                )
            )
        }


    }

    fun continueChat(history: List<MessageModel>, prompt: String) {
        viewModelScope.launch(Dispatchers.IO) {
            visibleMesageDao.insertMessage(
                VisibleMessageModel(
                    role = "user", message = prompt
                )
            )
            val chat = generativeModel.startChat(
                history = history.map {
                    content(it.role) { text(it.message) }
                }.toList()

            )

            val response = chat.sendMessage(continueChatPrompt(prompt))
            Log.d("VIEWMODEL", "Continue Chat Response: ${response.text}")
            val responseText = Gson().fromJson(
                extractBracketedContent(response.text ?: ""),
                Response::class.java
            ).text
            messageDao.insertMessage(
                MessageModel(
                    role = "model", message = responseText
                )
            )
            visibleMesageDao.insertMessage(
                VisibleMessageModel(
                    role = "model", message = responseText
                )
            )

        }

    }


}



