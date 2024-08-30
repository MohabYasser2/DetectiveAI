package com.mohab.detectiveai.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohab.detectiveai.data.CharacterData
import com.mohab.detectiveai.data.GameData
import com.mohab.detectiveai.data.MessageModel
import com.mohab.detectiveai.data.VisibleMessageModel

@Database(entities = [CharacterData::class, MessageModel::class,VisibleMessageModel::class, GameData::class], version = 1)
abstract class GameDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "game_database"
    }

    abstract fun characterDao(): CharacterDao
    abstract fun messageDao(): MessageDao
    abstract fun visibleMessageDao(): VisibleMessageDao
    abstract fun gameDao(): GameDao



}