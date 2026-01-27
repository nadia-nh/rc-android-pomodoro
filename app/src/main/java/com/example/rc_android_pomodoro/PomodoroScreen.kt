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

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    viewModel: PomodoroViewModel = viewModel(),
    ) {
    val isRunning by viewModel.isRunning.collectAsState()
    val progressLeft by viewModel.progressLeft.collectAsState(1.0f)

    // Default to 15 minutes, at index 14
    var sliderIndex by remember { mutableFloatStateOf(14f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "%02d:%02d".format(
                viewModel.getMinutesLeft(), viewModel.getSecondsLeft()),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Customization Slider
        Slider(
            value = sliderIndex,
            enabled = !isRunning,
            onValueChange = {
                sliderIndex = it
                // Pass the mapped minutes to the viewModel
                viewModel.setCustomTime(TimerConfig.durations[it.toInt()])
            },
            valueRange = 0f..TimerConfig.maxIndex,
            steps = TimerConfig.sliderNumSteps,
        )

        Button(
            onClick = {
                if (isRunning) viewModel.stopTimer()
                else viewModel.startTimer()
            },
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
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun PomodoroScreenPreview() {
    PomodoroScreen(viewModel = TestViewModel())
}

private object TimerConfig {
    // Generate: 1, 2, 3 ... 15
    private val fastIncrements = (1..15).toList()
    // Generate: 20, 25, 30 ... 45
    private val slowIncrements = (20..45 step 5).toList()

    // Final list: [1, 2, 3, ... 15, 20, 25, ... 45]
    val durations = fastIncrements + slowIncrements

    // Number of steps for the slider, removes start and end values
    val sliderNumSteps = durations.size - 2

    val maxIndex = (durations.size - 1).toFloat()
}