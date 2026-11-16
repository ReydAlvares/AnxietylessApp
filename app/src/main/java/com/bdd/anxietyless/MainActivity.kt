package com.bdd.anxietyless

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bdd.anxietyless.ui.theme.AnxietylessTheme

enum class AppScreen { START, GAME, SETTINGS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnxietylessTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(AppScreen.START) }
    var previousScreen by remember { mutableStateOf(AppScreen.START) }

    var selectedTopics by remember { mutableStateOf(setOf(Topic.NUMBERS)) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var isRandom by remember { mutableStateOf(true) }

    // --- НОВОЕ: Чтение и сохранение настройки через SharedPreferences ---
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE) }

    // Инициализируем состояние значением из памяти телефона (если там пусто, берем 0.5f)
    var minAcceleration by remember { mutableStateOf(prefs.getFloat("min_acceleration", 0.5f)) }

    Box(modifier = modifier.fillMaxSize()) {
        if (currentScreen == AppScreen.START || (currentScreen == AppScreen.SETTINGS && previousScreen == AppScreen.START)) {
            StartScreen(
                onStart = { topics, diff, useRandom ->
                    selectedTopics = topics
                    selectedDifficulty = diff
                    isRandom = useRandom
                    currentScreen = AppScreen.GAME
                },
                onOpenSettings = {
                    previousScreen = AppScreen.START
                    currentScreen = AppScreen.SETTINGS
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (currentScreen == AppScreen.GAME || (currentScreen == AppScreen.SETTINGS && previousScreen == AppScreen.GAME)) {
            GameScreen(
                initialTopics = selectedTopics,
                initialDifficulty = selectedDifficulty,
                isRandom = isRandom,
                minAcceleration = minAcceleration,
                onExit = {
                    currentScreen = AppScreen.START
                },
                onOpenSettings = {
                    previousScreen = AppScreen.GAME
                    currentScreen = AppScreen.SETTINGS
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        if (currentScreen == AppScreen.SETTINGS) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                SettingsScreen(
                    currentMinAccel = minAcceleration,
                    onSave = { newValue ->
                        minAcceleration = newValue // Обновляем экран
                        // --- НОВОЕ: Сохраняем в память телефона ---
                        prefs.edit().putFloat("min_acceleration", newValue).apply()
                    },
                    onBack = {
                        currentScreen = previousScreen
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}