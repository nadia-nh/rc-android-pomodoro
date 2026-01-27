package com.example.rc_android_pomodoro

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rc_android_pomodoro.data.PomodoroSession
import com.example.rc_android_pomodoro.data.PomodoroSessionDao
import com.example.rc_android_pomodoro.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    Main,
    History
}

class PomodoroViewModel(private val dao: PomodoroSessionDao) : ViewModel() {
    private val _timeLeft = MutableStateFlow(
        DateUtils.minutesToMillis(DEFAULT_DURATION_MINUTES))
    private val _totalTime = MutableStateFlow(
        DateUtils.minutesToMillis(DEFAULT_DURATION_MINUTES))
    private val _isRunning = MutableStateFlow(false)
    private var timer: CountDownTimer? = null
    private var _currentScreen = MutableStateFlow(AppScreen.Main)

    val isRunning = _isRunning.asStateFlow()
    val currentScreen = _currentScreen.asStateFlow()

    val progressLeft: Flow<Float> = combine(_timeLeft, _totalTime) { left, total ->
        if (total == 0L) 0f
        else (left.toFloat() / total.toFloat())
    }

    val allSessions: StateFlow<List<PomodoroSession>> = dao.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Update the time display without starting the timer
    fun setCustomTime(minutes: Int) {
        if (!_isRunning.value) {
            _timeLeft.value = DateUtils.minutesToMillis(minutes)
            _totalTime.value = DateUtils.minutesToMillis(minutes)
        }
    }

    fun getMinutesLeft(): Int {
        return DateUtils.millisToMinutes(_timeLeft.value)
    }

    fun getSecondsLeft(): Int {
        return DateUtils.millisToSeconds(_timeLeft.value)
    }

    fun getTotalMinutes(): Int {
        return DateUtils.millisToMinutes(_totalTime.value)
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }


    private fun saveSession(minutes: Int) {
        viewModelScope.launch {
            val session = PomodoroSession(
                duration = minutes,
                endTime = System.currentTimeMillis())
            dao.insertSession(session)
        }
    }

    fun startTimer() {
        _isRunning.value = true
        timer?.cancel()

        val millis = _totalTime.value
        timer = object : CountDownTimer(
            millis,
            DateUtils.secondsToMillis(1)) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                _timeLeft.value = 0
                _isRunning.value = false
                saveSession(getTotalMinutes())
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        _isRunning.value = false
        _timeLeft.value = _totalTime.value
    }

    private companion object {
        const val DEFAULT_DURATION_MINUTES = 15
    }
}
