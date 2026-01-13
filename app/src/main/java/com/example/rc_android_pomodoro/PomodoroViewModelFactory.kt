package com.example.rc_android_pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rc_android_pomodoro.data.PomodoroSessionDao

class PomodoroViewModelFactory(private val dao: PomodoroSessionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PomodoroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PomodoroViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
