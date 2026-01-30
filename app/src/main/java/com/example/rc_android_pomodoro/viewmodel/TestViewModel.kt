package com.example.rc_android_pomodoro.viewmodel

import android.util.Log
import com.example.rc_android_pomodoro.data.AppScreen
import com.example.rc_android_pomodoro.database.PomodoroSession
import com.example.rc_android_pomodoro.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class TestViewModel() : PomodoroViewModel() {
    private val nowTimestamp = System.currentTimeMillis()
    private val sessions = (1..20).map {
        val duration = Random.nextInt(1, 25)
        PomodoroSession(
            id = it,
            duration = duration,
            endTime = nowTimestamp - DateUtils.minutesToMillis(it * 30)
        )
    }.sortedByDescending { it.endTime }

    override val isRunning: StateFlow<Boolean> = MutableStateFlow(false)
    override val isSaving: StateFlow<Boolean> = MutableStateFlow(false)
    override val currentScreen: StateFlow<AppScreen> = MutableStateFlow(AppScreen.Main)
    override val progressLeft: Flow<Float> = MutableStateFlow(0.7f)
    override val allSessions: StateFlow<List<PomodoroSession>> = MutableStateFlow(sessions)

    override fun getMinutesLeft() = 15
    override fun getSecondsLeft() = 45
    override fun getTotalMinutes() = 15
    override fun getFormattedTimeLeft() = "15:45"

    override fun navigateTo(screen: AppScreen) {
        Log.d("TestViewModel", "navigateTo called with $screen")
    }

    override fun setCustomTime(minutes: Int) {
        Log.d("TestViewModel", "setCustomTime called with $minutes")
    }

    override fun stopTimer() {
        Log.d("TestViewModel", "stopTimer called")
    }

    override fun startTimer() {
        Log.d("TestViewModel", "startTimer called")
    }
}
