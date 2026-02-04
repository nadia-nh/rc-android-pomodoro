package com.example.rc_android_pomodoro

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalSlider
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rc_android_pomodoro.viewmodel.PomodoroViewModel
import com.example.rc_android_pomodoro.viewmodel.TestViewModel
import com.example.rc_android_pomodoro.data.TimerConfig
import com.example.rc_android_pomodoro.util.DateUtils

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    viewModel: PomodoroViewModel = viewModel(),
    ) {
    val isRunning by viewModel.isRunning.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val progressLeft by viewModel.progressLeft.collectAsState(1.0f)
    val timeLeft by viewModel.timeLeft.collectAsState(initial = DateUtils.minutesToMillis(15))

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val timeLeftText = DateUtils.formatTimestampTimeMinutes(timeLeft)
        val timerText = if (isSaving) "Saving..." else timeLeftText
        PomodoroDisplayAndButton(
            isRunning = isRunning,
            progressLeft = progressLeft,
            timerText = timerText,
            onStart = { viewModel.startTimer() },
            onStop = { viewModel.stopTimer() }
        )

        Spacer(modifier = Modifier.height(24.dp))
        PomodoroInputSlider(
            isRunning = isRunning,
            onValueChange = {viewModel.setCustomTime(TimerConfig.durations[it.toInt()])}
        )
    }
}

@Composable
fun PomodoroDisplayAndButton(
    isRunning: Boolean,
    isLandscape: Boolean = false,
    progressLeft: Float,
    timerText: String? = null,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        PomodoroProgressDisplay(
            progressLeft = progressLeft,
            timerText = timerText,
            isLandscape = isLandscape
        )

        Spacer(modifier = Modifier.height(24.dp))
        PomodoroButton(
            isRunning = isRunning,
            onStart = { onStart() },
            onStop = { onStop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PomodoroInputSlider(
    isRunning: Boolean,
    isLandscape: Boolean = false,
    onValueChange: (Float) -> Unit = {},
) {
    val sliderState = rememberSliderState(
        value = TimerConfig.DEFAULT_DURATION_INDEX,
        valueRange = 0f..TimerConfig.maxIndex,
        steps = TimerConfig.sliderNumSteps,
    )
    sliderState.onValueChange = {
        sliderState.value = it
        onValueChange(it)
    }

    PomodoroTimeInput(
        sliderState = sliderState,
        isRunning = isRunning,
        isLandscape = isLandscape
    )
}

private fun getProgressIcon(progressLeft: Float) : String {
    val progress = 1 - progressLeft
    return when {
        progress < 0.3f -> "🌱"
        progress < 0.9f -> "🌿"
        else -> "🌸"
    }
}

@Composable
fun PomodoroProgressDisplay(
    progressLeft: Float,
    timerText: String? = null,
    isLandscape: Boolean = false
) {
    // Circular progress display
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)
    ) {

        CircularProgressIndicator(
            progress = { progressLeft },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val sproutIcon = getProgressIcon(progressLeft)
            Text(text = sproutIcon, fontSize = 48.sp)

            if (isLandscape && timerText != null) {
                Spacer(modifier = Modifier.height(16.dp))
                PomodoroTimerDisplay(timerText)
            }
        }
    }

    if (!isLandscape && timerText != null) {
        Spacer(modifier = Modifier.height(32.dp))
        PomodoroTimerDisplay(timerText = timerText)
    }
}

@Composable
fun PomodoroTimerDisplay(
    timerText: String
) {
    Text(
        text = timerText,
        style = MaterialTheme.typography.displayLarge
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalMaterial3Api::class)
@Composable
fun PomodoroTimeInput(
    sliderState: SliderState,
    isRunning: Boolean,
    isLandscape: Boolean = false,
) {
    if (isLandscape) {
        VerticalSlider(
            state = sliderState,
            modifier = Modifier
                .height(200.dp)
                .width(50.dp),
            enabled = !isRunning,
            reverseDirection = true,
        )
    } else {
        Slider(
            state = sliderState,
            modifier = Modifier
                .height(50.dp)
                .width(200.dp),
            enabled = !isRunning,
        )
    }
}

@Composable
fun PomodoroButton(
    isRunning: Boolean = false,
    onStart: () -> Unit = {},
    onStop: () -> Unit = {}
) {
    Button(
        onClick = { if (isRunning) onStop() else onStart() },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isRunning) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Text(if (isRunning) "Stop Pomodoro" else "Start Pomodoro")
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun PomodoroScreenPreview() {
    PomodoroScreen(viewModel = TestViewModel())
}

@Preview
@Composable
fun PomodoroDisplayAndButtonPreview() {
    PomodoroDisplayAndButton(
        isRunning = false,
        progressLeft = 0.3f,
        timerText = "25:45"
    )
}

@Preview
@Composable
fun PomodoroInputSliderPreview() {
    PomodoroInputSlider(
        isRunning = false,
        isLandscape = true)
}

@Preview
@Composable
fun PomodoroProgressDisplayPreview() {
    PomodoroProgressDisplay(
        progressLeft = 0.3f,
        timerText = "25:45",
        isLandscape = true)
}

@Preview
@Composable
fun PomodoroTimerDisplayPreview() {
    PomodoroTimerDisplay("25:45")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PomodoroTimeInputPreview() {
    val sliderState = rememberSliderState(
        value = TimerConfig.DEFAULT_DURATION_INDEX,
        valueRange = 0f..TimerConfig.maxIndex,
        steps = TimerConfig.sliderNumSteps,
    )
    PomodoroTimeInput(
        sliderState = sliderState,
        isRunning = false,
        isLandscape = true)
}

@Preview
@Composable
fun PomodoroButtonPreview() {
    PomodoroButton(isRunning = true, onStart = {}, onStop = {})
}
