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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rc_android_pomodoro.viewmodel.PomodoroViewModel
import com.example.rc_android_pomodoro.viewmodel.TestViewModel
import com.example.rc_android_pomodoro.data.TimerConfig

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    viewModel: PomodoroViewModel = viewModel(),
    ) {
    val isRunning by viewModel.isRunning.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val progressLeft by viewModel.progressLeft.collectAsState(1.0f)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PomodoroProgressDisplay(progressLeft = progressLeft)

        Spacer(modifier = Modifier.height(32.dp))
        PomodoroTimerDisplay(
            timerText = if (isSaving) "Saving..." else viewModel.getFormattedTimeLeft()
        )

        Spacer(modifier = Modifier.height(24.dp))
        var sliderIndex by remember {
            mutableFloatStateOf(TimerConfig.DEFAULT_DURATION_INDEX) }
        PomodoroTimeInput(
            index = sliderIndex,
            isRunning = isRunning,
            onValueChange = {
                sliderIndex = it
                viewModel.setCustomTime(TimerConfig.durations[it.toInt()])
            }
        )

        PomodoroButton(
            isRunning = isRunning,
            onStart = { viewModel.startTimer() },
            onStop = { viewModel.stopTimer() }
        )
    }
}

@Composable
fun PomodoroProgressDisplay(
    progressLeft: Float
) {
    // Circular progress display
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(250.dp)) {

        CircularProgressIndicator(
            progress = { progressLeft },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)
        )

        // Change icon based on progress
        val progress = 1 - progressLeft
        val sproutIcon = when {
            progress < 0.3f -> "🌱"
            progress < 0.9f -> "🌿"
            else -> "🌸"
        }
        Text(text = sproutIcon, fontSize = 64.sp)
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

@Composable
fun PomodoroTimeInput(
    index: Float,
    isRunning: Boolean,
    onValueChange: (Float) -> Unit,
) {
    // Customization Slider
    Slider(
        value = index,
        enabled = !isRunning,
        onValueChange = { onValueChange(it) },
        valueRange = 0f..TimerConfig.maxIndex,
        steps = TimerConfig.sliderNumSteps,
    )
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
fun PomodoroProgressDisplayPreview() {
    PomodoroProgressDisplay(progressLeft = 0.3f)
}

@Preview
@Composable
fun PomodoroTimerDisplayPreview() {
    PomodoroTimerDisplay("25:45")
}

@Preview
@Composable
fun PomodoroTimeInputPreview() {
    PomodoroTimeInput(index = 14f, isRunning = false, onValueChange = {})
}

@Preview
@Composable
fun PomodoroButtonPreview() {
    PomodoroButton(isRunning = true, onStart = {}, onStop = {})
}
