package com.bdd.anxietyless

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.sin

val SkyTop = Color(0xFF8FD3FA)
val SkyMid = Color(0xFFA9C6F7)
val LavBottom = Color(0xFFCBB9F6)
val InkIndigo = Color(0xFF2B2560)
val DeepLav = Color(0xFF5B4B9E)

@Composable
fun CalmBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    val transition = rememberInfiniteTransition(label = "sky")
    val phase by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(14000, easing = LinearEasing), RepeatMode.Reverse), label = "phase")
    val drift by transition.animateFloat(0f, 1f, infiniteRepeatable(tween(60000, easing = LinearEasing), RepeatMode.Restart), label = "drift")
    val top = lerp(SkyTop, Color(0xFF9FBDF9), phase)
    val bottom = lerp(LavBottom, Color(0xFFB7C4F8), phase)

    Box(modifier.background(Brush.verticalGradient(listOf(top, SkyMid, bottom)))) {
        Canvas(Modifier.fillMaxSize()) {
            repeat(16) { i ->
                val seedX = ((i * 73) % 100) / 100f
                val seedY = ((i * 37) % 100) / 100f
                val speed = (1 + i % 3).toFloat()
                val y = (seedY + drift * speed) % 1f
                val x = seedX + 0.03f * sin((drift * 4 * PI + i).toFloat())
                drawCircle(
                    color = Color.White.copy(alpha = 0.08f + (i % 3) * 0.05f),
                    radius = (2.5f + (i % 4) * 2f).dp.toPx(),
                    center = Offset(x * size.width, (1f - y) * size.height)
                )
            }
        }
        content()
    }
}

@Composable
fun LotusMark(modifier: Modifier = Modifier, breathScale: Float = 1f) {
    Canvas(modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val baseY = h * 0.78f
        val petalLen = h * 0.62f * breathScale
        val petalW = w * 0.16f * breathScale

        drawCircle(
            brush = Brush.radialGradient(
                listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(cx, h * 0.5f), radius = w * 0.55f
            ),
            radius = w * 0.55f, center = Offset(cx, h * 0.5f)
        )

        listOf(-52f, -26f, 0f, 26f, 52f).forEach { a ->
            rotate(degrees = a, pivot = Offset(cx, baseY)) {
                val len = petalLen * (if (a == 0f) 1f else if (abs(a) > 40f) 0.72f else 0.88f)
                val path = Path().apply {
                    moveTo(cx, baseY)
                    cubicTo(cx - petalW, baseY - len * 0.45f, cx - petalW * 0.55f, baseY - len * 0.85f, cx, baseY - len)
                    cubicTo(cx + petalW * 0.55f, baseY - len * 0.85f, cx + petalW, baseY - len * 0.45f, cx, baseY)
                    close()
                }
                drawPath(path, Color.White.copy(alpha = 0.92f))
            }
        }

        val wave = Path().apply {
            moveTo(w * 0.12f, baseY + h * 0.06f)
            quadraticBezierTo(w * 0.35f, baseY - h * 0.05f, w * 0.55f, baseY + h * 0.05f)
            quadraticBezierTo(w * 0.75f, baseY + h * 0.13f, w * 0.9f, baseY + h * 0.02f)
        }
        drawPath(wave, Color.White.copy(alpha = 0.85f), style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))

        val wave2 = Path().apply {
            moveTo(w * 0.2f, baseY + h * 0.16f)
            quadraticBezierTo(w * 0.45f, baseY + h * 0.06f, w * 0.65f, baseY + h * 0.16f)
            quadraticBezierTo(w * 0.8f, baseY + h * 0.22f, w * 0.88f, baseY + h * 0.14f)
        }
        drawPath(wave2, Color.White.copy(alpha = 0.6f), style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round))
    }
}

@Composable
fun AnimatedEntrance(index: Int = 0, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(index * 80L); shown = true }
    val alpha by animateFloatAsState(if (shown) 1f else 0f, tween(420), label = "entranceA")
    val shift by animateFloatAsState(if (shown) 0f else 36f, tween(420, easing = FastOutSlowInEasing), label = "entranceT")
    Box(modifier.graphicsLayer { this.alpha = alpha; translationY = shift }) { content() }
}