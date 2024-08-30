package com.mohab.detectiveai

import android.app.Application
import androidx.room.Room
import com.mohab.detectiveai.data.database.GameDatabase

class MainApllication : Application() {
    companion object {
        lateinit var gameDatabase : GameDatabase

    }
    override fun onCreate() {
        super.onCreate()
        gameDatabase = Room.databaseBuilder(
            applicationContext,
            GameDatabase::class.java,
            GameDatabase.DATABASE_NAME
        ).build()
    }
}