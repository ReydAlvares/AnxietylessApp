package com.bdd.anxietyless

import kotlin.math.abs
import kotlin.math.pow
import kotlin.random.Random

val rawBaseProblems = listOf(
    // ЧИСЛА
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
    // ДРОБИ
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
    Problem("1/2 + 1/4 * 2", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/4 - 1/2) * 4", 1.0, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 * 3/4 + 1/8", 0.5, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/5 + 3/5) * 2", 1.6, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("3/4 / 2 + 1/4", 0.625, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/2 + 1/2) * 3/4", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/10 + 2/5 * 2", 0.9, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("5/8 - 1/4 * 2", 0.125, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(7/10 - 1/2) * 5", 1.0, 55, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2/3 * 3/4 * 1/2", 0.25, 60, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(1/4 + 1/4) / 1/2", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1 - 1/2 * 1/2", 0.75, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("(3/5 - 1/5) * 2", 0.8, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("1/2 / 2 + 3/4", 1.0, 50, Topic.FRACTIONS, Difficulty.MEDIUM),
    Problem("2 * 1/8 + 1/4", 0.5, 45, Topic.FRACTIONS, Difficulty.MEDIUM),
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
    Problem("5/6 * 3/5 + 1/2", 1.0, 70, Topic.FRACTIONS, Difficulty.HARD),
    Problem("1 - 1/2 / 1/4 + 2", 1.0, 75, Topic.FRACTIONS, Difficulty.HARD),
    Problem("(2/3 + 1/3) * 3/4 - 1/4", 0.5, 70, Topic.FRACTIONS, Difficulty.HARD),
    // КОРНИ
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
    Problem("√9 + √16 - √4", 5.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 / √25 + 5", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√4 + √9) * 2", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√49 * √4 + 3", 17.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√81 / 3 + √16", 7.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√121 - √49 + 2", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√144 / √4 - 3", 3.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("2 * √25 - √9", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√(16 + 9)", 5.0, 50, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√64 + √36 - 10", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√36 / √9) + 10", 12.0, 40, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√100 * 3 - 20", 10.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√225 / 5 + 3", 6.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("√16 * √9 - 5", 7.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√49 + 1) / 2", 4.0, 45, Topic.ROOTS, Difficulty.MEDIUM),
    Problem("(√16 + √9) * √4", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√144 / √4 - √9", 3.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("(√25 * √4) + √16", 14.0, 60, Topic.ROOTS, Difficulty.HARD),
    Problem("√81 / √9 + √49 - 5", 5.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√100 + √25) / 3", 5.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√36 * √4 - √16 / 2", 10.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√121 - √4) * 2", 18.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√64 / 2 * √9 - 10", 2.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√169 + 1) / √49", 2.0, 65, Topic.ROOTS, Difficulty.HARD),
    Problem("√196 - √25 * 2 + 3", 7.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("(√2 + √2) / √8", 1.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√50 / √2 * 3", 15.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√3 * √3) + √16 * 2", 11.0, 75, Topic.ROOTS, Difficulty.HARD),
    Problem("√(25 * 4) + √9", 13.0, 70, Topic.ROOTS, Difficulty.HARD),
    Problem("(√225 / √25) * √4 - 2", 4.0, 70, Topic.ROOTS, Difficulty.HARD),
    // ТРИГОНОМЕТРИЯ
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
    Problem("sin(180°) + cos(0°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(180°) + 2", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("sin(270°) + 1", 0.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("cos(360°) * 5", 5.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
    Problem("tan(180°) + sin(90°)", 1.0, 25, Topic.TRIGONOMETRY, Difficulty.EASY),
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
    Problem("sin(150°) + cos(120°)", 0.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(120°) * 4", -2.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("sin(210°) + cos(240°)", -1.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("tan(135°) + 2", 1.0, 45, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
    Problem("cos(300°) + sin(330°)", 0.0, 50, Topic.TRIGONOMETRY, Difficulty.MEDIUM),
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
    Problem("(sin(90°) * cos(60°) + tan(45°)) / 1.5", 1.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(390°) + cos(420°)", 1.0, 65, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(-60°) + sin(-30°)", 0.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("tan(225°) * 3 - 4", -1.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("sin(480°) + cos(510°)", 0.0, 75, Topic.TRIGONOMETRY, Difficulty.HARD),
    Problem("cos(540°) - sin(270°)", 0.0, 70, Topic.TRIGONOMETRY, Difficulty.HARD)
)

// ДЕСЯТИЧНЫЕ ЧИСЛА: десятые и сотые вперемешку с обычными числами
val decimalProblems = listOf(
    Problem("0.5 + 0.5", 1.0, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.3 + 0.4", 0.7, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.9 - 0.4", 0.5, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("1.5 + 2", 3.5, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("3 - 0.5", 2.5, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.2 * 3", 0.6, 25, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.5 * 4", 2.0, 25, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.8 / 2", 0.4, 25, Topic.DECIMALS, Difficulty.EASY),
    Problem("1.2 / 2", 0.6, 25, Topic.DECIMALS, Difficulty.EASY),
    Problem("2.5 + 1.5", 4.0, 20, Topic.DECIMALS, Difficulty.EASY),
    Problem("0.25 + 0.75", 1.0, 35, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("1.25 - 0.5", 0.75, 35, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.1 * 0.5", 0.05, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.2 * 0.3", 0.06, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("1.5 * 2 + 0.5", 3.5, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.5 + 0.25 * 2", 1.0, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("2.4 / 2 + 0.8", 2.0, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.9 - 0.3 * 2", 0.3, 40, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("1.2 + 0.8 - 0.5", 1.5, 35, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.75 / 0.25", 3.0, 45, Topic.DECIMALS, Difficulty.MEDIUM),
    Problem("0.5 * 0.4 + 0.8", 1.0, 60, Topic.DECIMALS, Difficulty.HARD),
    Problem("1.25 * 4 - 2.5", 2.5, 60, Topic.DECIMALS, Difficulty.HARD),
    Problem("0.3 * 0.3 + 0.01", 0.1, 65, Topic.DECIMALS, Difficulty.HARD),
    Problem("2.5 * 0.4 * 3", 3.0, 60, Topic.DECIMALS, Difficulty.HARD),
    Problem("0.72 / 0.8 + 0.1", 1.0, 65, Topic.DECIMALS, Difficulty.HARD),
    Problem("1.5 + 0.5 * 0.6", 1.8, 60, Topic.DECIMALS, Difficulty.HARD),
    Problem("3.6 / 1.2 - 0.5", 2.5, 65, Topic.DECIMALS, Difficulty.HARD),
    Problem("0.125 * 8 + 0.5", 1.5, 70, Topic.DECIMALS, Difficulty.HARD),
    Problem("2 - 0.25 * 4 + 0.5", 1.5, 65, Topic.DECIMALS, Difficulty.HARD),
    Problem("0.6 * 0.5 / 0.3", 1.0, 70, Topic.DECIMALS, Difficulty.HARD)
)

// ТАБЛИЦЫ: настоящая таблица умножения и таблица квадратов
val tableProblems = buildList {
    for (a in 2..9) for (b in a..9) add(Problem("$a × $b", (a * b).toDouble(), 12, Topic.TABLES, Difficulty.EASY))
    for (n in 2..12) add(Problem("$n²", (n * n).toDouble(), 12, Topic.TABLES, Difficulty.EASY))
    for (a in 2..12) for (b in 11..12) add(Problem("$a × $b", (a * b).toDouble(), 20, Topic.TABLES, Difficulty.MEDIUM))
    for (n in 13..20) add(Problem("$n²", (n * n).toDouble(), 25, Topic.TABLES, Difficulty.MEDIUM))
    for (a in 12..19) for (b in 3..9) add(Problem("$a × $b", (a * b).toDouble(), 35, Topic.TABLES, Difficulty.HARD))
    for (n in 21..30) add(Problem("$n²", (n * n).toDouble(), 40, Topic.TABLES, Difficulty.HARD))
}

val baseProblems = (rawBaseProblems + decimalProblems + tableProblems)
    .map { it.copy(timeLimit = (it.timeLimit * 0.7).toInt().coerceAtLeast(5)) }

val fastProblems = baseProblems
    .filter { it.difficulty == Difficulty.HARD }
    .map { it.copy(difficulty = Difficulty.FAST, timeLimit = (it.timeLimit / 1.5).toInt()) }

val allStaticProblems = baseProblems + fastProblems

fun getNextProblem(current: Problem, list: List<Problem>): Problem {
    if (list.size <= 1) return list.first()
    var next = list.random()
    while (next == current) { next = list.random() }
    return next
}

data class TrigTerm(val text: String, val answer: Double)
data class RootTerm(val text: String, val answer: Double)

// Точный вывод сотых: 72 → "0.72", 70 → "0.7", 300 → "3"
private fun fmtH(h: Int): String {
    val sign = if (h < 0) "-" else ""
    val a = abs(h)
    val whole = a / 100
    val frac = a % 100
    return when {
        frac == 0 -> "$sign$whole"
        frac % 10 == 0 -> "$sign$whole.${frac / 10}"
        else -> "$sign$whole.${frac.toString().padStart(2, '0')}"
    }
}

fun getRandomTrigTerm(diff: Difficulty): TrigTerm {
    val terms = mutableListOf(
        TrigTerm("sin(0°)", 0.0), TrigTerm("cos(0°)", 1.0), TrigTerm("tan(0°)", 0.0),
        TrigTerm("sin(30°)", 0.5), TrigTerm("sin(π/6)", 0.5),
        TrigTerm("cos(60°)", 0.5), TrigTerm("cos(π/3)", 0.5),
        TrigTerm("tan(45°)", 1.0), TrigTerm("tan(π/4)", 1.0),
        TrigTerm("sin(90°)", 1.0), TrigTerm("cos(90°)", 0.0),
        TrigTerm("sin(180°)", 0.0), TrigTerm("cos(180°)", -1.0), TrigTerm("tan(180°)", 0.0),
        TrigTerm("sin(270°)", -1.0), TrigTerm("cos(270°)", 0.0),
        TrigTerm("sin(360°)", 0.0), TrigTerm("cos(360°)", 1.0), TrigTerm("tan(360°)", 0.0),
        TrigTerm("sin(π)", 0.0), TrigTerm("cos(π)", -1.0),
        TrigTerm("sin(2π)", 0.0), TrigTerm("cos(2π)", 1.0)
    )
    if (diff != Difficulty.EASY) {
        terms.addAll(listOf(
            TrigTerm("sin(120°)", 0.866), TrigTerm("cos(120°)", -0.5),
            TrigTerm("sin(135°)", 0.707), TrigTerm("cos(135°)", -0.707), TrigTerm("tan(135°)", -1.0),
            TrigTerm("sin(150°)", 0.5), TrigTerm("cos(150°)", -0.866), TrigTerm("tan(150°)", -0.577),
            TrigTerm("sin(210°)", -0.5), TrigTerm("cos(210°)", -0.866), TrigTerm("tan(210°)", 0.577),
            TrigTerm("sin(240°)", -0.866), TrigTerm("cos(240°)", -0.5), TrigTerm("tan(240°)", 1.732),
            TrigTerm("sin(300°)", -0.866), TrigTerm("cos(300°)", 0.5), TrigTerm("tan(300°)", -1.732),
            TrigTerm("sin(315°)", -0.707), TrigTerm("cos(315°)", 0.707), TrigTerm("tan(315°)", -1.0),
            TrigTerm("sin(330°)", -0.5), TrigTerm("cos(330°)", 0.866), TrigTerm("tan(330°)", -0.577),
            TrigTerm("sin(45°)", 0.707), TrigTerm("sin(π/4)", 0.707),
            TrigTerm("cos(45°)", 0.707), TrigTerm("cos(π/4)", 0.707),
            TrigTerm("tan(30°)", 0.577), TrigTerm("cot(45°)", 1.0),
            TrigTerm("sin(2π/3)", 0.866), TrigTerm("cos(2π/3)", -0.5),
            TrigTerm("sin(3π/4)", 0.707), TrigTerm("cos(3π/4)", -0.707),
            TrigTerm("sin(5π/6)", 0.5), TrigTerm("cos(5π/6)", -0.866),
            TrigTerm("sin(7π/6)", -0.5), TrigTerm("cos(4π/3)", -0.5),
            TrigTerm("sin(3π/2)", -1.0), TrigTerm("cos(3π/2)", 0.0),
            TrigTerm("sin(5π/3)", -0.866), TrigTerm("cos(5π/3)", 0.5),
            TrigTerm("sin(7π/4)", -0.707), TrigTerm("cos(7π/4)", 0.707),
            TrigTerm("sin(11π/6)", -0.5), TrigTerm("cos(11π/6)", 0.866)
        ))
    }
    if (diff == Difficulty.HARD || diff == Difficulty.FAST) {
        terms.addAll(listOf(
            TrigTerm("sin(390°)", 0.5), TrigTerm("cos(390°)", 0.866),
            TrigTerm("cos(420°)", 0.5), TrigTerm("sin(405°)", 0.707), TrigTerm("tan(405°)", 1.0),
            TrigTerm("sin(480°)", 0.866), TrigTerm("cos(510°)", -0.866),
            TrigTerm("sin(540°)", 0.0), TrigTerm("cos(540°)", -1.0), TrigTerm("tan(225°)", 1.0),
            TrigTerm("sin(-30°)", -0.5), TrigTerm("cos(-60°)", 0.5),
            TrigTerm("tan(-45°)", -1.0), TrigTerm("sin(-90°)", -1.0), TrigTerm("cos(-180°)", -1.0),
            TrigTerm("cot(30°)", 1.732), TrigTerm("cot(60°)", 0.577),
            TrigTerm("cot(120°)", -0.577), TrigTerm("cot(135°)", -1.0),
            TrigTerm("sin(-π/6)", -0.5), TrigTerm("cos(-π/3)", 0.5),
            TrigTerm("tan(5π/4)", 1.0), TrigTerm("sin(13π/6)", 0.5), TrigTerm("cos(7π/3)", 0.5)
        ))
    }
    return terms.random()
}

fun getRandomRootTerm(): RootTerm {
    val r = Random.nextInt(2, 16)
    return RootTerm("√${r * r}", r.toDouble())
}

fun generateRandomProblem(selectedTopics: Set<Topic>, diff: Difficulty): Problem {
    val baseTime = when (diff) {
        Difficulty.EASY -> 20
        Difficulty.MEDIUM -> 35
        Difficulty.HARD -> 50
        Difficulty.FAST -> (50 / 1.5).toInt()
    }
    val timeLimit = (baseTime * 0.7).toInt().coerceAtLeast(5)
    if (selectedTopics.size >= 2 && Random.nextInt(100) < 60) {
        return generateHybridProblem(selectedTopics, diff, timeLimit)
    }
    return generateSingleTopicProblem(selectedTopics.random(), diff, timeLimit)
}

fun generateSingleTopicProblem(topic: Topic, diff: Difficulty, timeLimit: Int): Problem {
    return when (topic) {
        Topic.NUMBERS -> when (diff) {
            Difficulty.EASY -> {
                val a = Random.nextInt(2, 20); val b = Random.nextInt(2, 20)
                val action = listOf("+", "-", "*").random()
                val ans = when (action) { "+" -> a + b; "-" -> a - b; else -> a * b }
                Problem("$a $action $b", ans.toDouble(), timeLimit, topic, diff)
            }
            Difficulty.MEDIUM -> {
                val a = Random.nextInt(10, 100); val b = Random.nextInt(10, 100)
                val action = listOf("+", "-").random()
                Problem("$a $action $b", (if (action == "+") a + b else a - b).toDouble(), timeLimit, topic, diff)
            }
            Difficulty.HARD, Difficulty.FAST -> {
                val aUnits = Random.nextInt(1, 10)
                val bUnits = 10 - aUnits
                val a = Random.nextInt(1, 10) * 100 + Random.nextInt(1, 10) * 10 + aUnits
                val b = Random.nextInt(1, 10) * 100 + Random.nextInt(1, 10) * 10 + bUnits
                val op = listOf("+", "-").random()
                val mult = Random.nextInt(2, 6)
                val innerAns = if (op == "+") a + b else a - b
                Problem("($a $op $b) * $mult", (innerAns * mult).toDouble(), timeLimit, topic, diff)
            }
        }

        Topic.DECIMALS -> when (diff) {
            Difficulty.EASY -> when (Random.nextInt(4)) {
                0 -> {
                    val a = Random.nextInt(1, 100) * 10; val b = Random.nextInt(1, 10) * 100
                    Problem("${fmtH(a)} + ${b / 100}", (a + b) / 100.0, timeLimit, topic, diff)
                }
                1 -> {
                    val whole = Random.nextInt(2, 10)
                    val a = whole * 100 + Random.nextInt(1, 10) * 10
                    val b = Random.nextInt(1, whole + 1) * 100
                    Problem("${fmtH(a)} - ${b / 100}", (a - b) / 100.0, timeLimit, topic, diff)
                }
                2 -> {
                    val a = Random.nextInt(1, 30) * 10; val k = Random.nextInt(2, 6)
                    Problem("${fmtH(a)} * $k", a * k / 100.0, timeLimit, topic, diff)
                }
                else -> {
                    val q = Random.nextInt(1, 21) * 10; val k = Random.nextInt(2, 6)
                    Problem("${fmtH(q * k)} / $k", q / 100.0, timeLimit, topic, diff)
                }
            }
            Difficulty.MEDIUM -> when (Random.nextInt(4)) {
                0 -> {
                    val a = Random.nextInt(1, 100); val b = Random.nextInt(1, 100)
                    Problem("${fmtH(a)} + ${fmtH(b)}", (a + b) / 100.0, timeLimit, topic, diff)
                }
                1 -> {
                    val a = Random.nextInt(2, 20) * 10; val b = Random.nextInt(2, 10) * 10
                    Problem("${fmtH(a)} * ${fmtH(b)}", a * b / 10000.0, timeLimit, topic, diff)
                }
                2 -> {
                    val a = Random.nextInt(1, 20) * 10; val k = Random.nextInt(2, 5); val c = Random.nextInt(1, 20) * 10
                    Problem("${fmtH(a)} * $k + ${fmtH(c)}", (a * k + c) / 100.0, timeLimit, topic, diff)
                }
                else -> {
                    val q = Random.nextInt(2, 10) * 10; val b = Random.nextInt(2, 10) * 10
                    Problem("${fmtH(q * b / 100)} / ${fmtH(b)}", q / 100.0, timeLimit, topic, diff)
                }
            }
            Difficulty.HARD, Difficulty.FAST -> when (Random.nextInt(4)) {
                0 -> {
                    val a = Random.nextInt(2, 20) * 10; val b = Random.nextInt(2, 10) * 10; val c = Random.nextInt(1, 50) * 10
                    Problem("${fmtH(a)} * ${fmtH(b)} + ${fmtH(c)}", (a * b / 100 + c) / 100.0, timeLimit, topic, diff)
                }
                1 -> {
                    val a = Random.nextInt(10, 40) * 10; val k = Random.nextInt(2, 6); val c = Random.nextInt(1, 20) * 10
                    Problem("${fmtH(a)} * $k - ${fmtH(c)}", (a * k - c) / 100.0, timeLimit, topic, diff)
                }
                2 -> {
                    val q = Random.nextInt(2, 15) * 10; val b = Random.nextInt(2, 10) * 10; val c = Random.nextInt(1, 30) * 10
                    Problem("${fmtH(q * b / 100)} / ${fmtH(b)} + ${fmtH(c)}", (q + c) / 100.0, timeLimit, topic, diff)
                }
                else -> {
                    val a = Random.nextInt(1, 30) * 10; val b = Random.nextInt(1, 30) * 10; val k = Random.nextInt(2, 5)
                    Problem("(${fmtH(a)} + ${fmtH(b)}) * $k", (a + b) * k / 100.0, timeLimit, topic, diff)
                }
            }
        }

        Topic.TABLES -> {
            val square = Random.nextBoolean()
            when (diff) {
                Difficulty.EASY -> if (square) {
                    val n = Random.nextInt(2, 13); Problem("$n²", (n * n).toDouble(), timeLimit, topic, diff)
                } else {
                    val a = Random.nextInt(2, 10); val b = Random.nextInt(2, 10)
                    Problem("$a × $b", (a * b).toDouble(), timeLimit, topic, diff)
                }
                Difficulty.MEDIUM -> if (square) {
                    val n = Random.nextInt(12, 21); Problem("$n²", (n * n).toDouble(), timeLimit, topic, diff)
                } else {
                    val a = Random.nextInt(2, 13); val b = Random.nextInt(2, 13)
                    Problem("$a × $b", (a * b).toDouble(), timeLimit, topic, diff)
                }
                Difficulty.HARD, Difficulty.FAST -> if (square) {
                    val n = Random.nextInt(20, 31); Problem("$n²", (n * n).toDouble(), timeLimit, topic, diff)
                } else {
                    val a = Random.nextInt(12, 20); val b = Random.nextInt(3, 13)
                    Problem("$a × $b", (a * b).toDouble(), timeLimit, topic, diff)
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

        Topic.FRACTIONS -> {
            val d = listOf(2, 4, 5, 8, 10).random()
            when (diff) {
                Difficulty.EASY -> {
                    val n = Random.nextInt(1, d)
                    Problem("$n/$d", n.toDouble() / d, timeLimit, topic, diff)
                }
                Difficulty.MEDIUM -> {
                    val n1 = Random.nextInt(1, d); val n2 = Random.nextInt(1, d)
                    val op = listOf("+", "-").random()
                    val text = if (op == "+") "($n1+$n2)/$d" else "($n1-$n2)/$d"
                    val ans = (if (op == "+") n1 + n2 else n1 - n2).toDouble() / d
                    Problem(text, ans, timeLimit, topic, diff)
                }
                Difficulty.HARD, Difficulty.FAST -> {
                    val n1 = Random.nextInt(2, 10); val n2 = Random.nextInt(2, 5)
                    val op = listOf("*", "+", "-").random()
                    val numText = if (op == "*") "$n1*$n2" else "$n1$op$n2"
                    val numAns = when (op) { "*" -> (n1 * n2).toDouble(); "+" -> (n1 + n2).toDouble(); else -> (n1 - n2).toDouble() }
                    val n3 = Random.nextInt(1, d)
                    Problem("($numText)/$d + $n3/$d", (numAns / d) + (n3.toDouble() / d), timeLimit, topic, diff)
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

fun generateHybridProblem(selectedTopics: Set<Topic>, diff: Difficulty, timeLimit: Int): Problem {
    val topics = selectedTopics.toList()
    val t1 = topics.random()
    var t2 = topics.random()
    while (t1 == t2) { t2 = topics.random() }
    return when (setOf(t1, t2)) {
        setOf(Topic.FRACTIONS, Topic.TRIGONOMETRY) -> {
            val a = getRandomTrigTerm(Difficulty.EASY); val b = getRandomTrigTerm(Difficulty.EASY)
            val d = listOf(2, 4, 5).random(); val op = listOf("+", "-").random()
            val ans = (if (op == "+") a.answer + b.answer else a.answer - b.answer) / d
            Problem("(${a.text}$op${b.text})/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        setOf(Topic.FRACTIONS, Topic.ROOTS) -> {
            val r1 = getRandomRootTerm(); val r2 = getRandomRootTerm()
            val d = listOf(2, 4, 5).random(); val op = listOf("+", "-").random()
            val ans = (if (op == "+") r1.answer + r2.answer else r1.answer - r2.answer) / d
            Problem("(${r1.text}$op${r2.text})/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        setOf(Topic.FRACTIONS, Topic.NUMBERS) -> {
            val n1 = Random.nextInt(10, 50); val n2 = Random.nextInt(1, 20)
            val d = listOf(2, 4, 5).random(); val op = listOf("+", "-", "*").random()
            val ans = when (op) { "+" -> (n1 + n2).toDouble() / d; "-" -> (n1 - n2).toDouble() / d; else -> (n1 * n2).toDouble() / d }
            Problem("($n1$op$n2)/$d", ans, timeLimit, Topic.FRACTIONS, diff)
        }
        setOf(Topic.ROOTS, Topic.TRIGONOMETRY) -> {
            val r = getRandomRootTerm(); val t = getRandomTrigTerm(Difficulty.EASY)
            Problem("${r.text} + ${t.text}", r.answer + t.answer, timeLimit, Topic.TRIGONOMETRY, diff)
        }
        setOf(Topic.NUMBERS, Topic.TRIGONOMETRY) -> {
            val mult = Random.nextInt(2, 5); val t = getRandomTrigTerm(Difficulty.EASY)
            Problem("$mult * ${t.text}", mult * t.answer, timeLimit, Topic.TRIGONOMETRY, diff)
        }
        setOf(Topic.ROOTS, Topic.NUMBERS) -> {
            val r = getRandomRootTerm(); val mult = Random.nextInt(2, 5); val extra = Random.nextInt(1, 20)
            Problem("${r.text} * $mult + $extra", r.answer * mult + extra, timeLimit, Topic.ROOTS, diff)
        }
        setOf(Topic.NUMBERS, Topic.DECIMALS) -> {
            val a = Random.nextInt(2, 20); val b = Random.nextInt(2, 20)
            val k = listOf(20, 25, 50, 75).random()
            Problem("($a + $b) * ${fmtH(k)}", (a + b) * k / 100.0, timeLimit, Topic.DECIMALS, diff)
        }
        setOf(Topic.NUMBERS, Topic.TABLES) -> {
            val a = Random.nextInt(2, 10); val b = Random.nextInt(2, 10); val c = Random.nextInt(1, 30)
            Problem("$a × $b + $c", (a * b + c).toDouble(), timeLimit, Topic.TABLES, diff)
        }
        setOf(Topic.FRACTIONS, Topic.DECIMALS) -> {
            val d = listOf(2, 4, 5).random(); val n = Random.nextInt(1, d)
            val k = Random.nextInt(1, 10) * 10
            Problem("$n/$d + ${fmtH(k)}", n.toDouble() / d + k / 100.0, timeLimit, Topic.DECIMALS, diff)
        }
        setOf(Topic.FRACTIONS, Topic.TABLES) -> {
            val a = Random.nextInt(2, 10); val b = Random.nextInt(2, 10); val d = listOf(2, 4, 5).random()
            Problem("($a × $b)/$d", a * b.toDouble() / d, timeLimit, Topic.TABLES, diff)
        }
        setOf(Topic.ROOTS, Topic.DECIMALS) -> {
            val r = getRandomRootTerm(); val k = Random.nextInt(1, 10) * 10
            Problem("${r.text} + ${fmtH(k)}", r.answer + k / 100.0, timeLimit, Topic.DECIMALS, diff)
        }
        setOf(Topic.ROOTS, Topic.TABLES) -> {
            val r = getRandomRootTerm(); val a = Random.nextInt(2, 10); val b = Random.nextInt(2, 10)
            Problem("${r.text} + $a × $b", r.answer + a * b, timeLimit, Topic.TABLES, diff)
        }
        setOf(Topic.TRIGONOMETRY, Topic.DECIMALS) -> {
            val t = getRandomTrigTerm(Difficulty.EASY); val k = Random.nextInt(1, 10) * 10
            Problem("${t.text} + ${fmtH(k)}", t.answer + k / 100.0, timeLimit, Topic.DECIMALS, diff)
        }
        setOf(Topic.TRIGONOMETRY, Topic.TABLES) -> {
            val n = Random.nextInt(2, 7); val t = getRandomTrigTerm(Difficulty.EASY)
            Problem("$n² * ${t.text}", n * n * t.answer, timeLimit, Topic.TABLES, diff)
        }
        setOf(Topic.DECIMALS, Topic.TABLES) -> {
            val n = Random.nextInt(2, 10); val k = listOf(20, 25, 50).random()
            Problem("$n² * ${fmtH(k)}", n * n * k / 100.0, timeLimit, Topic.DECIMALS, diff)
        }
        else -> generateSingleTopicProblem(t1, diff, timeLimit)
    }
}