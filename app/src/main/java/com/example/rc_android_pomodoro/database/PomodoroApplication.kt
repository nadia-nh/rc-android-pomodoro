package com.example.rc_android_pomodoro.database

import android.app.Application
import androidx.room.Room

class PomodoroApplication : Application() {
    val database: PomodoroDatabase by lazy {
        Room.databaseBuilder(
            this,
            PomodoroDatabase::class.java,
            "pomodoro-db"
        ).build()
    }
}