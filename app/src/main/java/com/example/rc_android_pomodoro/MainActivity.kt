package com.example.rc_android_pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rc_android_pomodoro.ui.theme.RCandroidpomodoroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RCandroidpomodoroTheme {
                val app = LocalContext.current.applicationContext as PomodoroApplication
                val dao = app.database.sessionDao()

                val pomodoroViewModel: PomodoroViewModel = viewModel(
                    factory = PomodoroViewModelFactory(dao)
                )

                PomodoroMain(viewModel = pomodoroViewModel)
            }
        }
    }
}