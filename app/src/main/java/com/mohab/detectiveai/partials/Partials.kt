package com.mohab.detectiveai.partials

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mohab.detectiveai.GameViewModel
import com.mohab.detectiveai.R
import com.mohab.detectiveai.routes.Route.NEW_GAME
import java.lang.Long.parseLong
import androidx.compose.ui.platform.LocalContext as LocalContext1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeader(
    title: String,
    navController: NavController,
    hasInfo: Boolean = false,
    onInfoClick: () -> Unit = {},


) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.primary)

    ) {


        CenterAlignedTopAppBar(

            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight(450),
                    modifier = Modifier.padding(8.dp)

                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent
            ),
            actions = {
                if (hasInfo)
                IconButton(onClick = { onInfoClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_info),
                        contentDescription = "Back button",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },

        )
    }

}

fun generateCharacters(
    theme: String,
    difficulty: String,
): String {
    val prompt = """
        Generate a list of 5 characters for a murder mystery game set in a ${theme} with ${difficulty} difficulty level indicating all next replies by the characters.
        Make sure of the following: The victim is not among the characters , one of the suspects is the killer
        Only Reply with the list of characters.Include the following information about each character in a JSON format:

        * "name": Character's name (not the victim or the detective)
        * "personality": A brief description of their personality.
        * "killer":  True or False
        * "relationship": Their relationship to the other characters (e.g., "friend," "family member," "acquaintance"). 
        * "murderDetails": Details about the murder if the character is a killer. Include "method", "weapon", "location", "motive", "victim" : generated name of the victim  and "story" which is a condensed story of the murder.
        
        You can use the following as an example structure:
        [
            {
                "name": "Alice",
                "personality": "Quiet and observant, Alice is a gifted artist who prefers solitude. She's deeply introspective and often lost in her own world.",
                "killer": false,
                "relationship": "Friend of the group" 
            },
            ...
        ]
    """.trimIndent()
    return prompt

}

fun startChatPrompt(
    character: String
): String {
    val prompt = """
     
For the next messages, you are to act as **[$character]** and you should greet the detective.  

**Instructions:**

1. **Review the previous conversation:**  Carefully review the previous messages exchanged between the detective and the suspect. Pay attention to:
    * **Key topics discussed:** What were the main points of conversation? What questions were asked and answered?
    * **Character's behavior:** How did the character respond to previous questions? Were they evasive, helpful, or suspicious?
    * **Revealed information:** Did the character reveal any information about themselves, their whereabouts, or their relationship to the victim?

2. **Consider the suspect's personality:** Refer to the suspect's original personality description from the initial list. Use this to guide your response, keeping in mind:
    * **Their general demeanor:** Are they outgoing and friendly, or reserved and cautious?
    * **Their tendencies:** Are they prone to exaggeration, honesty, manipulation, or denial?
    * **Their motivations:** Do they have any reason to lie, protect someone, or conceal information?

3. **Maintain consistency:**  Ensure your response is consistent with the suspect's established personality and previous interactions. Avoid sudden shifts in behavior or contradictions in their statements.

4. **Provide a compelling response:**  Make the response engaging and contribute to the mystery's unfolding. Consider:
    * **Hint level:**  How much information should the suspect reveal based on their assigned "Hint Level" (High, Medium, Low)? 
    * **Suspicion:**  How should the suspect respond if the player is directly accusing them? 
    * **Clues and red herrings:** Can you subtly plant clues or introduce red herrings to keep the player guessing?

**Response:**
5. ** ONLY REPLY IN JSON FORMAT WITH THE FOLLOWING STRUCTURE:**
{
  "text": "Your response as the character here"
}
    """.trimIndent()
    return prompt
}

fun continueChatPrompt(
    string: String
): String {
    val prompt = """

**Instructions:**

1. **Review the previous conversation:**  Carefully review the previous messages exchanged between the detective and the suspect. Pay attention to:
    * **Key topics discussed:** What were the main points of conversation? What questions were asked and answered?
    * **Character's behavior:** How did the character respond to previous questions? Were they evasive, helpful, or suspicious?
    * **Revealed information:** Did the character reveal any information about themselves, their whereabouts, or their relationship to the victim?

2. **Consider the suspect's personality:** Refer to the suspect's original personality description from the initial list. Use this to guide your response, keeping in mind:
    * **Their general demeanor:** Are they outgoing and friendly, or reserved and cautious?
    * **Their tendencies:** Are they prone to exaggeration, honesty, manipulation, or denial?
    * **Their motivations:** Do they have any reason to lie, protect someone, or conceal information?

3. **Respond to the player's message:**  Use the player's message to continue the conversation.  Make sure your response:
    * **Is relevant to the topic:** Addresses the player's question or comment directly.
    * **Flows naturally:** Continues the conversation without abrupt changes in tone or subject.
    * **Maintains character voice:** Uses language and mannerisms consistent with the suspect's personality.
    * **No character name will be mentioned.

4. **Maintain consistency:**  Ensure your response is consistent with the suspect's established personality and previous interactions. Avoid sudden shifts in behavior or contradictions in their statements.

5. **Provide a compelling response:**  Make the response engaging and contribute to the mystery's unfolding. Consider:
    * **Hint level:**  How much information should the suspect reveal based on their assigned "Hint Level" (High, Medium, Low)? 
    * **Suspicion:**  How should the suspect respond if the player is directly accusing them? 
    * **Clues and red herrings:** Can you subtly plant clues or introduce red herrings to keep the player guessing?

**Player's Message:** [$string]

**Response:**
6. ** ONLY REPLY IN JSON FORMAT WITH THE FOLLOWING STRUCTURE:**
{
  "text": "Your response as the character here"
}
    
     
        
    """.trimIndent()
    return prompt
}

