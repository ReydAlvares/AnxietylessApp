package com.bdd.anxietyless

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WelcomeScreen(onStart: () -> Unit, modifier: Modifier = Modifier) {
    val phrases = remember {
        listOf(
            "Ты в безопасности. Здесь можно не спешить.",
            "Спокойный ум решает всё.",
            "Каждый пример — маленький шаг к уверенности.",
            "Дыши ровно — мысли станут яснее.",
            "Ошибки — это черновики успеха.",
            "Ты справляешься лучше, чем думаешь.",
            "Тревога отступает, когда появляется ритм.",
            "Математика — тихая гавань для мыслей.",
            "Сегодня хороший день, чтобы стать чуть спокойнее.",
            "Один пример за раз. Остальное подождёт."
        )
    }
    var phraseIndex by rememberSaveable { mutableStateOf(0) }
    LaunchedEffect(Unit) { while (true) { delay(5000L); phraseIndex = (phraseIndex + 1) % phrases.size } }

    val breathTransition = rememberInfiniteTransition(label = "breath")
    val breathPhase by breathTransition.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart), label = "breathPhase")
    val lotusScale = 0.92f + 0.16f * sin(PI * breathPhase).toFloat()
    val breathLabel = if (breathPhase < 0.5f) "Вдох…" else "Выдох…"

    CalmBackground(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedEntrance(0) { Text("ANXIETYLESS", letterSpacing = 6.sp, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.White.copy(alpha = 0.85f)) }
            Spacer(Modifier.height(20.dp))
            AnimatedEntrance(1) { LotusMark(Modifier.size(220.dp), breathScale = lotusScale) }
            AnimatedEntrance(2) {
                AnimatedContent(breathLabel, transitionSpec = { fadeIn(tween(600)) togetherWith fadeOut(tween(600)) }, label = "breath") {
                    Text(it, color = Color.White.copy(alpha = 0.9f), fontSize = 16.sp)
                }
            }
            Spacer(Modifier.height(24.dp))
            AnimatedEntrance(3) {
                Text("Anxietyless", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = Color.White,
                    style = TextStyle(shadow = Shadow(Color(0x663A2E7A), Offset(0f, 5f), 16f)))
            }
            AnimatedEntrance(4) { Text("тренажёр спокойной математики", fontSize = 16.sp, color = InkIndigo.copy(alpha = 0.75f)) }
            Spacer(Modifier.height(28.dp))
            AnimatedEntrance(5) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.55f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
                ) {
                    Box(Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        AnimatedContent(
                            phraseIndex,
                            transitionSpec = {
                                (slideInVertically(tween(500)) { it / 3 } + fadeIn(tween(500))) togetherWith
                                        (slideOutVertically(tween(400)) { -it / 3 } + fadeOut(tween(400)))
                            },
                            label = "phrase"
                        ) { idx ->
                            Text(phrases[idx], textAlign = TextAlign.Center, fontSize = 17.sp, lineHeight = 24.sp, color = InkIndigo, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            AnimatedEntrance(6) {
                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = DeepLav)
                ) { Text("Начать тренировку", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(14.dp))
            AnimatedEntrance(7) {
                Text("Без оценок и давления. Только ты, числа и спокойное дыхание.", fontSize = 13.sp, color = InkIndigo.copy(alpha = 0.65f), textAlign = TextAlign.Center)
            }
        }
    }
}