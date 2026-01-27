package com.example.rc_android_pomodoro.viewmodel

import androidx.lifecycle.ViewModel
import com.example.rc_android_pomodoro.data.AppScreen
import com.example.rc_android_pomodoro.data.PomodoroSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

abstract class PomodoroViewModel() : ViewModel() {
    abstract val isRunning : StateFlow<Boolean>
    abstract val currentScreen : StateFlow<AppScreen>
    abstract val progressLeft: Flow<Float>
    abstract val allSessions: StateFlow<List<PomodoroSession>>
    abstract fun setCustomTime(minutes: Int)
    abstract fun getMinutesLeft(): Int
    abstract fun getSecondsLeft(): Int
    abstract fun getFormattedTimeLeft(): String
    abstract fun getTotalMinutes(): Int
    abstract fun navigateTo(screen: AppScreen)
    abstract fun startTimer()
    abstract fun stopTimer()
}

