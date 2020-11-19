package understanding_kotlin_coroutine

import html
import img
import tee
import understanding_kotlin_coroutine.ch1_introducing_asynchronous_application_design.ch1
import java.io.File
import java.io.PrintStream

fun main() {
    val dir = "./src/main/kotlin/understanding_kotlin_coroutine"
    arrayOf(
            "ch1_introducing_asynchronous_application_design"
    ).forEach {
        val readme ="${dir}/${it}/README.md"
        File(readme).createNewFile()
        tee(PrintStream(readme))
        ch1()
    }
}

