package cookbook.ch1_ranges_progressions_sequences
import code
import comment
import run
import h2
import h1
import html
import link
import list
import not_implemented
import pre
import text
import java.util.*

fun ch1() {

    h1("Ch1. Ranges, Progressions, and Sequences")
    h2(arrayOf(
        ::exploring_the_use_of_rang_expression_to_iterate_through_alphabet_characters,
        ::traversing_through_ranges_using_progression_with_a_custom_step_value,
        ::building_custom_progressions_to_traverse_dates,
        ::using_range_expressions_with_flow_control_statements,
        ::discovering_the_concept_of_sequences,
        ::applying_sequences_to_solve_alghorithmic_problems
    ))
}

fun exploring_the_use_of_rang_expression_to_iterate_through_alphabet_characters() {
    code("""
    for (letter in 'Z' downTo 'A') print(letter)")
    """)
    run {
        for (letter in 'Z' downTo 'A') print(letter)
    }
    comment("downTo is an infix function of Char")

    code("""
    val ten = 10
    for (letter in 9 downTo 'A') print(letter)")
    """)
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

fun traversing_through_ranges_using_progression_with_a_custom_step_value() {
    code("""
    val progression: IntProgression = 0..10 step 2
    for (i in progression) print(i)    
    """)
    run {
        val progression: IntProgression = 0..10 step 2
        for (i in progression) print(i)
    }
}

fun building_custom_progressions_to_traverse_dates() {
    not_implemented()
}

fun using_range_expressions_with_flow_control_statements() {
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
            println("$randomInt doesn't not belong to $range")
        }
    }
    list(arrayOf(
    "There is no good way to escape dollar sign in Kotlin here document, you have to use string interpolation: `${'$'}{'${'$'}'}`",
    "`in` is an operator. see ${link("Operator overloading", "https://kotlinlang.org/docs/reference/operator-overloading.html")}",
        "${link("Provide a way for escaping the dollar sign symbol (\"\$\") in raw strings and string templates","https://youtrack.jetbrains.com/issue/KT-2425")}"
    ))

    code("""
    var score = Random().nextInt(100)
    var grade = when(score) {
        in 90..100 -> "Excellent"
        in 75 until 90 -> "Great"
        in 60 until 75 -> "OK"
        else -> "Bad"
    }
    print("${'$'}score belongs to grade ${'$'}grade")
    """)
    run {
        var score = Random().nextInt(100)
        var grade = when(score) {
            in 90..100 -> "Excellent"
            in 75 until 90 -> "Great"
            in 60 until 75 -> "OK"
            else -> "Bad"
        }
        print("$score belongs to grade $grade")
    }
    comment("You can inspect Kotlin bytecode in IntelliJ>Tools>Kotlin>Show Kotlin bytecode")
    comment("Above `when` expression is translate into following bytecode:")
    html(pre("""
        LINENUMBER 104 L1
        ILOAD 1
        ISTORE 3
       L2
        LINENUMBER 105 L2
        BIPUSH 100
        BIPUSH 90
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L3
        POP
        GOTO L4
       L3
        ILOAD 4
        IF_ICMPLT L4
       L5
        LDC "Excellent"
        GOTO L6
       L4
        LINENUMBER 106 L4
        BIPUSH 90
        BIPUSH 75
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L7
        POP
        GOTO L8
       L7
        ILOAD 4
        IF_ICMPLE L8
       L9
        LDC "Great"
        GOTO L6
       L8
        LINENUMBER 107 L8
        BIPUSH 75
        BIPUSH 60
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L10
        POP
        GOTO L11
       L10
        ILOAD 4
        IF_ICMPLE L11
       L12
        LDC "OK"
        GOTO L6
       L11
        LINENUMBER 108 L11
        LDC "Bad"
       L13
        LINENUMBER 104 L13
       L6
        ASTORE 2
       L14"""))
}

fun discovering_the_concept_of_sequences() {
    text("`Sequence delays any operations on its elements until they are finally consumed.`")
    code("""
    val seq = ('a'..'z').asSequence()
    for (c in seq.map{it.toUpperCase()}.take(3)) print(c)
    """)
    run {
        val range = ('a'..'h')
        println("\nsequence:")
        for (c in range.asSequence().map{
            println("in map: $it")
            it.toUpperCase()
        }.take(3)) println(c)

        println("\nrange:")
        for (c in range.map{
            println("in map: $it")
            it.toUpperCase()
        }.take(3)) println(c)
    }
    comment("""
    map() was applied only to the first 3 elements of the sequence,
    event though the take() functtion was called after the mapping.
    see ${link("Java Streams vs. Kotlin Sequences", "https://proandroiddev.com/java-streams-vs-kotlin-sequences-c9ae080abfdc")}
    """)
}

fun applying_sequences_to_solve_alghorithmic_problems() {
    code("""
    fun fibonacci(): Sequence<Int> {
        return generateSequence(1 to 1) {
            Pair(it.second, it.first + it.second)
        }.map { it.first }
    }
    println(fibonacci().take(6).toList())
    """)
    run {
        fun fibonacci(): Sequence<Int> {
            return generateSequence(1 to 1) {
                Pair(it.second, it.first + it.second)
            }.map { it.first }
        }
        println(fibonacci().take(6).toList())
    }
    comment("""
    `generateSequence` is like a mixture of `reduce` and `map`:
    it take an seed value (like reduce) optionally and take the return value of every call to the lambda
    as sequence elements
    """)
}