package com.example.rc_android_pomodoro.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface PomodoroSessionDao {
    @Insert
    suspend fun insertSession(session: PomodoroSession)
}
