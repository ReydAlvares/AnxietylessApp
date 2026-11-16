package com.bdd.anxietyless

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartScreen(
    onStart: (Set<Topic>, Difficulty, Boolean) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTopics by remember { mutableStateOf(setOf(Topic.NUMBERS)) }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var useRandom by remember { mutableStateOf(true) }

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

        Spacer(modifier = Modifier.height(16.dp))

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

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Генерировать случайные примеры", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = useRandom, onCheckedChange = { useRandom = it })
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onStart(selectedTopics, selectedDifficulty, useRandom) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = selectedTopics.isNotEmpty()
        ) {
            Text("Начать игру", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Настройки ускорения", fontSize = 16.sp)
        }
    }
}