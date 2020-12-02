package coreprog.ch4_adt_and_pattern_matching

import kotlin.random.Random

enum class WeekDay {
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT
}

sealed class Day {
    object SUN: Day();
    object MON: Day();
    object TUE: Day();
    object WED: Day();
    object THU: Day();
    object FRI: Day();
    object SAT: Day();
}

fun schedule(day: Day): String {
    return when (day) {
        is Day.SUN -> "fishing"
        is Day.MON -> "work"
        is Day.TUE -> "study"
        is Day.WED -> "library"
        is Day.THU -> "writing"
        is Day.FRI -> "appointment"
        is Day.SAT -> "basketball"
    }
}

fun constantPattern(a: Int) = when (a) {
    1 -> "One"
    2 -> "Two"
    else -> "Greater than two"
}

fun logicPattern(a: Int) = when (a) {
    in 2..11 -> "$a is smaller than 10 and bigger than 1"
    else -> "Maybe $a is bigger than 10 or smaller than 1"
}

sealed class Expr {
    data class Num(val value: Int): Expr()
    data class Operate(val opName: String, val left: Expr, val right: Expr): Expr()
}

fun simpifyExpr(expr: Expr): Expr = when(expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        Expr.Operate("+", Expr.Num(0), expr.right) -> simpifyExpr(expr.right)
        Expr.Operate("+", expr.left, Expr.Num(0)) -> simpifyExpr(expr.left)
        else -> expr
    }
}

fun main() {
    println(schedule(Day.WED))
    println(constantPattern(Random.nextInt(1, 10)))
    println(logicPattern(Random.nextInt(1, 15)))
    // 0 + (1 + 0)
    val expr = Expr.Operate("+", Expr.Num(0), Expr.Operate("+", Expr.Num(1), Expr.Num(0)))
    println(simpifyExpr(expr))
}