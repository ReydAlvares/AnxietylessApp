package com.bdd.anxietyless

import android.os.SystemClock
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.round
import kotlin.math.roundToInt

private val ProblemSaver = Saver<Problem, ArrayList<String>>(
    save = { p -> arrayListOf(p.text, p.answer.toString(), p.timeLimit.toString(), p.topic.name, p.difficulty.name) },
    restore = { a -> Problem(a[0], a[1].toDouble(), a[2].toInt(), Topic.valueOf(a[3]), Difficulty.valueOf(a[4])) }
)

@Composable
fun GameScreen(
    initialTopics: Set<Topic>,
    initialDifficulty: Difficulty,
    isRandom: Boolean,
    minAcceleration: Float,
    onExit: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val encouragingMessages = listOf(
        "Почти! Проверь знаки.", "Не спеши, у тебя получится.", "Где-то ошибка, попробуй еще.",
        "Вспомни порядок действий.", "Ты близко! Попробуй еще раз."
    )

    val staticFiltered = remember(initialTopics, initialDifficulty) {
        allStaticProblems.filter { it.topic in initialTopics && it.difficulty == initialDifficulty }
    }

    fun getNextQuestion(current: Problem?): Problem {
        return if (isRandom) {
            generateRandomProblem(initialTopics, initialDifficulty)
        } else {
            if (staticFiltered.isEmpty()) return Problem("2 + 2", 4.0, 10, Topic.NUMBERS, Difficulty.EASY)
            if (current == null) staticFiltered.random() else getNextProblem(current, staticFiltered)
        }
    }

    var currentProblem by rememberSaveable(stateSaver = ProblemSaver) { mutableStateOf(getNextQuestion(null)) }
    var gameCounter by rememberSaveable { mutableStateOf(0) }
    var handledCounter by rememberSaveable { mutableStateOf(-1) }
    var userAnswer by rememberSaveable { mutableStateOf("") }
    var totalTime by rememberSaveable { mutableStateOf(currentProblem.timeLimit) }
    var timeLeft by rememberSaveable { mutableStateOf(totalTime) }
    var message by rememberSaveable { mutableStateOf("Реши пример!") }
    var isGameRunning by rememberSaveable { mutableStateOf(true) }
    var isCorrect by rememberSaveable { mutableStateOf(false) }
    var isRoundOver by rememberSaveable { mutableStateOf(false) }
    var isShowingTimeout by rememberSaveable { mutableStateOf(false) }
    var attempts by rememberSaveable { mutableStateOf(0) }
    var isError by remember { mutableStateOf(false) }
    val shakeOffset = remember { Animatable(0f) }
    val haptic = LocalHapticFeedback.current
    var confettiParties by remember { mutableStateOf(listOf<Party>()) }
    var isPaused by rememberSaveable { mutableStateOf(false) }
    var combo by rememberSaveable { mutableStateOf(0) }
    var maxCombo by rememberSaveable { mutableStateOf(0) }
    var totalAnswered by rememberSaveable { mutableStateOf(0) }
    var correctAnswers by rememberSaveable { mutableStateOf(0) }
    var sessionTotalTimeMs by rememberSaveable { mutableStateOf(0L) }
    var roundStartMs by rememberSaveable { mutableStateOf(SystemClock.elapsedRealtime()) }
    var pauseStartMs by rememberSaveable { mutableStateOf(0L) }
    var pausedTotalMs by rememberSaveable { mutableStateOf(0L) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isCorrect) Color(0xFF4CAF50).copy(alpha = 0.3f) else Color.Transparent,
        animationSpec = tween(300), label = "bg"
    )
    val progress by animateFloatAsState(
        targetValue = if (totalTime > 0) timeLeft.toFloat() / totalTime else 0f,
        animationSpec = tween(900, easing = LinearEasing), label = "progress"
    )
    val percentage = if (totalAnswered > 0) correctAnswers * 100 / totalAnswered else 0
    val animatedPercentage by animateIntAsState(percentage, tween(450), label = "pct")
    val infinite = rememberInfiniteTransition(label = "timerPulse")
    val pulse by infinite.animateFloat(1f, 1.18f, infiniteRepeatable(tween(450), RepeatMode.Reverse), label = "pulse")
    val timerScale = if (timeLeft <= 5 && isGameRunning && !isPaused) pulse else 1f

    fun elapsedAnswerMs(): Long {
        val now = SystemClock.elapsedRealtime()
        val paused = pausedTotalMs + if (isPaused && pauseStartMs > 0L) now - pauseStartMs else 0L
        return (now - roundStartMs - paused).coerceIn(0L, totalTime * 1000L)
    }

    fun recordAnswerTime(timeMs: Long) {
        StatsManager.recordTime(initialDifficulty, timeMs)
        sessionTotalTimeMs += timeMs
    }

    fun openPause() {
        keyboardController?.hide()
        pauseStartMs = SystemClock.elapsedRealtime()
        isPaused = true
    }

    fun closePause() {
        if (pauseStartMs > 0L) {
            pausedTotalMs += SystemClock.elapsedRealtime() - pauseStartMs
            pauseStartMs = 0L
        }
        isPaused = false
    }

    suspend fun advanceDelay(ms: Long) {
        var left = ms
        while (left > 0L) {
            if (isPaused) delay(100L) else { val step = minOf(left, 100L); delay(step); left -= step }
        }
    }

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

    LaunchedEffect(gameCounter) {
        if (handledCounter != gameCounter) {
            handledCounter = gameCounter
            if (gameCounter > 0) currentProblem = getNextQuestion(currentProblem)
            userAnswer = ""
            val comboMultiplier = if (combo >= 3) max(minAcceleration, 1.0f - (combo - 2) * 0.05f) else 1.0f
            totalTime = (currentProblem.timeLimit * comboMultiplier).roundToInt().coerceAtLeast(3)
            timeLeft = totalTime
            isGameRunning = true
            isCorrect = false
            isRoundOver = false
            isShowingTimeout = false
            attempts = 0
            isError = false
            message = if (combo >= 3) "🔥 Комбо x$combo! Время сокращено!" else "Реши пример!"
            confettiParties = emptyList()
            roundStartMs = SystemClock.elapsedRealtime()
            pausedTotalMs = 0L
            pauseStartMs = 0L
        }

        while (isGameRunning && timeLeft > 0) {
            delay(1000L)
            if (!isPaused && isGameRunning) timeLeft--
        }

        if (timeLeft == 0 && isGameRunning && !isRoundOver && !isPaused) {
            isGameRunning = false
            isRoundOver = true
            isShowingTimeout = true
            totalAnswered++
            combo = 0
            message = "Время вышло! Правильный ответ: ${currentProblem.answer}"
            keyboardController?.hide()
            recordAnswerTime(totalTime * 1000L)
        }
        if (isShowingTimeout) {
            advanceDelay(2000L)
            if (!isCorrect) gameCounter++
        }
    }

    LaunchedEffect(isCorrect) {
        if (isCorrect) {
            advanceDelay(1000L)
            gameCounter++
        }
    }

    fun checkAnswer() {
        if (!isGameRunning || isCorrect || isPaused) return
        val normalizedAnswer = userAnswer.replace(',', '.').trim()
        val userAnswerDouble = normalizedAnswer.toDoubleOrNull()
        // Целые ответы (таблицы, квадраты) — точно; дробные — до 3 знаков
        val whole = abs(currentProblem.answer - round(currentProblem.answer)) < 1e-9
        val tolerance = if (whole) 1e-6 else max(0.0015, abs(currentProblem.answer) * 0.002)
        if (userAnswerDouble != null && abs(userAnswerDouble - currentProblem.answer) < tolerance) {
            isCorrect = true
            isGameRunning = false
            isRoundOver = true
            correctAnswers++
            totalAnswered++
            combo++
            if (combo > maxCombo) maxCombo = combo
            recordAnswerTime(elapsedAnswerMs())
            message = "Верно! Молодец! ${if (combo >= 3) "🔥 Комбо x$combo" else ""}"
            keyboardController?.hide()
            confettiParties = listOf(
                Party(
                    speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    position = Position.Relative(0.5, 0.3),
                    emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
                )
            )
        } else {
            attempts++
            combo = 0
            if (attempts >= 3) {
                isGameRunning = false
                isRoundOver = true
                totalAnswered++
                recordAnswerTime(elapsedAnswerMs())
                message = "Подсказка: правильный ответ ${currentProblem.answer}. Нажми «Пропустить»."
                keyboardController?.hide()
            } else {
                isError = true
                message = encouragingMessages.random()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState).imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedEntrance(0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { openPause() }) { Text("⏸ Пауза", fontSize = 16.sp) }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AnimatedVisibility(visible = combo >= 3, enter = scaleIn(tween(250)) + fadeIn(tween(250)), exit = scaleOut(tween(200)) + fadeOut(tween(200))) {
                            Surface(color = Color(0xFFFFE0B2), shape = MaterialTheme.shapes.small) {
                                Text("🔥 x$combo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100), modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                        }
                        Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                            Text("Верно: $animatedPercentage%", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }
                }
            }

            AnimatedEntrance(1) {
                Surface(modifier = Modifier.fillMaxWidth(), color = backgroundColor, shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Время: $timeLeft сек", fontSize = 18.sp,
                                fontWeight = if (combo >= 3) FontWeight.Bold else FontWeight.Normal,
                                color = if (timeLeft <= 5) Color.Red else Color.Unspecified,
                                modifier = Modifier.graphicsLayer { scaleX = timerScale; scaleY = timerScale }
                            )
                            if (combo >= 3) {
                                Text(text = "Ускорение: x${String.format(Locale.US, "%.2f", max(minAcceleration, 1.0f - (combo - 2) * 0.05f))}", fontSize = 14.sp, color = Color.Red)
                            } else {
                                Text(text = "Лимит: $totalTime сек", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth(), color = if (timeLeft <= 5) Color.Red else MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedEntrance(2) {
                AnimatedContent(
                    targetState = currentProblem,
                    transitionSpec = {
                        (slideInVertically(tween(320)) { it / 2 } + fadeIn(tween(320))) togetherWith
                                (slideOutVertically(tween(220)) { -it / 2 } + fadeOut(tween(220)))
                    },
                    modifier = Modifier.padding(vertical = 16.dp),
                    label = "problem"
                ) { problem -> MathText(text = problem.text, fontSize = 48.sp) }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedEntrance(3) {
                OutlinedTextField(
                    value = userAnswer,
                    onValueChange = { userAnswer = it },
                    label = { Text("Ваш ответ") },
                    enabled = isGameRunning && !isCorrect && !isPaused,
                    isError = isError,
                    trailingIcon = {
                        TextButton(
                            onClick = { userAnswer = if (userAnswer.startsWith("-")) userAnswer.drop(1) else "-$userAnswer" },
                            enabled = isGameRunning && !isCorrect && !isPaused
                        ) { Text("±", fontSize = 22.sp, fontWeight = FontWeight.Bold) }
                    },
                    supportingText = { Text("Дробь — точкой (0.25). ± меняет знак. Округляй до 3 знаков", fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isCorrect) Color(0xFF4CAF50) else if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = if (isCorrect) Color(0xFF4CAF50) else if (isError) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        focusedContainerColor = if (isCorrect) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.Transparent,
                        unfocusedContainerColor = if (isCorrect) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { checkAnswer() }),
                    modifier = Modifier.fillMaxWidth().offset { IntOffset(shakeOffset.value.roundToInt(), 0) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedEntrance(4) {
                Button(onClick = { checkAnswer() }, enabled = isGameRunning && !isCorrect && !isPaused, modifier = Modifier.fillMaxWidth()) { Text("Проверить") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedEntrance(5) {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        if (!isRoundOver) {
                            totalAnswered++
                            recordAnswerTime(elapsedAnswerMs())
                        }
                        combo = 0
                        gameCounter++
                    },
                    enabled = !isPaused && !isCorrect && !isShowingTimeout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Пропустить") }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedEntrance(6) {
                AnimatedContent(targetState = message, transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(200)) }, label = "message") { msg ->
                    Text(
                        text = msg, fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                        color = if (isCorrect) Color(0xFF2E7D32) else if (isError) Color(0xFFE65100) else Color.Unspecified
                    )
                }
            }
        }

        KonfettiView(modifier = Modifier.fillMaxSize(), parties = confettiParties)

        AnimatedVisibility(
            visible = isPaused,
            enter = fadeIn(tween(220)) + scaleIn(initialScale = 0.92f, animationSpec = tween(260)),
            exit = fadeOut(tween(180)) + scaleOut(targetScale = 0.95f, animationSpec = tween(180))
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Пауза", fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                val sessionAvg = if (totalAnswered > 0) (sessionTotalTimeMs / 1000f) / totalAnswered else 0f
                                Text("Результаты сессии:", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                                Text("Верных ответов: $correctAnswers из $totalAnswered ($percentage%)", fontSize = 15.sp)
                                Text("Среднее время: ${String.format(Locale.US, "%.1f", sessionAvg)} сек", fontSize = 15.sp)
                                Text("Максимальное комбо: $maxCombo", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100), modifier = Modifier.padding(top = 4.dp))
                            }
                        }
                        Button(onClick = { closePause() }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Продолжить", fontSize = 18.sp) }
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                gameCounter++
                                combo = 0; maxCombo = 0; totalAnswered = 0; correctAnswers = 0; sessionTotalTimeMs = 0L
                                userAnswer = ""
                                closePause()
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) { Text("Начать заново", fontSize = 18.sp) }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(onClick = { onOpenSettings() }, modifier = Modifier.fillMaxWidth().height(50.dp)) { Text("Настройки", fontSize = 18.sp) }
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedButton(
                            onClick = { onExit() },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) { Text("Завершить игру", fontSize = 18.sp) }
                    }
                }
            }
        }
    }
}