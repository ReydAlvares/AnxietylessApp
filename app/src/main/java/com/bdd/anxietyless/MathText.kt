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
    val adaptiveFontSize = when {
        text.length > 25 -> fontSize * 0.5f
        text.length > 18 -> fontSize * 0.7f
        else -> fontSize
    }
    val tokens = tokenizeTopLevel(text)
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        itemVerticalAlignment = Alignment.CenterVertically
    ) {
        tokens.forEachIndexed { index, token ->
            MathToken(token, adaptiveFontSize)
            if (index < tokens.size - 1) Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
private fun MathToken(token: String, fontSize: TextUnit) {
    val frac = splitTopLevelFraction(token)
    if (frac != null && frac.first.isNotEmpty() && frac.second.isNotEmpty()
        && !hasTopLevelOps(frac.first) && !hasTopLevelOps(frac.second)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            MathToken(stripOuterParens(frac.first), fontSize)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
            MathToken(stripOuterParens(frac.second), fontSize)
        }
    } else if (isFullyWrapped(token)) {
        val inner = tokenizeTopLevel(token.substring(1, token.length - 1))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("(", fontSize = fontSize, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            inner.forEachIndexed { i, t ->
                MathToken(t, fontSize)
                if (i < inner.size - 1) Spacer(modifier = Modifier.width(8.dp))
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(")", fontSize = fontSize, fontWeight = FontWeight.Bold)
        }
    } else {
        Text(
            text = when (token) { "/" -> "÷"; "*" -> "×"; else -> token },
            fontSize = fontSize, fontWeight = FontWeight.Bold
        )
    }
}

private fun tokenizeTopLevel(text: String): List<String> {
    val tokens = mutableListOf<String>()
    val cur = StringBuilder()
    var depth = 0
    for (c in text) {
        when {
            c == '(' -> { depth++; cur.append(c) }
            c == ')' -> { depth--; cur.append(c) }
            c == ' ' && depth == 0 -> {
                if (cur.isNotEmpty()) { tokens.add(cur.toString()); cur.clear() }
            }
            else -> cur.append(c)
        }
    }
    if (cur.isNotEmpty()) tokens.add(cur.toString())
    return tokens
}

fun splitTopLevelFraction(s: String): Pair<String, String>? {
    var depth = 0
    for (i in s.indices) {
        val c = s[i]
        if (c == '(') depth++
        if (c == ')') depth--
        if (c == '/' && depth == 0) return Pair(s.substring(0, i), s.substring(i + 1))
    }
    return null
}

private fun hasTopLevelOps(s: String): Boolean {
    var depth = 0
    for (c in s) {
        when (c) {
            '(' -> depth++
            ')' -> depth--
            '+', '-', '*', '×' -> if (depth == 0) return true
        }
    }
    return false
}

private fun isFullyWrapped(s: String): Boolean {
    if (!s.startsWith("(") || !s.endsWith(")")) return false
    var depth = 0
    for (i in s.indices) {
        if (s[i] == '(') depth++
        if (s[i] == ')') depth--
        if (depth == 0 && i < s.length - 1) return false
    }
    return depth == 0
}

private fun stripOuterParens(s: String): String =
    if (isFullyWrapped(s)) s.substring(1, s.length - 1) else s