package com.example.rc_android_pomodoro

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class PomodoroViewModel : ViewModel() {
    private val _timeLeft = MutableStateFlow(
        (DEFAULT_DURATION_MINUTES * MILLIS_IN_MINUTE).toLong())
    private val _totalTime = MutableStateFlow(
        (DEFAULT_DURATION_MINUTES * MILLIS_IN_MINUTE).toLong())
    private val _isRunning = MutableStateFlow(false)
    private var timer: CountDownTimer? = null

    val isRunning = _isRunning.asStateFlow()
    val progressLeft: Flow<Float> = combine(_timeLeft, _totalTime) { left, total ->
        if (total == 0L) 0f
        else (left.toFloat() / total.toFloat())
    }

    // Update the time display without starting the timer
    fun setCustomTime(minutes: Int) {
        if (!_isRunning.value) {
            _timeLeft.value = minutes * MILLIS_IN_MINUTE
            _totalTime.value = minutes * MILLIS_IN_MINUTE
        }
    }

    fun getMinutesLeft(): Int {
        return (_timeLeft.value / MILLIS_IN_MINUTE).toInt()
    }

    fun getSecondsLeft(): Int {
        return (_timeLeft.value / MILLIS_IN_SECOND % 60).toInt()
    }

    fun startTimer() {
        _isRunning.value = true
        timer?.cancel()

        val millis = _totalTime.value
        timer = object : CountDownTimer(millis, MILLIS_IN_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished
            }

            override fun onFinish() {
                _timeLeft.value = 0
                _isRunning.value = false
                // TODO: Save to Room
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
        const val MILLIS_IN_SECOND = 1_000L
        const val MILLIS_IN_MINUTE = 60 * MILLIS_IN_SECOND
    }
}
