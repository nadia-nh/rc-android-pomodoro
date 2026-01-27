package com.example.rc_android_pomodoro.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.example.rc_android_pomodoro.data.AppScreen
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

class PomodoroViewModelImpl(private val dao: PomodoroSessionDao) : PomodoroViewModel() {
    private val _timeLeft = MutableStateFlow(
        DateUtils.minutesToMillis(DEFAULT_DURATION_MINUTES)
    )
    private val _totalTime = MutableStateFlow(
        DateUtils.minutesToMillis(DEFAULT_DURATION_MINUTES)
    )
    private val _isRunning = MutableStateFlow(false)
    private var timer: CountDownTimer? = null
    private var _currentScreen = MutableStateFlow(AppScreen.Main)

    override val isRunning = _isRunning.asStateFlow()
    override val currentScreen = _currentScreen.asStateFlow()

    override val progressLeft: Flow<Float> = combine(_timeLeft, _totalTime) { left, total ->
        if (total == 0L) 0f
        else (left.toFloat() / total.toFloat())
    }

    override val allSessions: StateFlow<List<PomodoroSession>> = dao.getAllSessions()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Update the time display without starting the timer
    override fun setCustomTime(minutes: Int) {
        if (!_isRunning.value) {
            _timeLeft.value = DateUtils.minutesToMillis(minutes)
            _totalTime.value = DateUtils.minutesToMillis(minutes)
        }
    }

    override fun getMinutesLeft(): Int {
        return DateUtils.millisToMinutes(_timeLeft.value)
    }

    override fun getSecondsLeft(): Int {
        return DateUtils.millisToSeconds(_timeLeft.value)
    }

    override fun getFormattedTimeLeft(): String {
        return DateUtils.formatTimestampTimeMinutes(_timeLeft.value)
    }

    override fun getTotalMinutes(): Int {
        return DateUtils.millisToMinutes(_totalTime.value)
    }

    override fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    private fun saveSession(minutes: Int) {
        viewModelScope.launch {
            val session = PomodoroSession(
                duration = minutes,
                endTime = System.currentTimeMillis()
            )
            dao.insertSession(session)
        }
    }

    override fun startTimer() {
        _isRunning.value = true
        timer?.cancel()

        val millis = _totalTime.value
        timer = object : CountDownTimer(
            millis,
            DateUtils.secondsToMillis(1)
        ) {
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

    override fun stopTimer() {
        timer?.cancel()
        _isRunning.value = false
        _timeLeft.value = _totalTime.value
    }

    private companion object {
        const val DEFAULT_DURATION_MINUTES = 15
    }
}
