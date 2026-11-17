package com.bdd.anxietyless

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateMapOf

object StatsManager {
    private val totalTime = mutableStateMapOf<Difficulty, Long>()
    private val answerCount = mutableStateMapOf<Difficulty, Int>()
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs != null) return
        val p = context.applicationContext.getSharedPreferences("AnxietylessStats", Context.MODE_PRIVATE)
        Difficulty.values().forEach { d ->
            totalTime[d] = p.getLong("time_${d.name}", 0L)
            answerCount[d] = p.getInt("count_${d.name}", 0)
        }
        prefs = p
    }

    fun recordTime(difficulty: Difficulty, timeMs: Long) {
        val t = (totalTime[difficulty] ?: 0L) + timeMs
        val c = (answerCount[difficulty] ?: 0) + 1
        totalTime[difficulty] = t
        answerCount[difficulty] = c
        prefs?.edit()?.putLong("time_${difficulty.name}", t)?.putInt("count_${difficulty.name}", c)?.apply()
    }

    fun getAverageTimeSeconds(difficulty: Difficulty): Float {
        val count = answerCount[difficulty] ?: 0
        if (count == 0) return 0f
        return ((totalTime[difficulty] ?: 0L).toFloat() / count) / 1000f
    }
}