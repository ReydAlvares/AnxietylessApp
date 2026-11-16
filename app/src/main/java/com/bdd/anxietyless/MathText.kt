package com.bdd.anxietyless

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MathText(text: String, fontSize: TextUnit, modifier: Modifier = Modifier) {
    val parts = text.split(" ")
    val adaptiveFontSize = when {
        text.length > 25 -> fontSize * 0.5f
        text.length > 18 -> fontSize * 0.7f
        else -> fontSize
    }

    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        parts.forEachIndexed { index, part ->
            // Если это настоящая дробь (слэш не спрятан внутри скобок), рисуем вертикально
            if (isTopLevelFraction(part)) {
                val split = part.split("/")
                val num = split[0].removePrefix("(").removeSuffix(")")
                val den = split[1].removePrefix("(").removeSuffix(")")

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    Text(num, fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                            .height(3.dp)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                    Text(den, fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
                }
            } else {
                // Если это cos(π/4), sin(30°) или просто текст, выводим в строку
                Text(part, fontSize = adaptiveFontSize, fontWeight = FontWeight.Bold)
            }

            if (index < parts.size - 1) {
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

// НОВАЯ ФУНКЦИЯ: Проверяет, является ли слэш настоящим знаком деления (не внутри скобок)
fun isTopLevelFraction(s: String): Boolean {
    var depth = 0
    for (c in s) {
        if (c == '(') depth++
        if (c == ')') depth--
        if (c == '/' && depth == 0) return true // Слэш найден вне скобок
    }
    return false
}