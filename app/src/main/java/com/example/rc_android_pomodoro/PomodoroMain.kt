package com.example.rc_android_pomodoro

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.rc_android_pomodoro.data.AppScreen
import com.example.rc_android_pomodoro.viewmodel.PomodoroViewModel

@Composable
fun PomodoroMain(viewModel: PomodoroViewModel) {
    val screenOrientation = LocalConfiguration.current.orientation
    val currentScreen = viewModel.currentScreen.collectAsState()

    PomodoroMainStateless(
        currentScreen = currentScreen.value,
        screenOrientation = screenOrientation,
        onClickMain = { viewModel.navigateTo(AppScreen.Main) },
        onClickHistory = { viewModel.navigateTo(AppScreen.History) },
        mainScreen = { innerPadding ->
            PomodoroScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel) },
        historyScreen = { innerPadding ->
            HistoryScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel) }
        )
}

@Composable
fun PomodoroMainStateless(
    currentScreen: AppScreen,
    screenOrientation: Int,
    onClickMain: () -> Unit,
    onClickHistory: () -> Unit,
    mainScreen: @Composable ((PaddingValues) -> Unit) = {},
    historyScreen: @Composable ((PaddingValues) -> Unit) = {}
) {
    val isLandscape = screenOrientation == Configuration.ORIENTATION_LANDSCAPE

    Row(modifier = Modifier.fillMaxSize()) {
        if (isLandscape) {
            PomodoroMainNavigationRail(
                currentScreen,
                onClickMain,
                onClickHistory
            )
        }

        Scaffold(
            bottomBar = {
                if (!isLandscape) {
                    PomodoroMainBottomBar(
                        currentScreen,
                        onClickMain = onClickMain,
                        onClickHistory = onClickHistory
                    )
                }
            }
        ) { innerPadding ->
            when (currentScreen) {
                AppScreen.Main -> mainScreen(innerPadding)
                AppScreen.History -> historyScreen(innerPadding)
            }
        }
    }
}

@Composable
fun PomodoroMainBottomBar(
    currentScreen: AppScreen,
    onClickMain: () -> Unit,
    onClickHistory: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == AppScreen.Main,
            onClick = onClickMain,
            icon = {
                Icon(Icons.Default.Timer,
                    "Timer") },
            label = { Text("Timer") }
        )
        NavigationBarItem(
            selected = currentScreen == AppScreen.History,
            onClick = onClickHistory,
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    "History") },
            label = { Text("History") }
        )
    }
}

@Composable
fun PomodoroMainNavigationRail(
    currentScreen: AppScreen,
    onClickMain: () -> Unit,
    onClickHistory: () -> Unit
){
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        NavigationRailItem(
            selected = currentScreen == AppScreen.Main,
            onClick = onClickMain,
            icon = { Icon(Icons.Default.Timer,
                "Timer") },
            label = { Text("Timer") }
        )
        NavigationRailItem(
            selected = currentScreen == AppScreen.History,
            onClick = onClickHistory,
            icon = { Icon(Icons.AutoMirrored.Filled.List,
                "History") },
            label = { Text("History") }
        )
    }
}

@Preview
@Composable
fun PomodoroMainPreview() {
    PomodoroMainStateless(
        currentScreen = AppScreen.Main,
        screenOrientation = Configuration.ORIENTATION_LANDSCAPE,
        onClickMain = {},
        onClickHistory = {},
        mainScreen = {},
        historyScreen = {}
    )
}


@Preview
@Composable
fun PomodoroMainBottomBarPreview() {
    PomodoroMainBottomBar(
        currentScreen = AppScreen.Main,
        onClickMain = {},
        onClickHistory = {}
    )
}

@Preview
@Composable
fun PomodoroMainNavigationRailPreview() {
    PomodoroMainNavigationRail(
        currentScreen = AppScreen.Main,
        onClickMain = {},
        onClickHistory = {}
    )
}