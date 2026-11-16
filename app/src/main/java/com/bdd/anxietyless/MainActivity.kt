package com.bdd.anxietyless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bdd.anxietyless.ui.theme.AnxietylessTheme
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

enum class Topic(val displayName: String) {
    NUMBERS("Числа"), FRACTIONS("Дроби"), ROOTS("Корни"), TRIGONOMETRY("Тригонометрия")
}

enum class Difficulty(val displayName: String) {
    EASY("Легко"), MEDIUM("Средне"), HARD("Сложно"), FAST("Быстро")
}

data class Problem(
    val text: String,
    val answer: Double,
    val timeLimit: Int,
    val topic: Topic,
    val difficulty: Difficulty
)

// 1. Базовые примеры (Легко, Средне, Сложно)
val baseProblems = listOf(
    // ==========================================
    // ТЕМА: ЧИСЛА (NUMBERS)
    // ==========================================
    // Легко (2 действия)
    Problem("2 + 3 * 4", 14.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("10 - 6 / 2", 7.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(5 + 5) * 2", 20.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("15 + 5 - 3", 17.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("20 / 4 + 5", 10.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("8 * 2 - 10", 6.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(12 - 4) / 2", 4.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("6 + 3 * 3", 15.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("18 / 3 + 2", 8.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("4 * 4 - 5", 11.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("30 / 5 + 7", 13.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("2 * 8 - 6", 10.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(9 + 1) * 3", 30.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("16 - 12 / 3", 12.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("7 * 2 + 4", 18.0, 20, Topic.NUMBERS, Difficulty.EASY),

    // Средне (2-3 действия)
    Problem("15 * 2 - 10", 20.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("100 / 5 + 12", 32.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("7 * 8 - 6 * 2", 44.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(10 + 5) * 3 - 10", 35.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("24 / 3 + 15 - 5", 18.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(15 - 5) * 4 / 2", 20.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("18 + 12 * 2", 42.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("50 - 20 / 4", 45.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(8 + 4) * (10 - 7)", 36.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("45 / 5 + 3 * 4", 21.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("60 - (15 * 2) + 5", 35.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(20 + 10) / 3 + 5", 15.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("100 - 15 * 4", 40.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("3 * 15 - 10 + 5", 40.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(12 / 3) * 7 - 14", 14.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),

    // Сложно (3 действия)
    Problem("(12 + 8) * 3 - 15", 45.0, 60, Topic.NUMBERS, Difficulty.HARD),
    Problem("50 / 2 * 3 + 10", 85.0, 60, Topic.NUMBERS, Difficulty.HARD),
    Problem("100 - (25 * 2) / 5", 90.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("15 * 4 + 12 / 3 - 5", 59.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(80 / 4) + 15 * 2 - 10", 40.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("(5 * 6 + 10) / 4", 10.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("200 - (50 / 2) * 3", 125.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(14 + 16) * 2 - 30 / 3", 50.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("25 * 3 + (40 / 8) - 5", 75.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("(100 - 20) / 4 + 15 * 2", 50.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("36 / 6 * 7 - 12 + 8", 38.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(90 / 3) + 15 * 3 - 10", 65.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("12 * (5 + 5) - 20 / 2", 110.0, 75, Topic.NUMBERS, Difficulty.HARD),
    Problem("(60 - 15) / 5 * 4 + 8", 44.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("150 - (30 * 4) + 50 / 2", 55.0, 70, Topic.NUMBERS, Difficulty.HARD),

    // ==========================================
    // ТЕМА: ДРОБИ (FRACTIONS)
    // ==========================================
    // Легко (2 действия)
    Problem("1/2 + 1/4", 0.75, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/4 - 1/2", 0.25, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 * 1/2", 0.25, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/5 + 2/5", 0.6, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/5 - 1/5", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/10 + 3/10", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("5/8 - 1/8", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 + 1/8", 0.625, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/4 - 1/4", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("2/5 + 1/5", 0.6, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/4 * 2/1", 0.5, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("7/10 - 2/10", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 + 3/8", 0.875, 30, Topic.FRACTIONS, Difficulty.EASY),
    Problem("4/5 - 2/5", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 * 1/5", 0.1, 25, Topic.FRACTIONS, Difficulty.EASY),

    // Средне (2-3 действия)
    Problem("1/2 + 1/4 * 2", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/4 - 1/2) * 4", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 * 3/4 + 1/8", 0.5, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/5 + 3/5) * 2", 1.6, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("3/4 / 2 + 1/4", 0.625, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/2 + 1/2) * 3/4", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/10 + 2/5 * 2", 0.9, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("5/8 - 1/4 * 2", 0.125, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(7/10 - 1/2) * 5", 1.0, 55, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2/3 * 3/4 * 1/2", 0.25, 60, Topic.FRACTIONS, Difficulty.MEDIUM), // Все сокращается
    Problem("(1/4 + 1/4) / 1/2", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1 - 1/2 * 1/2", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/5 - 1/5) * 2", 0.8, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 / 2 + 3/4", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2 * 1/8 + 1/4", 0.5, 45, Topic.FRACTIONS, Difficulty.MEDIUM),

    // Сложно (3 действия)
    Problem("(1/2 + 1/4) / 3/4", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("2/5 * 3/4 + 1/10", 0.4, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(5/8 - 1/2) * 4 + 1", 1.5, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/2 + 1/3 * 3/4", 0.75, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(1 - 1/5) / 2/5", 2.0, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("3/4 * 2/3 - 1/4 + 1/2", 0.75, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(1/2 + 1/8) * 2 - 1/4", 1.0, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/10 / 1/2 + 3/5 * 2", 1.4, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(7/8 - 3/8) / 1/2", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("2/5 * (1/2 + 1/4)", 0.3, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/2 * 2/3 * 3/4 + 1/4", 0.5, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(3/4 + 1/4) / 1/2 - 1", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("5/6 * 3/5 + 1/2", 1.0, 70, Topic.FRACTIONS, Difficulty.HARD), // 5/6 * 3/5 = 3/6 = 1/2 + 1/2 = 1
    Problem("1 - 1/2 / 1/4 + 2", 1.0, 75, Topic.FRACTIONS, Difficulty.HARD), // 1 - (0.5/0.25) + 2 = 1 - 2 + 2 = 1
    Problem("(2/3 + 1/3) * 3/4 - 1/4", 0.5, 70, Topic.FRACTIONS, Difficulty.HARD),

    // ==========================================
    // ТЕМА: КОРНИ (ROOTS)
    // ==========================================
    // Легко (2 действия)
    Problem("√4 + √9", 5.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√16 - √4", 2.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√25 * 2", 10.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√36 / 3", 2.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√49 + 1", 8.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√100 - 5", 5.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("3 + √9", 6.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("10 - √16", 6.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√81 / 9", 1.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√64 / 2", 4.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√121 - 10", 1.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√144 / 6", 2.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√9 * √4", 6.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√25 + √16", 9.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√64 - √36", 2.0, 25, Topic.ROOTS, Difficulty.EASY),

    // Средне (2-3 действия)
    Problem("√9 + √16 - √4", 5.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 / √25 + 5", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√4 + √9) * 2", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√49 * √4 + 3", 17.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√81 / 3 + √16", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√121 - √49 + 2", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√144 / √4 - 3", 3.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("2 * √25 - √9", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√(16 + 9)", 5.0, 50, Topic.ROOTS, Difficulty.MEDIUM), // Внимание: корень из суммы
    Problem("√64 + √36 - 10", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√36 / √9) + 10", 12.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 * 3 - 20", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√225 / 5 + 3", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√16 * √9 - 5", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√49 + 1) / 2", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),

    // Сложно (3 действия)
    Problem("(√16 + √9) * √4", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√144 / √4 - √9", 3.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("(√25 * √4) + √16", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√81 / √9 + √49 - 5", 5.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√100 + √25) / 3", 5.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√36 * √4 - √16 / 2", 10.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√121 - √4) * 2", 18.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√64 / 2 * √9 - 10", 14.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√169 + 1) / √49", 2.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√196 - √25 * 2 + 3", 7.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("(√2 + √2) / √8", 1.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√50 / √2 * 3", 15.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√3 * √3) + √16 * 2", 11.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√(25 * 4) + √9", 13.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√225 / √25) * √4 - 2", 4.0, 70, Topic.ROOTS, Difficulty.HARD),

    // ==========================================
    // ТЕМА: ТРИГОНОМЕТРИЯ (TRIGONOMETRY)
    // ==========================================
    // Легко (2 действия)
    Problem("sin(30°) + cos(60°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) * cos(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) + sin(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) - sin(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(30°) * cos(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(0°) - cos(60°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) - tan(45°)", 0.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) * sin(30°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) + sin(30°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(0°) + cos(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) - cos(60°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) * tan(45°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(0°) * sin(30°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(30°) + sin(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) * cos(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),

    // Средне (2-3 действия)
    Problem("sin(30°) * cos(60°) + cos(0°)", 1.25, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) / tan(45°) - cos(60°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("(sin(30°) + cos(60°)) * 2", 2.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(0°) + sin(30°) - cos(60°)", 1.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) * cos(0°) / tan(45°)", 1.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("2 * sin(30°) + cos(0°)", 2.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(0°) / 2 + sin(30°)", 1.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) - cos(60°) * 2", 0.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("tan(45°) * cos(60°) + 1", 1.5, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(30°) * 3 - cos(0°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("(cos(0°) + cos(60°)) * 2", 3.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) / 2 + cos(0°)", 1.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(60°) + sin(30°) * 2", 1.5, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("tan(45°) / 2 + sin(90°)", 1.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(30°) + cos(0°) - tan(45°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),

    // Сложно (3 действия)
    Problem("(sin(90°) - cos(0°) + tan(45°)) * 2", 2.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) * cos(60°) + cos(60°) * cos(0°)", 0.75, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(cos(0°) + sin(30°)) / tan(45°)", 1.5, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(90°) * tan(45°) - cos(0°) + 2", 2.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(60°) * 4 - sin(30°) / 0.5", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(30°) + sin(90°)) * cos(60°)", 0.75, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("tan(45°) * 2 + cos(0°) - 3", 0.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(90°) / (cos(0°) * tan(45°)) + 1", 2.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(cos(0°) - cos(60°)) * sin(90°)", 0.5, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) * 2 + cos(0°) / 2", 1.5, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(90°) + cos(0°)) * (tan(45°) - 0.5)", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) / 0.5 + cos(60°) * 2", 2.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(tan(45°) + cos(0°)) / 2 - sin(0°)", 1.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(0°) * 3 - sin(30°) * 4", 1.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(90°) * cos(60°) + tan(45°)) / 1.5", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD)
)

// 2. Автоматически создаем "Быстро" из "Сложно", уменьшая время в 1.5 раза
val fastProblems = baseProblems
    .filter { it.difficulty == Difficulty.HARD } // Берем только сложные
    .map {
        it.copy(
            difficulty = Difficulty.FAST,
            timeLimit = (it.timeLimit / 1.5).toInt() // Делим время на 1.5
        )
    }

// 3. Итоговый список для игры
val allProblems = baseProblems + fastProblems

// Функция, которая гарантированно выберет НОВЫЙ пример (если он не один в списке)
fun getNextProblem(current: Problem, list: List<Problem>): Problem {
    if (list.size <= 1) return list.first()
    var next = list.random()
    while (next == current) {
        next = list.random()
    }
    return next
}

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
    var isGameStarted by remember { mutableStateOf(false) }
    var gameProblems by remember { mutableStateOf(listOf<Problem>()) }

    if (isGameStarted) {
        GameScreen(
            problems = gameProblems,
            onBack = { isGameStarted = false },
            modifier = modifier
        )
    } else {
        StartScreen(
            onStart = { selectedTopics, selectedDifficulty ->
                val filtered = allProblems.filter {
                    it.topic in selectedTopics && it.difficulty == selectedDifficulty
                }
                if (filtered.isNotEmpty()) {
                    gameProblems = filtered
                    isGameStarted = true
                }
            },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartScreen(
    onStart: (Set<Topic>, Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTopics by remember { mutableStateOf(setOf(Topic.NUMBERS)) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Настройки тренировки", fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 32.dp))

        Text("Выберите темы:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start))
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Topic.values().forEach { topic ->
                FilterChip(
                    selected = selectedTopics.contains(topic),
                    onClick = {
                        selectedTopics = if (selectedTopics.contains(topic)) selectedTopics - topic else selectedTopics + topic
                    },
                    label = { Text(topic.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Выберите сложность:", fontSize = 18.sp, modifier = Modifier.align(Alignment.Start))
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Difficulty.values().forEach { diff ->
                FilterChip(
                    selected = selectedDifficulty == diff,
                    onClick = { selectedDifficulty = diff },
                    label = { Text(diff.displayName) }
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onStart(selectedTopics, selectedDifficulty) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = selectedTopics.isNotEmpty()
        ) {
            Text("Начать игру", fontSize = 18.sp)
        }
    }
}

@Composable
fun GameScreen(
    problems: List<Problem>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val encouragingMessages = listOf("Почти! Проверь знаки.", "Не спеши, у тебя получится.", "Где-то ошибка, попробуй еще.", "Вспомни порядок действий.", "Ты близко! Попробуй еще раз.")

    var currentProblem by remember { mutableStateOf(problems.random()) }
    var gameCounter by remember { mutableStateOf(0) }

    var userAnswer by remember { mutableStateOf("") }
    var totalTime by remember { mutableStateOf(currentProblem.timeLimit) }
    var timeLeft by remember { mutableStateOf(totalTime) }
    var message by remember { mutableStateOf("Реши пример!") }
    var isGameRunning by remember { mutableStateOf(true) }
    var isCorrect by remember { mutableStateOf(false) }

    var attempts by remember { mutableStateOf(0) }
    var isError by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    var confettiParties by remember { mutableStateOf(listOf<Party>()) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isCorrect) Color(0xFF4CAF50).copy(alpha = 0.3f) else Color.Transparent,
        animationSpec = tween(300), label = "bg"
    )

    LaunchedEffect(isError) {
        if (isError) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            repeat(3) {
                shakeOffset.animateTo(15f, tween(50))
                shakeOffset.animateTo(-15f, tween(50))
            }
            shakeOffset.animateTo(0f, tween(50))
            delay(1000L)
            isError = false
        }
    }

    LaunchedEffect(key1 = gameCounter) {
        if (gameCounter > 0) {
            currentProblem = getNextProblem(currentProblem, problems)
        }

        totalTime = currentProblem.timeLimit
        timeLeft = totalTime
        isGameRunning = true
        isCorrect = false
        attempts = 0
        isError = false
        message = "Реши пример!"
        confettiParties = emptyList()

        while (isGameRunning && timeLeft > 0) {
            delay(1000L)
            if (!isGameRunning) break
            timeLeft--
        }

        if (timeLeft == 0 && isGameRunning) {
            isGameRunning = false
            message = "Время вышло! Правильный ответ: ${currentProblem.answer}"
            delay(2000L)
            if (!isCorrect) {
                gameCounter++
            }
        }
    }

    LaunchedEffect(key1 = isCorrect) {
        if (isCorrect) {
            delay(1000L)
            gameCounter++
            userAnswer = ""
            isCorrect = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                TextButton(onClick = onBack) { Text("← Меню", fontSize = 16.sp) }
            }

            Surface(modifier = Modifier.fillMaxWidth(), color = backgroundColor, shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Время: $timeLeft сек", fontSize = 18.sp)
                        Text(text = "Лимит: $totalTime сек", fontSize = 14.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { if (totalTime > 0) timeLeft.toFloat() / totalTime else 0f },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (timeLeft <= 5) Color.Red else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            MathText(
                text = currentProblem.text,
                fontSize = 48.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Ваш ответ (десятичная дробь)") },
                enabled = isGameRunning && !isCorrect,
                isError = isError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth().offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val normalizedAnswer = userAnswer.replace(',', '.')
                    val userAnswerDouble = normalizedAnswer.toDoubleOrNull()

                    if (userAnswerDouble != null && abs(userAnswerDouble - currentProblem.answer) < 0.001) {
                        isCorrect = true
                        isGameRunning = false
                        message = "Верно! Молодец!"

                        confettiParties = listOf(Party(
                            speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
                            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                            position = Position.Relative(0.5, 0.3),
                            emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
                        ))
                    } else {
                        attempts++
                        if (attempts >= 3) {
                            isGameRunning = false
                            message = "Подсказка: правильный ответ ${currentProblem.answer}. Нажми «Пропустить», чтобы продолжить."
                        } else {
                            isError = true
                            message = encouragingMessages.random()
                        }
                    }
                },
                enabled = isGameRunning && !isCorrect,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Проверить") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    gameCounter++
                    userAnswer = ""
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) { Text("Пропустить") }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message, fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                color = when {
                    isCorrect -> Color(0xFF2E7D32)
                    isError -> Color(0xFFE65100)
                    else -> Color.Unspecified
                }
            )
        }

        KonfettiView(modifier = Modifier.fillMaxSize(), parties = confettiParties)
    }
}

// Безопасный компонент для отображения дробей
// Безопасный компонент для отображения дробей с переносом строк
@OptIn(ExperimentalLayoutApi::class)
// Безопасный компонент для отображения дробей с переносом строк
@Composable
fun MathText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    val parts = text.split(" ")

    // Умное уменьшение шрифта, если пример слишком длинный
    val adaptiveFontSize = when {
        text.length > 25 -> fontSize * 0.5f // Очень длинные примеры
        text.length > 18 -> fontSize * 0.7f // Средние примеры
        else -> fontSize // Короткие примеры
    }

    // Используем FlowRow вместо Row, чтобы элементы переносились на новую строку
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically // <--- ИСПРАВЛЕНО ЗДЕСЬ
    ) {
        parts.forEachIndexed { index, part ->
            val split = part.split("/")
            // Проверяем, что это дробь
            if (split.size == 2 && split[0].isNotEmpty() && split[0].all { it.isDigit() } && split[1].isNotEmpty() && split[1].all { it.isDigit() }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(split[0], fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                    Text(split[1], fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
                }
            } else {
                // Обычный текст или математический знак
                Text(part, fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
            }

            // Отступ между элементами
            if (index < parts.size - 1) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}