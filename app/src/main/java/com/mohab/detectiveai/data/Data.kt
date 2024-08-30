package com.mohab.detectiveai.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val personality: String,
    val killer: Boolean = false ,
    val relationship: String = "Friend of the group",
    @Embedded
    val murderDetails: MurderDetails? = null


)

@Entity
data class MurderDetails(
    val method: String,
    val weapon: String,
    val location: String,
    val motive: String,
    val victim: String,
    val story: String? = null
)

@Entity
data class MessageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String,
    val message: String
)

@Entity
data class VisibleMessageModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val role: String,
    val message: String
)

data class Response(
    val text : String
)

@Entity
data class GameData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val theme: String,
    val difficulty: String,
    @Embedded
    val scheme: ColourScheme
)

@Entity
data class ColourScheme (
    val primary : String,
    val secondary : String,
    val tertiary : String,
    val background : String,
    val onPrimary : String,
    val onSecondary : String,
    val onTertiary : String,
    val onBackground : String,
)
