package com.bdd.anxietyless

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import kotlin.math.roundToInt

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
    val encouragingMessages = listOf("Почти! Проверь знаки.", "Не спеши, у тебя получится.", "Где-то ошибка, попробуй еще.", "Вспомни порядок действий.", "Ты близко! Попробуй еще раз.")
    val staticFiltered = remember { allStaticProblems.filter { it.topic in initialTopics && it.difficulty == initialDifficulty } }

    fun getNextQuestion(current: Problem?): Problem {
        return if (isRandom) {
            generateRandomProblem(initialTopics, initialDifficulty)
        } else {
            if (staticFiltered.isEmpty()) return Problem("2+2", 4.0, 10, Topic.NUMBERS, Difficulty.EASY)
            if (current == null) staticFiltered.random() else getNextProblem(current, staticFiltered)
        }
    }

    var currentProblem by remember { mutableStateOf(getNextQuestion(null)) }
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

    var isPaused by remember { mutableStateOf(false) }
    var combo by remember { mutableStateOf(0) }
    var maxCombo by remember { mutableStateOf(0) }
    var totalAnswered by remember { mutableStateOf(0) }
    var correctAnswers by remember { mutableStateOf(0) }

    // НОВОЕ: Переменная для хранения времени текущей сессии
    var sessionTotalTimeMs by remember { mutableStateOf(0L) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isCorrect) Color(0xFF4CAF50).copy(alpha = 0.3f) else Color.Transparent,
        animationSpec = tween(300), label = "bg"
    )

    // НОВОЕ: Вспомогательная функция для записи времени в обе статистики
    fun recordAnswerTime(timeMs: Long) {
        StatsManager.recordTime(initialDifficulty, timeMs)
        sessionTotalTimeMs += timeMs
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

    LaunchedEffect(key1 = gameCounter) {
        if (gameCounter > 0) {
            currentProblem = getNextQuestion(currentProblem)
        }

        userAnswer = ""
        val comboMultiplier = if (combo >= 3) max(minAcceleration, 1.0f - (combo - 2) * 0.05f) else 1.0f
        totalTime = (currentProblem.timeLimit * comboMultiplier).roundToInt()
        timeLeft = totalTime
        isGameRunning = true
        isCorrect = false
        attempts = 0
        isError = false
        message = if (combo >= 3) "🔥 Комбо x$combo! Время сокращено!" else "Реши пример!"
        confettiParties = emptyList()

        while (isGameRunning && timeLeft > 0) {
            delay(1000L)
            if (!isPaused && isGameRunning) {
                timeLeft--
            }
        }

        if (timeLeft == 0 && isGameRunning && !isPaused) {
            isGameRunning = false
            totalAnswered++
            combo = 0
            message = "Время вышло! Правильный ответ: ${currentProblem.answer}"
            keyboardController?.hide()

            recordAnswerTime(totalTime * 1000L)

            delay(2000L)
            if (!isCorrect) gameCounter++
        }
    }

    LaunchedEffect(key1 = isCorrect) {
        if (isCorrect) {
            delay(1000L)
            gameCounter++
            isCorrect = false
        }
    }

    fun checkAnswer() {
        if (!isGameRunning || isCorrect || isPaused) return
        val normalizedAnswer = userAnswer.replace(',', '.')
        val userAnswerDouble = normalizedAnswer.toDoubleOrNull()

        if (userAnswerDouble != null && abs(userAnswerDouble - currentProblem.answer) < 0.02) {
            isCorrect = true
            isGameRunning = false
            correctAnswers++
            totalAnswered++
            combo++
            if (combo > maxCombo) maxCombo = combo

            recordAnswerTime(((totalTime - timeLeft) * 1000L).coerceAtLeast(0L))

            message = "Верно! Молодец! ${if (combo >= 3) "🔥 Комбо x$combo" else ""}"
            keyboardController?.hide()

            confettiParties = listOf(Party(
                speed = 0f, maxSpeed = 30f, damping = 0.9f, spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                position = Position.Relative(0.5, 0.3),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
            ))
        } else {
            attempts++
            combo = 0
            if (attempts >= 3) {
                isGameRunning = false
                totalAnswered++

                recordAnswerTime(((totalTime - timeLeft) * 1000L).coerceAtLeast(0L))

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
            modifier = Modifier.fillMaxSize().padding(16.dp).animateContentSize().verticalScroll(scrollState).imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row {
                    TextButton(onClick = { keyboardController?.hide(); isPaused = true }) { Text("⏸ Пауза", fontSize = 16.sp) }
                }
                val percentage = if (totalAnswered > 0) (correctAnswers * 100 / totalAnswered) else 0
                Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small) {
                    Text("Верно: $percentage%", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            }

            Surface(modifier = Modifier.fillMaxWidth(), color = backgroundColor, shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Время: $timeLeft сек", fontSize = 18.sp, fontWeight = if (combo >= 3) FontWeight.Bold else FontWeight.Normal)
                        if (combo >= 3) {
                            Text(text = "Ускорение: x${String.format(Locale.US, "%.2f", max(minAcceleration, 1.0f - (combo - 2) * 0.05f))}", fontSize = 14.sp, color = Color.Red)
                        } else {
                            Text(text = "Лимит: $totalTime сек", fontSize = 14.sp, color = Color.Gray)
                        }
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
            MathText(text = currentProblem.text, fontSize = 48.sp, modifier = Modifier.padding(vertical = 16.dp))
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = userAnswer,
                onValueChange = { userAnswer = it },
                label = { Text("Ваш ответ") },
                enabled = isGameRunning && !isCorrect && !isPaused,
                isError = isError,
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

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { checkAnswer() }, enabled = isGameRunning && !isCorrect && !isPaused, modifier = Modifier.fillMaxWidth()) { Text("Проверить") }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    totalAnswered++
                    combo = 0
                    recordAnswerTime(((totalTime - timeLeft) * 1000L).coerceAtLeast(0L))
                    gameCounter++
                },
                enabled = !isPaused,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) { Text("Пропустить") }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = message, fontSize = 18.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center,
                color = if (isCorrect) Color(0xFF2E7D32) else if (isError) Color(0xFFE65100) else Color.Unspecified
            )
        }

        KonfettiView(modifier = Modifier.fillMaxSize(), parties = confettiParties)

        // ОВЕРЛЕЙ ПАУЗЫ
        if (isPaused) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)).padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Пауза", fontSize = 32.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))

                        // БЛОК СТАТИСТИКИ ТЕКУЩЕЙ СЕССИИ
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                val percentage = if (totalAnswered > 0) (correctAnswers * 100 / totalAnswered) else 0
                                val sessionAvg = if (totalAnswered > 0) (sessionTotalTimeMs / 1000f) / totalAnswered else 0f

                                Text("Результаты сессии:", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                                Text("Верных ответов: $correctAnswers из $totalAnswered ($percentage%)", fontSize = 15.sp)
                                Text("Среднее время: ${String.format(Locale.US, "%.1f", sessionAvg)} сек", fontSize = 15.sp)
                                Text("Максимальное комбо: $maxCombo", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100), modifier = Modifier.padding(top = 4.dp))
                            }
                        }

                        Button(onClick = { isPaused = false }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                            Text("Продолжить", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // НОВАЯ КНОПКА "НАЧАТЬ ЗАНОВО"
                        Button(
                            onClick = {
                                // Сбрасываем всю статистику и запускаем игру заново
                                gameCounter++
                                combo = 0
                                maxCombo = 0
                                totalAnswered = 0
                                correctAnswers = 0
                                sessionTotalTimeMs = 0L
                                userAnswer = ""
                                isPaused = false
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Text("Начать заново", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(onClick = { onOpenSettings() }, modifier = Modifier.fillMaxWidth().height(50.dp)) {
                            Text("Настройки", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { onExit() },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text("Завершить игру", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }
}