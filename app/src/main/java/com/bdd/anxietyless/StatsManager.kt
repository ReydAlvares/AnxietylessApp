package com.bdd.anxietyless

import androidx.compose.runtime.mutableStateMapOf

object StatsManager {
    // Храним общее время и количество ответов для каждого уровня сложности
    private val totalTime = mutableStateMapOf<Difficulty, Long>()
    private val answerCount = mutableStateMapOf<Difficulty, Int>()

    fun recordTime(difficulty: Difficulty, timeMs: Long) {
        totalTime[difficulty] = (totalTime[difficulty] ?: 0L) + timeMs
        answerCount[difficulty] = (answerCount[difficulty] ?: 0) + 1
    }

    fun getAverageTimeSeconds(difficulty: Difficulty): Float {
        val count = answerCount[difficulty] ?: 0
        if (count == 0) return 0f
        val total = totalTime[difficulty] ?: 0L
        return (total.toFloat() / count) / 1000f
    }
}