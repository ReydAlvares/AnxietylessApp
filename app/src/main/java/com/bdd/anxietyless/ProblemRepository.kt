package com.bdd.anxietyless

import kotlin.math.pow
import kotlin.random.Random


// Средне (2-3 действия)
// Ваш список статических примеров (оставляем как есть, но применим к нему 30% урезание времени)
val rawBaseProblems = listOf(

    // ==========================================
    // ТЕМА: ЧИСЛА (NUMBERS)
    // ==========================================
    // Легко (2 действия)
    Problem("2 + 3 * 4", 14.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(5 + 5) * 2", 20.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("15 + 5 - 3", 17.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("20 / 4 + 5", 10.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("8 * 2 - 10", 6.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(12 - 4) / 2", 4.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("6 + 3 * 3", 15.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("18 / 3 + 2", 8.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("4 * 4 - 5", 11.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("30 / 5 + 7", 13.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("2 * 8 - 6", 10.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("(9 + 1) * 3", 30.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("16 - 12 / 3", 12.0, 25, Topic.NUMBERS, Difficulty.EASY),
    Problem("7 * 2 + 4", 18.0, 20, Topic.NUMBERS, Difficulty.EASY),
    Problem("10 - 6 / 2", 7.0, 20, Topic.NUMBERS, Difficulty.EASY),

    Problem("15 * 2 - 10", 20.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("100 / 5 + 12", 32.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("7 * 8 - 6 * 2", 44.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(10 + 5) * 3 - 10", 35.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("24 / 3 + 15 - 5", 18.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(15 - 5) * 4 / 2", 20.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("18 + 12 * 2", 42.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("50 - 20 / 4", 45.0, 35, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(8 + 4) * (10 - 7)", 36.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("45 / 5 + 3 * 4", 21.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("60 - (15 * 2) + 5", 35.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(20 + 10) / 3 + 5", 15.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("100 - 15 * 4", 40.0, 40, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("3 * 15 - 10 + 5", 40.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),
    Problem("(12 / 3) * 7 - 14", 14.0, 45, Topic.NUMBERS, Difficulty.MEDIUM),

// Сложно (3 действия)
    Problem("(12 + 8) * 3 - 15", 45.0, 60, Topic.NUMBERS, Difficulty.HARD),
    Problem("50 / 2 * 3 + 10", 85.0, 60, Topic.NUMBERS, Difficulty.HARD),
    Problem("100 - (25 * 2) / 5", 90.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("15 * 4 + 12 / 3 - 5", 59.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(80 / 4) + 15 * 2 - 10", 40.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("(5 * 6 + 10) / 4", 10.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("200 - (50 / 2) * 3", 125.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(14 + 16) * 2 - 30 / 3", 50.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("25 * 3 + (40 / 8) - 5", 75.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("(100 - 20) / 4 + 15 * 2", 50.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("36 / 6 * 7 - 12 + 8", 38.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("(90 / 3) + 15 * 3 - 10", 65.0, 65, Topic.NUMBERS, Difficulty.HARD),
    Problem("12 * (5 + 5) - 20 / 2", 110.0, 75, Topic.NUMBERS, Difficulty.HARD),
    Problem("(60 - 15) / 5 * 4 + 8", 44.0, 70, Topic.NUMBERS, Difficulty.HARD),
    Problem("150 - (30 * 4) + 50 / 2", 55.0, 70, Topic.NUMBERS, Difficulty.HARD),

// ==========================================
// ТЕМА: ДРОБИ (FRACTIONS)
// ==========================================
// Легко (2 действия)
    Problem("1/2 + 1/4", 0.75, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/4 - 1/2", 0.25, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 * 1/2", 0.25, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/5 + 2/5", 0.6, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/5 - 1/5", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/10 + 3/10", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("5/8 - 1/8", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 + 1/8", 0.625, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("3/4 - 1/4", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("2/5 + 1/5", 0.6, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/4 * 2/1", 0.5, 25, Topic.FRACTIONS, Difficulty.EASY),
    Problem("7/10 - 2/10", 0.5, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 + 3/8", 0.875, 30, Topic.FRACTIONS, Difficulty.EASY),
    Problem("4/5 - 2/5", 0.4, 20, Topic.FRACTIONS, Difficulty.EASY),
    Problem("1/2 * 1/5", 0.1, 25, Topic.FRACTIONS, Difficulty.EASY),

// Средне (2-3 действия)
    Problem("1/2 + 1/4 * 2", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/4 - 1/2) * 4", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 * 3/4 + 1/8", 0.5, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/5 + 3/5) * 2", 1.6, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("3/4 / 2 + 1/4", 0.625, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/2 + 1/2) * 3/4", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/10 + 2/5 * 2", 0.9, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("5/8 - 1/4 * 2", 0.125, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(7/10 - 1/2) * 5", 1.0, 55, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2/3 * 3/4 * 1/2", 0.25, 60, Topic.FRACTIONS, Difficulty.MEDIUM), // Все сокращается
    Problem("(1/4 + 1/4) / 1/2", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1 - 1/2 * 1/2", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/5 - 1/5) * 2", 0.8, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 / 2 + 3/4", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2 * 1/8 + 1/4", 0.5, 45, Topic.FRACTIONS, Difficulty.MEDIUM),

// Сложно (3 действия)
    Problem("(1/2 + 1/4) / 3/4", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("2/5 * 3/4 + 1/10", 0.4, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(5/8 - 1/2) * 4 + 1", 1.5, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/2 + 1/3 * 3/4", 0.75, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(1 - 1/5) / 2/5", 2.0, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("3/4 * 2/3 - 1/4 + 1/2", 0.75, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(1/2 + 1/8) * 2 - 1/4", 1.0, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/10 / 1/2 + 3/5 * 2", 1.4, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(7/8 - 3/8) / 1/2", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("2/5 * (1/2 + 1/4)", 0.3, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1/2 * 2/3 * 3/4 + 1/4", 0.5, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(3/4 + 1/4) / 1/2 - 1", 1.0, 65, Topic.FRACTIONS, Difficulty.HARD),
    Problem("5/6 * 3/5 + 1/2", 1.0, 70, Topic.FRACTIONS, Difficulty.HARD), // 5/6 * 3/5 = 3/6 = 1/2 + 1/2 = 1
    Problem("1 - 1/2 / 1/4 + 2", 1.0, 75, Topic.FRACTIONS, Difficulty.HARD), // 1 - (0.5/0.25) + 2 = 1 - 2 + 2 = 1
    Problem("(2/3 + 1/3) * 3/4 - 1/4", 0.5, 70, Topic.FRACTIONS, Difficulty.HARD),

// ==========================================
// ТЕМА: КОРНИ (ROOTS)
// ==========================================
// Легко (2 действия)
    Problem("√4 + √9", 5.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√16 - √4", 2.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√25 * 2", 10.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√36 / 3", 2.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√49 + 1", 8.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√100 - 5", 5.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("3 + √9", 6.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("10 - √16", 6.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√81 / 9", 1.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√64 / 2", 4.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√121 - 10", 1.0, 20, Topic.ROOTS, Difficulty.EASY),
    Problem("√144 / 6", 2.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√9 * √4", 6.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√25 + √16", 9.0, 25, Topic.ROOTS, Difficulty.EASY),
    Problem("√64 - √36", 2.0, 25, Topic.ROOTS, Difficulty.EASY),

// Средне (2-3 действия)
    Problem("√9 + √16 - √4", 5.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 / √25 + 5", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√4 + √9) * 2", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√49 * √4 + 3", 17.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√81 / 3 + √16", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√121 - √49 + 2", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√144 / √4 - 3", 3.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("2 * √25 - √9", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√(16 + 9)", 5.0, 50, Topic.ROOTS, Difficulty.MEDIUM), // Внимание: корень из суммы
    Problem("√64 + √36 - 10", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√36 / √9) + 10", 12.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 * 3 - 20", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√225 / 5 + 3", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√16 * √9 - 5", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√49 + 1) / 2", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),

// Сложно (3 действия)
    Problem("(√16 + √9) * √4", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√144 / √4 - √9", 3.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("(√25 * √4) + √16", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√81 / √9 + √49 - 5", 5.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√100 + √25) / 3", 5.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√36 * √4 - √16 / 2", 10.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√121 - √4) * 2", 18.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√64 / 2 * √9 - 10", 14.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√169 + 1) / √49", 2.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√196 - √25 * 2 + 3", 7.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("(√2 + √2) / √8", 1.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√50 / √2 * 3", 15.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√3 * √3) + √16 * 2", 11.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√(25 * 4) + √9", 13.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√225 / √25) * √4 - 2", 4.0, 70, Topic.ROOTS, Difficulty.HARD),

// ==========================================
// ТЕМА: ТРИГОНОМЕТРИЯ (TRIGONOMETRY)
// ==========================================
// Легко (2 действия)
    Problem("sin(30°) + cos(60°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) * cos(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) + sin(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) - sin(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(30°) * cos(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(0°) - cos(60°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) - tan(45°)", 0.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) * sin(30°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) + sin(30°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(0°) + cos(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(45°) - cos(60°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(90°) * tan(45°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(0°) * sin(30°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(30°) + sin(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(60°) * cos(0°)", 0.5, 25, Topic.TRIGONOMETRY, Difficulty.EASY),

// Средне (2-3 действия)
    Problem("sin(30°) * cos(60°) + cos(0°)", 1.25, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) / tan(45°) - cos(60°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("(sin(30°) + cos(60°)) * 2", 2.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(0°) + sin(30°) - cos(60°)", 1.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) * cos(0°) / tan(45°)", 1.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("2 * sin(30°) + cos(0°)", 2.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(0°) / 2 + sin(30°)", 1.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) - cos(60°) * 2", 0.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("tan(45°) * cos(60°) + 1", 1.5, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(30°) * 3 - cos(0°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("(cos(0°) + cos(60°)) * 2", 3.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(90°) / 2 + cos(0°)", 1.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(60°) + sin(30°) * 2", 1.5, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("tan(45°) / 2 + sin(90°)", 1.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(30°) + cos(0°) - tan(45°)", 0.5, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),

// Сложно (3 действия)
    Problem("(sin(90°) - cos(0°) + tan(45°)) * 2", 2.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) * cos(60°) + cos(60°) * cos(0°)", 0.75, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(cos(0°) + sin(30°)) / tan(45°)", 1.5, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(90°) * tan(45°) - cos(0°) + 2", 2.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(60°) * 4 - sin(30°) / 0.5", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(30°) + sin(90°)) * cos(60°)", 0.75, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("tan(45°) * 2 + cos(0°) - 3", 0.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(90°) / (cos(0°) * tan(45°)) + 1", 2.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(cos(0°) - cos(60°)) * sin(90°)", 0.5, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) * 2 + cos(0°) / 2", 1.5, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(90°) + cos(0°)) * (tan(45°) - 0.5)", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(30°) / 0.5 + cos(60°) * 2", 2.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(tan(45°) + cos(0°)) / 2 - sin(0°)", 1.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(0°) * 3 - sin(30°) * 4", 1.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("(sin(90°) * cos(60°) + tan(45°)) / 1.5", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD)
)

// Урезаем время на 30% для всех статических примеров (умножаем на 0.7)
val baseProblems = rawBaseProblems.map { it.copy(timeLimit = (it.timeLimit * 0.7).toInt().coerceAtLeast(5)) }

val fastProblems = baseProblems
    .filter { it.difficulty == Difficulty.HARD }
    .map { it.copy(difficulty = Difficulty.FAST, timeLimit = (it.timeLimit / 1.5).toInt()) }

val allStaticProblems = baseProblems + fastProblems

fun getNextProblem(current: Problem, list: List<Problem>): Problem {
    if (list.size <= 1) return list.first()
    var next = list.random()
    while (next == current) {
        next = list.random()
    }
    return next
}

// Вспомогательный класс для тригонометрии
data class TrigTerm(val text: String, val answer: Double)

// НОВОЕ: Вспомогательный класс для корней
data class RootTerm(val text: String, val answer: Double)

// НОВОЕ: Вспомогательная функция для генерации простых математических операций
fun getSimpleOp(allowMult: Boolean): Pair<String, Double> {
    val a = Random.nextInt(2, 15)
    val b = Random.nextInt(2, 15)
    val op = if (allowMult) listOf("+", "-", "*").random() else listOf("+", "-").random()
    val ans = when(op) {
        "+" -> (a + b).toDouble()
        "-" -> (a - b).toDouble()
        else -> (a * b).toDouble()
    }
    return Pair("$a$op$b", ans) // Пишем без пробелов, чтобы не ломать верстку дроби
}

fun getRandomTrigTerm(diff: Difficulty): TrigTerm {
    val terms = mutableListOf<TrigTerm>()
    terms.add(TrigTerm("sin(0°)", 0.0))
    terms.add(TrigTerm("cos(0°)", 1.0))
    terms.add(TrigTerm("sin(30°)", 0.5))
    terms.add(TrigTerm("sin(π/6)", 0.5))
    terms.add(TrigTerm("cos(60°)", 0.5))
    terms.add(TrigTerm("cos(π/3)", 0.5))
    terms.add(TrigTerm("tan(45°)", 1.0))
    terms.add(TrigTerm("tan(π/4)", 1.0))

    if (diff != Difficulty.EASY) {
        terms.add(TrigTerm("sin(45°)", 0.707))
        terms.add(TrigTerm("sin(π/4)", 0.707))
        terms.add(TrigTerm("cos(45°)", 0.707))
        terms.add(TrigTerm("cos(π/4)", 0.707))
        terms.add(TrigTerm("tan(30°)", 0.577))
        terms.add(TrigTerm("cot(45°)", 1.0))
    }
    return terms.random()
}

fun getRandomRootTerm(): RootTerm {
    val r = Random.nextInt(2, 16)
    return RootTerm("√${r*r}", r.toDouble())
}

// Умный генератор
fun generateRandomProblem(selectedTopics: Set<Topic>, diff: Difficulty): Problem {
    val baseTime = when (diff) {
        Difficulty.EASY -> 20; Difficulty.MEDIUM -> 35; Difficulty.HARD -> 50; Difficulty.FAST -> 25
    }
    val timeLimit = (baseTime * 0.7).toInt().coerceAtLeast(5)

    if (selectedTopics.size >= 2 && Random.nextInt(100) < 60) {
        return generateHybridProblem(selectedTopics, diff, timeLimit)
    }
    val topic = selectedTopics.random()
    return generateSingleTopicProblem(topic, diff, timeLimit)
}

fun generateSingleTopicProblem(topic: Topic, diff: Difficulty, timeLimit: Int): Problem {
    return when (topic) {
        Topic.NUMBERS -> {
            when (diff) {
                Difficulty.EASY -> {
                    val a = Random.nextInt(2, 20); val b = Random.nextInt(2, 20)
                    val action = listOf("+", "-", "*").random()
                    val ans = when(action) { "+" -> a + b; "-" -> a - b; else -> a * b }
                    Problem("$a $action $b", ans.toDouble(), timeLimit, topic, diff)
                }
                Difficulty.MEDIUM -> {
                    val a = Random.nextInt(10, 100); val b = Random.nextInt(10, 100)
                    val action = listOf("+", "-").random()
                    Problem("$a $action $b", if(action=="+") (a+b).toDouble() else (a-b).toDouble(), timeLimit, topic, diff)
                }
                Difficulty.HARD, Difficulty.FAST -> {
                    // НОВАЯ ЛОГИКА ДЛЯ ТРЕХЗНАЧНЫХ ЧИСЕЛ
                    // 1. Единицы, которые в сумме дают 10 (например, 3 и 7)
                    val aUnits = Random.nextInt(1, 10)
                    val bUnits = 10 - aUnits

                    // 2. Сотни и десятки - всегда значащие (от 1 до 9)
                    val a = Random.nextInt(1, 10) * 100 + Random.nextInt(1, 10) * 10 + aUnits
                    val b = Random.nextInt(1, 10) * 100 + Random.nextInt(1, 10) * 10 + bUnits

                    val op = listOf("+", "-").random()
                    val mult = Random.nextInt(2, 6)

                    val innerAns = if (op == "+") a + b else a - b
                    val text = "($a $op $b) * $mult"

                    Problem(text, (innerAns * mult).toDouble(), timeLimit, topic, diff)
                }
            }
        }
        Topic.ROOTS -> {
            val rootBase = Random.nextInt(2, 21); val square = rootBase.toDouble().pow(2).toInt()
            when (diff) {
                Difficulty.EASY -> Problem("√$square", rootBase.toDouble(), timeLimit, topic, diff)
                Difficulty.MEDIUM -> {
                    val extra = Random.nextInt(1, 10)
                    Problem("√$square + $extra", (rootBase + extra).toDouble(), timeLimit, topic, diff)
                }
                Difficulty.HARD, Difficulty.FAST -> {
                    val root2 = Random.nextInt(2, 11); val square2 = root2.toDouble().pow(2).toInt()
                    Problem("√$square + √$square2", (rootBase + root2).toDouble(), timeLimit, topic, diff)
                }
            }
        }
        // === ОБНОВЛЕННЫЕ ДРОБИ ===
        Topic.FRACTIONS -> {
            val denominators = listOf(2, 4, 5, 8, 10)
            val d = denominators.random()

            when (diff) {
                Difficulty.EASY -> {
                    val n = Random.nextInt(1, d)
                    Problem("$n/$d", (n.toDouble() / d), timeLimit, topic, diff)
                }
                Difficulty.MEDIUM -> {
                    // Пример: (2+3)/5 или 2/5 + 3/5
                    if (Random.nextBoolean()) {
                        val n1 = Random.nextInt(1, d)
                        val n2 = Random.nextInt(1, d)
                        val op = listOf("+", "-").random()
                        val (text, ans) = if (op == "+") Pair("($n1+$n2)/$d", (n1 + n2).toDouble() / d) else Pair("($n1-$n2)/$d", (n1 - n2).toDouble() / d)
                        Problem(text, ans, timeLimit, topic, diff)
                    } else {
                        val n1 = Random.nextInt(1, d)
                        val n2 = Random.nextInt(1, d)
                        Problem("$n1/$d + $n2/$d", (n1 + n2).toDouble() / d, timeLimit, topic, diff)
                    }
                }
                Difficulty.HARD, Difficulty.FAST -> {
                    // Сложные дроби: (a*b)/c + d/e
                    val n1 = Random.nextInt(2, 10)
                    val n2 = Random.nextInt(2, 5)
                    val op = listOf("*", "+", "-").random()
                    val numText = if (op == "*") "$n1*$n2" else "$n1$op$n2"
                    val numAns = if (op == "*") (n1 * n2).toDouble() else if (op == "+") (n1 + n2).toDouble() else (n1 - n2).toDouble()

                    Problem("($numText)/$d + ${Random.nextInt(1, d)}/$d", (numAns / d) + (Random.nextInt(1, d).toDouble() / d), timeLimit, topic, diff)
                }
            }
        }
        Topic.TRIGONOMETRY -> {
            val term1 = getRandomTrigTerm(diff)
            if (diff == Difficulty.EASY) {
                Problem(term1.text, term1.answer, timeLimit, topic, diff)
            } else {
                val term2 = getRandomTrigTerm(diff)
                Problem("${term1.text} + ${term2.text}", term1.answer + term2.answer, timeLimit, topic, diff)
            }
        }
    }
}

// === ОБНОВЛЕННЫЕ ГИБРИДЫ ===
fun generateHybridProblem(selectedTopics: Set<Topic>, diff: Difficulty, timeLimit: Int): Problem {
    val topics = selectedTopics.toList()
    val t1 = topics.random()
    var t2 = topics.random()
    while (t1 == t2) { t2 = topics.random() }
    val pair = setOf(t1, t2)

    return when {
        // 1. ДРОБИ + ТРИГОНОМЕТРИЯ: (sin+cos)/2
        pair == setOf(Topic.FRACTIONS, Topic.TRIGONOMETRY) -> {
            val t1 = getRandomTrigTerm(Difficulty.EASY)
            val t2 = getRandomTrigTerm(Difficulty.EASY)
            val d = listOf(2, 4, 5).random()
            val op = listOf("+", "-").random()
            val ans = if (op == "+") (t1.answer + t2.answer) / d else (t1.answer - t2.answer) / d
            Problem("(${t1.text}$op${t2.text})/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        // 2. ДРОБИ + КОРНИ: (√4+√9)/5
        pair == setOf(Topic.FRACTIONS, Topic.ROOTS) -> {
            val r1 = getRandomRootTerm()
            val r2 = getRandomRootTerm()
            val d = listOf(2, 4, 5).random()
            val op = listOf("+", "-").random()
            val ans = if (op == "+") (r1.answer + r2.answer) / d else (r1.answer - r2.answer) / d
            Problem("(${r1.text}$op${r2.text})/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        // 3. ДРОБИ + ЧИСЛА: (10+5)/3
        pair == setOf(Topic.FRACTIONS, Topic.NUMBERS) -> {
            val num1 = Random.nextInt(10, 50)
            val num2 = Random.nextInt(1, 20)
            val d = listOf(2, 4, 5).random()
            val op = listOf("+", "-", "*").random()
            val ans = if (op == "+") (num1 + num2).toDouble() / d else if (op == "-") (num1 - num2).toDouble() / d else (num1 * num2).toDouble() / d
            Problem("($num1$op$num2)/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        // 4. КОРНИ + ТРИГОНОМЕТРИЯ: √x + sin(...)
        pair == setOf(Topic.ROOTS, Topic.TRIGONOMETRY) -> {
            val r = getRandomRootTerm()
            val t = getRandomTrigTerm(Difficulty.EASY)
            Problem("${r.text} + ${t.text}", r.answer + t.answer, timeLimit, Topic.TRIGONOMETRY, diff)
        }
        // 5. ЧИСЛА + ТРИГОНОМЕТРИЯ: 5 * cos(...)
        pair == setOf(Topic.NUMBERS, Topic.TRIGONOMETRY) -> {
            val mult = Random.nextInt(2, 5)
            val t = getRandomTrigTerm(Difficulty.EASY)
            Problem("$mult * ${t.text}", mult * t.answer, timeLimit, Topic.TRIGONOMETRY, diff)
        }
        // 6. КОРНИ + ЧИСЛА: √25 * 4 + 10
        pair == setOf(Topic.ROOTS, Topic.NUMBERS) -> {
            val r = getRandomRootTerm()
            val mult = Random.nextInt(2, 5); val extra = Random.nextInt(1, 20)
            Problem("${r.text} * $mult + $extra", (r.answer * mult + extra), timeLimit, Topic.ROOTS, diff)
        }
        else -> generateSingleTopicProblem(t1, diff, timeLimit) // Fallback
    }
}