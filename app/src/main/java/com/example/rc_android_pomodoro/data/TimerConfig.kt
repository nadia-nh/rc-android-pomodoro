package com.example.rc_android_pomodoro.data

object TimerConfig {
    // Generate: 1, 2, 3 ... 15
    private val fastIncrements = (1..15).toList()
    // Generate: 20, 25, 30 ... 45
    private val slowIncrements = (20..45 step 5).toList()

    // Final list: [1, 2, 3, ... 15, 20, 25, ... 45]
    val durations = fastIncrements + slowIncrements

    // Number of steps for the slider, removes start and end values
    val sliderNumSteps = durations.size - 2

    val maxIndex = (durations.size - 1).toFloat()

    const val DEFAULT_DURATION_MINUTES = 15
    const val DEFAULT_DURATION_INDEX = 14f
}