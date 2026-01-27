package com.example.rc_android_pomodoro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.rc_android_pomodoro.database.PomodoroSession
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroSessionDao {
    @Insert
    suspend fun insertSession(session: PomodoroSession)

    @Query("SELECT * FROM sessions ORDER BY endTime DESC")
    fun getAllSessions(): Flow<List<PomodoroSession>>
}