package cookbook.ch3_shaping_code_with_kotlin_functional_programming

import comment
import h1
import h2
import list
import run
import not_implemented

fun ch3() {
    h1("Ch3. Shaping code with Kotlin functional programming")
    h2(arrayOf(
        ::working_effectively_with_lambda_expressions,
        ::discovering_basic_scoping_functions
    ))
}

fun working_effectively_with_lambda_expressions() {
 not_implemented()
}

data class Player(val name: String, val bestScore: Int)
fun discovering_basic_scoping_functions() {
    fun getPlayers() : List<Player> = listOf(
        Player("Stefan Madej", 109),
        Player("Adam Ondra", 323),
        Player("Chris Charma", 329)
    )

    run {
        getPlayers().let {
            it.also {
                println("${it.size} players records fetched")
                println(it)
            }.let {
                it.sortedByDescending { it.bestScore }
            }.let {
                it.first()
            }.apply {
                val name = this.name
                print("Best Player: $name")
            }
        }
    }

    list(arrayOf(
        "`public inline fun <T, R> T.let(block: (T) -> R): R",
        "`public inline fun <T> T.also(block: (T) -> Unit) :T",
        "`public inline fun <T> T.apply(block: T.() -> Unit): T"
    ))
}