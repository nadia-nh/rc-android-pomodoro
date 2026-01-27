package com.example.rc_android_pomodoro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rc_android_pomodoro.data.PomodoroSession
import com.example.rc_android_pomodoro.util.DateUtils

@Composable
fun SessionItem(session: PomodoroSession) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🌱", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${session.duration} Minute Focus",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = formatPomodoroTimestamp(
                        session.endTime),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatPomodoroTimestamp(timestamp: Long): String {
    val date = DateUtils.formatTimestampDate(timestamp)
    val endTime = DateUtils.formatTimestampTime(timestamp)
    return "$date - $endTime"
}

@Preview
@Composable
fun SessionItemPreview() {
    val session = PomodoroSession(
        id = 1,
        duration = 25,
        endTime = System.currentTimeMillis()
    )
    SessionItem(session = session)
}
