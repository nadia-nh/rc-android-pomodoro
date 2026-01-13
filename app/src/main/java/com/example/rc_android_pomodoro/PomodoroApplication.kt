package com.example.rc_android_pomodoro

import android.app.Application
import androidx.room.Room
import com.example.rc_android_pomodoro.data.PomodoroDatabase

class PomodoroApplication : Application() {
    val database: PomodoroDatabase by lazy {
        Room.databaseBuilder(
            this,
            PomodoroDatabase::class.java,
            "pomodoro-db"
        ).build()
    }
}
