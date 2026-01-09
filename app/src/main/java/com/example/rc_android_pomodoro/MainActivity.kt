package com.example.rc_android_pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.rc_android_pomodoro.ui.theme.RCandroidpomodoroTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RCandroidpomodoroTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        PomodoroScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                }
            }
        }
    }
}