package com.bdd.anxietyless

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun SettingsScreen(
    currentMinAccel: Float,
    onSave: (Float) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember { mutableStateOf(currentMinAccel.toString()) }
    var warningMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Настройки", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        // --- БЛОК СТАТИСТИКИ ---
        Text("Среднее время на ответ:", fontSize = 18.sp, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(8.dp))

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Difficulty.values().forEach { diff ->
                    val avg = StatsManager.getAverageTimeSeconds(diff)
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(diff.displayName, fontSize = 16.sp)
                        Text(
                            text = if (avg > 0) "${String.format(Locale.US, "%.1f", avg)} сек" else "Нет данных",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- БЛОК НАСТРОЙКИ УСКОРЕНИЯ ---
        Text(
            text = "Мин. коэффициент ускорения (0.1 - 1.0):",
            fontSize = 16.sp,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                warningMessage = null
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        warningMessage?.let {
            Text(text = it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val parsed = textValue.replace(',', '.').toFloatOrNull()
                if (parsed == null || parsed < 0.1f) {
                    onSave(0.5f)
                    textValue = "0.5"
                    warningMessage = "Значение < 0.1! Установлено: 0.5"
                } else if (parsed > 1.0f) {
                    onSave(1.0f)
                    textValue = "1.0"
                    warningMessage = "Максимум 1.0 (без ускорения)."
                } else {
                    onSave(parsed)
                    warningMessage = "Сохранено!"
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Сохранить", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Назад", fontSize = 18.sp)
        }
    }
}