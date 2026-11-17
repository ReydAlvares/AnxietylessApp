package com.bdd.anxietyless

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TableScreen(onBack: () -> Unit, modifier: Modifier = Modifier) {
    var mode by rememberSaveable { mutableStateOf(0) }
    var maxFactor by rememberSaveable { mutableStateOf(12) }
    var drill by rememberSaveable { mutableStateOf(false) }
    var zoom by rememberSaveable { mutableStateOf(1f) }
    var revealed by remember { mutableStateOf(setOf<Int>()) }
    var picked by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var pickedSquare by rememberSaveable { mutableStateOf(0) }

    val hScroll = rememberScrollState()
    val vScroll = rememberScrollState()
    val sqScroll = rememberScrollState()

    LaunchedEffect(maxFactor, mode, zoom) {
        hScroll.scrollTo(0)
        vScroll.scrollTo(0)
    }

    CalmBackground(modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 14.dp, vertical = 10.dp)) {

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("← Назад", fontSize = 15.sp, color = InkIndigo)
                }
                Spacer(Modifier.width(6.dp))
                Text("Таблицы", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = InkIndigo)
            }

            Spacer(Modifier.height(6.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Умножение", "Квадраты").forEachIndexed { i, label ->
                        FilterChip(
                            selected = mode == i,
                            onClick = { mode = i; revealed = emptySet() },
                            label = { Text(label, fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.4f), labelColor = InkIndigo,
                                selectedContainerColor = Color.White.copy(alpha = 0.95f), selectedLabelColor = DeepLav
                            )
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = drill,
                        onCheckedChange = { drill = it; revealed = emptySet() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White, checkedTrackColor = DeepLav,
                            uncheckedThumbColor = DeepLav, uncheckedTrackColor = Color.White.copy(alpha = 0.5f),
                            uncheckedBorderColor = DeepLav.copy(alpha = 0.4f)
                        )
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Проверка", fontSize = 13.sp, color = InkIndigo)
                }
            }

            Spacer(Modifier.height(6.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                if (mode == 0) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        Text("До:", fontSize = 13.sp, color = InkIndigo)
                        listOf(9, 12, 15, 19).forEach { m ->
                            FilterChip(
                                selected = maxFactor == m,
                                onClick = { maxFactor = m; picked = null; revealed = emptySet() },
                                label = { Text("$m", fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.White.copy(alpha = 0.35f), labelColor = InkIndigo,
                                    selectedContainerColor = DeepLav, selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                } else {
                    Text("Квадраты 2–30", fontSize = 13.sp, color = InkIndigo)
                }
                TextButton(onClick = { revealed = emptySet(); picked = null; pickedSquare = 0 }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                    Text("Сбросить", fontSize = 13.sp, color = DeepLav)
                }
            }

            if (mode == 0) {
                Spacer(Modifier.height(4.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("Масштаб:", fontSize = 12.sp, color = InkIndigo)
                    Spacer(Modifier.width(8.dp))
                    ZoomBtn("−") { zoom = (zoom - 0.25f).coerceAtLeast(1f) }
                    Text(
                        "${(zoom * 100).toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = InkIndigo,
                        textAlign = TextAlign.Center, modifier = Modifier.width(48.dp)
                    )
                    ZoomBtn("+") { zoom = (zoom + 0.25f).coerceAtMost(3f) }
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = { zoom = 1f }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)) {
                        Text("Вписать", fontSize = 13.sp, color = DeepLav, fontWeight = FontWeight.Bold)
                    }
                }
                Text("Тяни ползунки справа и снизу или саму таблицу", fontSize = 11.sp, color = InkIndigo.copy(alpha = 0.65f))
            }

            Spacer(Modifier.height(6.dp))

            Box(Modifier.weight(1f).fillMaxWidth()) {
                if (mode == 0) {
                    BoxWithConstraints(Modifier.fillMaxSize()) {
                        val cols = maxFactor
                        val fitBox = maxWidth / cols
                        val cellBox = (fitBox * zoom).coerceAtMost(56.dp)
                        val baseFont = (cellBox.value * 0.40f).coerceIn(7f, 16f)

                        Column(Modifier.fillMaxSize()) {
                            Row {
                                GridCell("×", cellBox, baseFont.sp, DeepLav, Color.White, true)
                                Row(Modifier.weight(1f).horizontalScroll(hScroll)) {
                                    for (b in 2..maxFactor) {
                                        val hot = picked?.second == b
                                        GridCell("$b", cellBox, baseFont.sp,
                                            if (hot) Color.White else DeepLav.copy(alpha = 0.78f),
                                            if (hot) DeepLav else Color.White, true)
                                    }
                                }
                            }
                            Row(Modifier.weight(1f)) {
                                Column(Modifier.verticalScroll(vScroll)) {
                                    for (a in 2..maxFactor) {
                                        val hot = picked?.first == a
                                        GridCell("$a", cellBox, baseFont.sp,
                                            if (hot) Color.White else DeepLav.copy(alpha = 0.78f),
                                            if (hot) DeepLav else Color.White, true)
                                    }
                                }
                                Box(Modifier.weight(1f).horizontalScroll(hScroll).verticalScroll(vScroll)) {
                                    Column {
                                        for (a in 2..maxFactor) {
                                            Row {
                                                for (b in 2..maxFactor) {
                                                    val key = a * 100 + b
                                                    val isPicked = picked == Pair(a, b)
                                                    val inCross = picked?.first == a || picked?.second == b
                                                    val show = !drill || revealed.contains(key) || isPicked
                                                    val value = "${a * b}"
                                                    val f = (if (value.length >= 3) baseFont * 0.82f else baseFont).sp
                                                    GridCell(
                                                        text = if (show) value else "?",
                                                        box = cellBox, font = f,
                                                        bg = when {
                                                            isPicked -> DeepLav
                                                            inCross -> Color.White.copy(alpha = 0.92f)
                                                            else -> Color.White.copy(alpha = 0.45f)
                                                        },
                                                        fg = if (isPicked) Color.White else InkIndigo,
                                                        bold = isPicked
                                                    ) { picked = Pair(a, b); revealed = revealed + key }
                                                }
                                            }
                                        }
                                    }
                                }
                                DragScrollbar(vScroll, true, Modifier.width(11.dp).fillMaxHeight().padding(vertical = 2.dp, horizontal = 1.dp))
                            }
                        }
                        DragScrollbar(hScroll, false, Modifier.align(Alignment.BottomCenter).height(11.dp).fillMaxWidth().padding(horizontal = 2.dp, vertical = 1.dp))
                    }
                } else {
                    BoxWithConstraints(Modifier.fillMaxSize()) {
                        val cardW = ((maxWidth - 24.dp) / 4).coerceAtMost(92.dp)
                        Box(Modifier.fillMaxSize().verticalScroll(sqScroll)) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth().padding(end = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (n in 2..30) {
                                    val isPicked = pickedSquare == n
                                    val show = !drill || revealed.contains(n) || isPicked
                                    Column(
                                        modifier = Modifier
                                            .width(cardW)
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(if (isPicked) DeepLav else Color.White.copy(alpha = 0.55f))
                                            .clickable { pickedSquare = n; revealed = revealed + n }
                                            .padding(vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("$n²", fontSize = 13.sp, fontWeight = FontWeight.Medium,
                                            color = if (isPicked) Color.White.copy(alpha = 0.85f) else InkIndigo.copy(alpha = 0.7f))
                                        Text(if (show) "${n * n}" else "?", fontSize = 19.sp, fontWeight = FontWeight.Bold,
                                            color = if (isPicked) Color.White else InkIndigo)
                                    }
                                }
                            }
                        }
                        DragScrollbar(sqScroll, true, Modifier.align(Alignment.CenterEnd).width(11.dp).fillMaxHeight().padding(vertical = 2.dp, horizontal = 1.dp))
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Surface(color = Color.White.copy(alpha = 0.75f), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                AnimatedContent(
                    targetState = if (mode == 0) picked?.let { "${it.first} × ${it.second} = ${it.first * it.second}" }
                    else if (pickedSquare > 0) "$pickedSquare² = ${pickedSquare * pickedSquare}" else null,
                    transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                    label = "tableFooter"
                ) { text ->
                    Text(
                        text = text ?: if (drill) "Нажми на клетку — откроется ответ" else "Нажми на клетку — подсветим строку и столбец",
                        fontSize = if (text != null) 21.sp else 13.sp,
                        fontWeight = if (text != null) FontWeight.Bold else FontWeight.Normal,
                        color = InkIndigo, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 13.dp, horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ZoomBtn(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(9.dp))
            .background(Color.White.copy(alpha = 0.75f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) { Text(label, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = DeepLav) }
}

@Composable
private fun GridCell(text: String, box: Dp, font: TextUnit, bg: Color, fg: Color, bold: Boolean = false, onClick: (() -> Unit)? = null) {
    Box(
        modifier = Modifier
            .padding(1.5.dp)
            .size(box - 3.dp)
            .clip(RoundedCornerShape((box.value * 0.22f).coerceIn(3f, 10f).dp))
            .background(bg)
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text, fontSize = font, fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium, color = fg, maxLines = 1, softWrap = false)
    }
}

// Ползунок прокрутки: тянется пальцем, тап по дорожке — прыжок к позиции
@Composable
private fun DragScrollbar(state: ScrollState, vertical: Boolean, modifier: Modifier = Modifier) {
    if (state.maxValue <= 0 || state.maxValue == Int.MAX_VALUE || state.viewportSize <= 0) return
    val scope = rememberCoroutineScope()

    Canvas(
        modifier
            .pointerInput(state, vertical) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        val trackLen = if (vertical) size.height else size.width
                        val content = state.viewportSize + state.maxValue
                        val frac = (state.viewportSize.toFloat() / content).coerceIn(0.12f, 1f)
                        val thumbLen = (trackLen * frac).coerceAtLeast(28.dp.toPx()).coerceAtMost(trackLen.toFloat())
                        val movable = trackLen - thumbLen
                        if (movable > 0f) {
                            val delta = if (vertical) dragAmount.y else dragAmount.x
                            val scrollDelta = (delta / movable) * state.maxValue
                            val target = (state.value + scrollDelta).roundToInt().coerceIn(0, state.maxValue)
                            scope.launch { state.scrollTo(target) }
                        }
                    }
                )
            }
            .pointerInput(state, vertical) {
                detectTapGestures { offset ->
                    val trackLen = if (vertical) size.height else size.width
                    if (trackLen > 0) {
                        val pos = if (vertical) offset.y else offset.x
                        val target = ((pos / trackLen) * state.maxValue).roundToInt().coerceIn(0, state.maxValue)
                        scope.launch { state.animateScrollTo(target) }
                    }
                }
            }
    ) {
        val trackLen = if (vertical) size.height else size.width
        val thick = if (vertical) size.width else size.height
        if (trackLen <= 0f) return@Canvas
        val content = state.viewportSize + state.maxValue
        val frac = (state.viewportSize.toFloat() / content).coerceIn(0.12f, 1f)
        val thumbLen = (trackLen * frac).coerceAtLeast(28.dp.toPx()).coerceAtMost(trackLen)
        val pos = state.value.toFloat() / state.maxValue
        val start = pos * (trackLen - thumbLen)
        val r = CornerRadius(thick / 2f)

        drawRoundRect(color = Color.White.copy(alpha = 0.5f), size = size, cornerRadius = r)

        val thumbSize = if (vertical) Size(thick, thumbLen) else Size(thumbLen, thick)
        val thumbTop = if (vertical) Offset(0f, start) else Offset(start, 0f)
        drawRoundRect(color = DeepLav, topLeft = thumbTop, size = thumbSize, cornerRadius = r)

        val grip = Color.White.copy(alpha = 0.9f)
        val cx = thumbTop.x + thumbSize.width / 2f
        val cy = thumbTop.y + thumbSize.height / 2f
        val step = 4.dp.toPx()
        val dot = 1.2.dp.toPx()
        if (vertical) {
            repeat(3) { i -> drawCircle(grip, dot, Offset(cx, cy + (i - 1) * step)) }
        } else {
            repeat(3) { i -> drawCircle(grip, dot, Offset(cx + (i - 1) * step, cy)) }
        }
    }
}