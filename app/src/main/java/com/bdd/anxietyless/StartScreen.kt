package com.bdd.anxietyless

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartScreen(
    onStart: (Set<Topic>, Difficulty, Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenTable: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTopics by remember { mutableStateOf(setOf(Topic.NUMBERS)) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var useRandom by remember { mutableStateOf(true) }

    val hint = when {
        Topic.TABLES in selectedTopics && Topic.DECIMALS in selectedTopics ->
            "Таблица умножения до 19, квадраты до 30 и десятичные вперемешку с целыми."
        Topic.TABLES in selectedTopics -> "Таблица умножения 2–19 и квадраты 2–30. Ответ всегда целый."
        Topic.DECIMALS in selectedTopics -> "Десятые и сотые рядом с обычными числами: 0.25 · 1.5 · 7."
        else -> null
    }

    CalmBackground(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedEntrance(0) { LotusMark(Modifier.size(72.dp)) }
            Spacer(Modifier.height(8.dp))
            AnimatedEntrance(1) {
                Text("Настройки тренировки", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = InkIndigo, modifier = Modifier.padding(bottom = 20.dp))
            }
            AnimatedEntrance(2) {
                Column(Modifier.fillMaxWidth()) {
                    Text("Выберите темы:", fontSize = 18.sp, color = InkIndigo, fontWeight = FontWeight.Medium)
                    FlowRow(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Topic.values().forEach { topic ->
                            FilterChip(
                                selected = selectedTopics.contains(topic),
                                onClick = { selectedTopics = if (selectedTopics.contains(topic)) selectedTopics - topic else selectedTopics + topic },
                                label = { Text(topic.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.White.copy(alpha = 0.4f), labelColor = InkIndigo,
                                    selectedContainerColor = Color.White.copy(alpha = 0.95f), selectedLabelColor = DeepLav
                                )
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = hint != null,
                        enter = fadeIn(tween(250)) + expandVertically(tween(250)),
                        exit = fadeOut(tween(180)) + shrinkVertically(tween(180))
                    ) {
                        Surface(color = Color.White.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Text(hint ?: "", fontSize = 13.sp, color = InkIndigo, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            AnimatedEntrance(3) {
                Column(Modifier.fillMaxWidth()) {
                    Text("Выберите сложность:", fontSize = 18.sp, color = InkIndigo, fontWeight = FontWeight.Medium)
                    FlowRow(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Difficulty.values().forEach { diff ->
                            FilterChip(
                                selected = selectedDifficulty == diff,
                                onClick = { selectedDifficulty = diff },
                                label = { Text(diff.displayName) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color.White.copy(alpha = 0.4f), labelColor = InkIndigo,
                                    selectedContainerColor = Color.White.copy(alpha = 0.95f), selectedLabelColor = DeepLav
                                )
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            AnimatedEntrance(4) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Генерировать случайные примеры", fontSize = 16.sp, color = InkIndigo)
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = useRandom, onCheckedChange = { useRandom = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White, checkedTrackColor = DeepLav,
                            uncheckedThumbColor = DeepLav, uncheckedTrackColor = Color.White.copy(alpha = 0.5f),
                            uncheckedBorderColor = DeepLav.copy(alpha = 0.4f)
                        )
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            AnimatedEntrance(5) {
                Button(
                    onClick = { onStart(selectedTopics, selectedDifficulty, useRandom) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = selectedTopics.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepLav, contentColor = Color.White)
                ) { Text("Начать игру", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }
            Spacer(Modifier.height(12.dp))
            AnimatedEntrance(6) {
                OutlinedButton(
                    onClick = onOpenTable,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = InkIndigo),
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.85f))
                ) { Text("Таблица умножения и квадратов", fontSize = 16.sp) }
            }
            Spacer(Modifier.height(12.dp))
            AnimatedEntrance(7) {
                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = InkIndigo),
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f))
                ) { Text("Настройки и статистика", fontSize = 16.sp) }
            }
        }
    }
}