fun generateColorScheme(theme: String, isDarkMode: Boolean): String {
    val prompt = """
    Generate a colorful accurate color scheme for a theme Closely based on **$theme** in ,write a comment after each color explaining why you chose this, make sure the primary color is vibrant and colorful with an elegant and simple background.

    **Instructions:**
0. output will be in string format given to a JSON converter in my android app, don't add format to text
    1.  **Consider the theme:**  Think about the dominant colors, mood, and imagery associated with the theme. 
    
    2. **Create a color palette:** Generate a set of colors that reflect the theme's aesthetic. Include:
        * **Primary:** The main color for buttons, accents, and important elements.
        * **Secondary:** A supporting color for less prominent elements.
        * **Tertiary:**  An optional third color for subtle accents or background elements.
        * **Background:** The primary color for the background,make it colorful.
        * **OnPrimary/OnSecondary/OnTertiary:**  Colors for text and icons on top of the respective primary, secondary, and tertiary colors. 
        * **OnBackground:**  The color for text and icons on top of the background.
        
    3.Make sure colors compliment each other and are not too similar.
    4. this is for a murder mystery game, so make colours suitable (darker) for a murder mystery game.
  4. ** ONLY REPLY IN JSON WITH THE FOLLOWING STRUCTURE WITH FORMATS (0xFFxxxxxx), starts and ends with the curly brackets only:**

    {
      "primary": 0xFFxxxxxx, // Replace with the hex color code for primary in the format 0xFFxxxxxx
      "secondary": 0xFFxxxxxx, // Replace with the hex color code for secondary in the format 0xFFxxxxxx
      "tertiary": 0xFFxxxxxx, // Replace with the hex color code for tertiary in the format 0xFFxxxxxx
      "background": 0xFFxxxxxx, // Replace with the hex color code for background in the format 0xFFxxxxxx
      "onPrimary": 0xFFxxxxxx, // Replace with the hex color code for onPrimary in the format 0xFFxxxxxx
      "onSecondary": 0xFFxxxxxx, // Replace with the hex color code for onSecondary in the format 0xFFxxxxxx
      "onTertiary": 0xFFxxxxxx, // Replace with the hex color code for onTertiary in the format 0xFFxxxxxx
      "onBackground": 0xFFxxxxxx // Replace with the hex color code for onBackground in the format 0xFFxxxxxx
    }
   
    
    
   
    
    """.trimIndent()

    return prompt
}

@Composable
fun GameButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    primary: Color = MaterialTheme.colorScheme.primary,
    onPrimary: Color = MaterialTheme.colorScheme.onPrimary
) {
    Button(
        onClick = {
            onClick()
        },
        modifier = modifier
            .padding(horizontal = 32.dp)
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = primary,
            contentColor = onPrimary
        )
    ) {
        Text(text = text, modifier = modifier.padding(4.dp), fontSize = 20.sp, color = onPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultyDropDownTextField(difficulties: Array<String>, onValueChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            value = selectedText,
            onValueChange = { },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true,
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = RoundedCornerShape(32.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = MaterialTheme.colorScheme.background,
                focusedTextColor = MaterialTheme.colorScheme.background,
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            difficulties.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = {
                        selectedText = item
                        onValueChange(selectedText)
                        expanded = false

                    },

                )
            }
        }
    }
}

fun extractBracketedContent(text: String , isNested: Boolean = false): String {

    var startIndex = -1
    var endIndex = -1

    if(!isNested) {
       startIndex = text.indexOf('{')
        endIndex = text.lastIndexOf('}')

    }else{
        startIndex = text.indexOf('[')
       endIndex = text.lastIndexOf(']')

    }
    if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
        return text.substring(startIndex, endIndex + 1)
    }
    return ""

}

