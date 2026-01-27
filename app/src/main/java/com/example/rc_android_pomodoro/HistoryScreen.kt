package com.example.rc_android_pomodoro

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rc_android_pomodoro.data.PomodoroSession
import com.example.rc_android_pomodoro.viewmodel.PomodoroViewModel
import com.example.rc_android_pomodoro.viewmodel.TestViewModel

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: PomodoroViewModel = viewModel()) {
    val sessions by viewModel.allSessions.collectAsState()
    HistoryScreenStateless(modifier = modifier, sessions = sessions)
}

@Composable
fun HistoryScreenStateless(modifier: Modifier = Modifier,
                           sessions: List<PomodoroSession>) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Pomodoro History",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (sessions.isEmpty()) {
            Text("No history yet. Start focusing to plant your first seed!")
        } else {
            LazyColumn {
                items(sessions) { session ->
                    SessionItem(session)
                }
            }
        }
    }
}


@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(modifier = Modifier, viewModel = TestViewModel())
}
