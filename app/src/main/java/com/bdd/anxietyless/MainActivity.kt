package com.bdd.anxietyless

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.bdd.anxietyless.ui.theme.AnxietylessTheme

enum class AppScreen { WELCOME, START, GAME, SETTINGS, TABLE }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        StatsManager.init(applicationContext)
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
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.WELCOME) }
    var previousScreen by rememberSaveable { mutableStateOf(AppScreen.START) }
    var selectedTopics by rememberSaveable(stateSaver = Saver<Set<Topic>, ArrayList<String>>(
        save = { set -> ArrayList(set.map { it.name }) },
        restore = { names -> names.mapNotNull { n -> runCatching { Topic.valueOf(n) }.getOrNull() }.toSet().ifEmpty { setOf(Topic.NUMBERS) } }
    )) { mutableStateOf(setOf(Topic.NUMBERS)) }
    var selectedDifficulty by rememberSaveable { mutableStateOf(Difficulty.EASY) }
    var isRandom by rememberSaveable { mutableStateOf(true) }

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE) }
    var minAcceleration by rememberSaveable { mutableStateOf(prefs.getFloat("min_acceleration", 0.5f)) }

    BackHandler(enabled = currentScreen != AppScreen.WELCOME) {
        currentScreen = when (currentScreen) {
            AppScreen.SETTINGS -> previousScreen
            AppScreen.GAME -> AppScreen.START
            AppScreen.TABLE -> AppScreen.START
            else -> AppScreen.WELCOME
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = currentScreen == AppScreen.WELCOME, enter = fadeIn(tween(400)), exit = fadeOut(tween(400))) {
            WelcomeScreen(onStart = { currentScreen = AppScreen.START }, modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = currentScreen == AppScreen.START ||
                    (currentScreen == AppScreen.SETTINGS && previousScreen == AppScreen.START) ||
                    currentScreen == AppScreen.TABLE,
            enter = fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 12 },
            exit = fadeOut(tween(250))
        ) {
            StartScreen(
                onStart = { topics, diff, useRandom ->
                    selectedTopics = topics; selectedDifficulty = diff; isRandom = useRandom
                    currentScreen = AppScreen.GAME
                },
                onOpenSettings = { previousScreen = AppScreen.START; currentScreen = AppScreen.SETTINGS },
                onOpenTable = { currentScreen = AppScreen.TABLE },
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = currentScreen == AppScreen.GAME || (currentScreen == AppScreen.SETTINGS && previousScreen == AppScreen.GAME),
            enter = fadeIn(tween(350)) + scaleIn(initialScale = 0.97f, animationSpec = tween(350)),
            exit = fadeOut(tween(250))
        ) {
            GameScreen(
                initialTopics = selectedTopics,
                initialDifficulty = selectedDifficulty,
                isRandom = isRandom,
                minAcceleration = minAcceleration,
                onExit = { currentScreen = AppScreen.START },
                onOpenSettings = { previousScreen = AppScreen.GAME; currentScreen = AppScreen.SETTINGS },
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = currentScreen == AppScreen.TABLE,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 },
            exit = fadeOut(tween(250)) + slideOutVertically(tween(250)) { it / 10 }
        ) {
            TableScreen(onBack = { currentScreen = AppScreen.START }, modifier = Modifier.fillMaxSize())
        }

        AnimatedVisibility(
            visible = currentScreen == AppScreen.SETTINGS,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 10 },
            exit = fadeOut(tween(250)) + slideOutVertically(tween(250)) { it / 10 }
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                SettingsScreen(
                    currentMinAccel = minAcceleration,
                    onSave = { newValue ->
                        minAcceleration = newValue
                        prefs.edit().putFloat("min_acceleration", newValue).apply()
                    },
                    onBack = { currentScreen = previousScreen },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}