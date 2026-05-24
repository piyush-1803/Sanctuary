package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.*
import com.example.ui.theme.SanctuaryThemeProvider
import com.example.ui.viewmodel.JournalViewModel
import com.example.ui.viewmodel.JournalViewModelFactory
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: JournalViewModel = viewModel(
                factory = JournalViewModelFactory(application)
            )
            val currentTheme by viewModel.activeTheme.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            SanctuaryThemeProvider(selectedTheme = currentTheme) {
                // Main application scaffolding
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Display bottom navigation only on standard hub screens (Library, Timeline, Settings)
                        if (currentScreen != Screen.Onboarding && currentScreen != Screen.Writing) {
                            Surface(
                                tonalElevation = 8.dp,
                                shadowElevation = 10.dp,
                                color = Color(0xFFFFFDF9)
                            ) {
                                NavigationBar(
                                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
                                    containerColor = Color(0xFFFFFDF5),
                                    tonalElevation = 0.dp
                                ) {
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Library,
                                        onClick = { viewModel.navigateTo(Screen.Library) },
                                        icon = { Icon(Icons.Default.Home, contentDescription = "Bookshelf") },
                                        label = { Text("Library", style = MaterialTheme.typography.labelSmall) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color(0xFFD48256),
                                            unselectedIconColor = Color(0xFF9E8E7D),
                                            selectedTextColor = Color(0xFFD48256),
                                            unselectedTextColor = Color(0xFF9E8E7D),
                                            indicatorColor = Color(0x23D48256)
                                        )
                                    )
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Timeline,
                                        onClick = { viewModel.navigateTo(Screen.Timeline) },
                                        icon = { Icon(Icons.Default.List, contentDescription = "Memory Chronicle") },
                                        label = { Text("Memories", style = MaterialTheme.typography.labelSmall) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color(0xFFD48256),
                                            unselectedIconColor = Color(0xFF9E8E7D),
                                            selectedTextColor = Color(0xFFD48256),
                                            unselectedTextColor = Color(0xFF9E8E7D),
                                            indicatorColor = Color(0x23D48256)
                                        )
                                    )
                                    NavigationBarItem(
                                        selected = currentScreen == Screen.Settings,
                                        onClick = { viewModel.navigateTo(Screen.Settings) },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = "Ambient Settings") },
                                        label = { Text("Settings", style = MaterialTheme.typography.labelSmall) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = Color(0xFFD48256),
                                            unselectedIconColor = Color(0xFF9E8E7D),
                                            selectedTextColor = Color(0xFFD48256),
                                            unselectedTextColor = Color(0xFF9E8E7D),
                                            indicatorColor = Color(0x23D48256)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = if (currentScreen != Screen.Onboarding && currentScreen != Screen.Writing) innerPadding.calculateBottomPadding() else 0.dp)
                    ) {
                        when (currentScreen) {
                            Screen.Onboarding -> OnboardingScreen(viewModel = viewModel)
                            Screen.Library -> LibraryScreen(viewModel = viewModel)
                            Screen.Writing -> WritingScreen(viewModel = viewModel)
                            Screen.Timeline -> TimelineScreen(viewModel = viewModel)
                            Screen.Settings -> SettingsScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
