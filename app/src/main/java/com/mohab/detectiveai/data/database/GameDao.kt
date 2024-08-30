package com.mohab.detectiveai.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mohab.detectiveai.data.CharacterData
import com.mohab.detectiveai.data.ColourScheme
import com.mohab.detectiveai.data.GameData
import com.mohab.detectiveai.data.MessageModel
import com.mohab.detectiveai.data.VisibleMessageModel

@Dao
interface CharacterDao {
    @Query("SELECT * FROM CharacterData")
    fun getCharacters(): LiveData<List<CharacterData>>

    @Query("SELECT * FROM CharacterData WHERE killer = 1")
    fun getKiller(): LiveData<CharacterData>

    @Insert
    fun insertCharacter(character: CharacterData)

    @Query("DELETE FROM CharacterData")
    fun deleteAllCharacters()

}

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageModel")
    fun getMessages(): LiveData<List<MessageModel>>

    @Insert
    fun insertMessage(message: MessageModel)

    @Query("DELETE FROM MessageModel")
    fun deleteAllMessages()
}

@Dao
interface VisibleMessageDao {
    @Query("SELECT * FROM VisibleMessageModel")
    fun getMessages(): LiveData<List<VisibleMessageModel>>

    @Insert
    fun insertMessage(message: VisibleMessageModel)

    @Query("DELETE FROM VisibleMessageModel")
    fun deleteAllMessages()
}

@Dao
interface GameDao {

    @Query("SELECT * FROM GameData")
    fun getGames(): LiveData<List<GameData>>

    @Insert
    fun insertGame(game: GameData)
    @Query("DELETE FROM GameData")
    fun deleteAllGames()
}