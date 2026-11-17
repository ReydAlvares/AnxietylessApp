package com.bdd.anxietyless

enum class Topic(val displayName: String) {
    NUMBERS("Числа"),
    DECIMALS("Десятичные числа"),
    FRACTIONS("Дроби"),
    ROOTS("Корни"),
    TRIGONOMETRY("Тригонометрия"),
    TABLES("Таблицы × и ²")
}

enum class Difficulty(val displayName: String) {
    EASY("Легко"), MEDIUM("Средне"), HARD("Сложно"), FAST("Быстро")
}

data class Problem(
    val text: String,
    val answer: Double,
    val timeLimit: Int,
    val topic: Topic,
    val difficulty: Difficulty
)