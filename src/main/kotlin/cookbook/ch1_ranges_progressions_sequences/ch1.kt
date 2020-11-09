package cookbook.ch1_ranges_progressions_sequences
import code
import comment
import run
import h2
import h1
import not_implemented
import java.util.*

fun ch1() {

    h1("Ch1. Ranges, Progressions, and Sequences")
    h2(arrayOf(
        ::exploring_the_use_of_rang_expression_to_iterate_through_alphabet_characters,
        ::traversing_through_ranges_using_progression_with_a_custom_step_value,
        ::building_custom_progressions_to_traverse_dates,
        ::using_range_expressions_with_flow_control_statements
    ))
}

inline fun exploring_the_use_of_rang_expression_to_iterate_through_alphabet_characters() {
    code("""
    for (letter in 'Z' downTo 'A') print(letter)")
    => """)
    run {
        for (letter in 'Z' downTo 'A') print(letter)
    }
    comment("downTo is an infix function of Char")

    code("""
    val ten = 10
    for (letter in 9 downTo 'A') print(letter)")
    => """)
    run {
        val ten = 10
        for (number in ten downTo 0) print(number)
    }
    comment("here, downTo is an infix function of Int")

    code("""
    val oneToTen = 1..10
    for (number in oneToTen) print(number)
    """)
    run {
        val oneToTen = 1..10
        for (number in oneToTen.reversed()) print(number)
    }
}

inline fun traversing_through_ranges_using_progression_with_a_custom_step_value() {
    code("""
    val progression: IntProgression = 0..10 step 2
    for (i in progression) print(i)    
    """)
    run {
        val progression: IntProgression = 0..10 step 2
        for (i in progression) print(i)
    }
}

inline fun building_custom_progressions_to_traverse_dates() {
    not_implemented()
}

inline fun using_range_expressions_with_flow_control_statements() {
    code("""
    val randomInt = Random().nextInt()
    val range = 0..10
    if (randomInt in range) {
        println("${'$'}randomInt belongs to ${'$'}range\")
    } else {
        println(\"${'$'}randomInt does't not belong to ${'$'}range\")
    }
    """)
    run {
        val randomInt = Random().nextInt()
        val range = 0..10
        if (randomInt in range) {
            println("$randomInt belongs to $range")
        } else {
            println("$randomInt does't not belong to $range")
        }
    }
}