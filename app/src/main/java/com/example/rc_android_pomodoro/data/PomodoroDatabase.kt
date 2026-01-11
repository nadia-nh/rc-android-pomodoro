package com.example.rc_android_pomodoro.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PomodoroSession::class], version = 1)
abstract class PomodoroDatabase : RoomDatabase() {
    abstract fun sessionDao(): PomodoroSessionDao
}
