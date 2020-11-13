package cookbook.ch7_making_asynchronous_programming_great_again

import code
import comment
import h1
import h2
import jdk.nashorn.internal.objects.Global
import kotlinx.coroutines.*
import list
import text
import run
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

fun ch7() {
    h1("Ch7. Making Asynchronous Programming Great Again")
    list(arrayOf(
        "[Kotlin Reference: Coroutine Basic](https://kotlinlang.org/docs/reference/coroutines/basics.html)",
        "[KotlinConf 2017 - Deep Dive into Coroutines on JVM by Roman Elizarov](https://www.youtube.com/watch?v=YrrUCSi72E8)",
        "[Kotlin’s suspend functions compared to JavaScript’s async/await](https://medium.com/@joffrey.bion/kotlins-suspend-functions-are-not-javascript-s-async-they-are-javascript-s-await-f95aae4b3fd9)"
    ))
    h2(arrayOf(
        ::executing_tasks_in_the_background_using_threads,
        ::background_threads_synchronization,
        ::using_coroutines_for_asynchronous_concurrent_execution_of_tasks,
        ::using_coroutines_for_asynchronous_concurrent_execution_with_results_handling,
        ::applying_coroutines_for_asynchronous_data_processing,
        ::easy_coroutine_cancelation,
        ::building_a_REST_API_client_with_Retrofit_and_coroutines_adapter,
        ::wrapping_third_party_callback_style_APIs_with_coroutines
    ))
}

fun getCurrentThreadName(): String = Thread.currentThread().name
fun `5 sec long task` () = Thread.sleep(5000)
fun `2 sec long task` () = Thread.sleep(2000)
fun dummyPrint(s: String) = Unit

fun executing_tasks_in_the_background_using_threads() {
    code("""
    fun getCurrentThreadName(): String = Thread.currentThread().name
    fun `5 sec long task` () = Thread.sleep(5000)
    fun `2 sec long task` () = Thread.sleep(2000)
    
    println("Running on ${'$'}{getCurrentThreadName()}")
    thread {
        println("Starting async operation on ${'$'}{getCurrentThreadName()}")
        `5 sec long task`()
        println("Ending async operation on ${'$'}{getCurrentThreadName()}")
    }
    thread {
        println("Starting async operation on ${'$'}{getCurrentThreadName()}")
        `2 sec long task`()
        println("Ending async operation on ${'$'}{getCurrentThreadName()}")
    }
    """)
    dummyPrint("Running on ${getCurrentThreadName()}")
    thread {
        dummyPrint("Starting async operation on ${getCurrentThreadName()}")
        `5 sec long task`()
        dummyPrint("Ending async operation on ${getCurrentThreadName()}")
    }
    thread {
        dummyPrint("Starting async operation on ${getCurrentThreadName()}")
        `2 sec long task`()
        dummyPrint("Ending async operation on ${getCurrentThreadName()}")
    }
    run {
        text("""
        Running on main
        Starting async operation on Thread-0
        Starting async operation on Thread-1
        Ending async operation on Thread-1
        Ending async operation on Thread-0
        """.trimIndent()
        )
    }
}

fun background_threads_synchronization() {
    code("""
    println("Running on ${'$'}{getCurrentThreadName()}")
    thread {
        println("Starting async operation on ${'$'}{getCurrentThreadName()}")
        `5 sec long task`()
        println("Ending async operation on ${'$'}{getCurrentThreadName()}")
    }.join()
    thread {
        println("Starting async operation on ${'$'}{getCurrentThreadName()}")
        `2 sec long task`()
        println("Ending async operation on ${'$'}{getCurrentThreadName()}")
    }.join()
    """)
    run {
        println("Running on ${getCurrentThreadName()}")
        thread {
            println("Starting async operation on ${getCurrentThreadName()}")
            `5 sec long task`()
            println("Ending async operation on ${getCurrentThreadName()}")
        }.join()
        thread {
            println("Starting async operation on ${getCurrentThreadName()}")
            `2 sec long task`()
            println("Ending async operation on ${getCurrentThreadName()}")
        }.join()
    }
}
private fun `cook rice`() {
    println("Starting to cook rice on ${getCurrentThreadName()}")
    Thread.sleep(1000)
    println("Rice cooked")
}
private fun `prepare fish`() {
    println("Starting to prepare fish on ${getCurrentThreadName()}")
    Thread.sleep(200)
    println("Fish prepared")
}
private fun `cut vegetable`() {
    println("Starting to cut vegetables on ${getCurrentThreadName()}")
    Thread.sleep(200)
    println("Vegetables ready")
}
private fun `roll the sushi`() {
    println("Starting to roll the sushi on ${getCurrentThreadName()}")
    Thread.sleep(200)
    println("Sushi rolled")
}
private fun `print current thread name`() {
    println("Running on ${getCurrentThreadName()}")
    println()
}
fun using_coroutines_for_asynchronous_concurrent_execution_of_tasks() {
    run {
        `print current thread name`()
        var sushiCookingJob: Job
        sushiCookingJob = GlobalScope.launch(newSingleThreadContext("SushiThread")) {
            `print current thread name`()
            var riceCookingJob = GlobalScope.launch {
                `cook rice`()
            }
            println("Current thread is not blocked while rice is being cooked")
            `prepare fish`()
            `cut vegetable`()
            riceCookingJob.join()
            `roll the sushi`()
        }
        measureTimeMillis {
            runBlocking {
                sushiCookingJob.join()
            }
        }.also {
            println("Total time: $it ms")
        }
    }
    comment("""
    see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)    
    """)
}

private suspend fun `caculate the anwser to life the universe and everything`(): Int {
    delay(5000)
    return 42
}
private suspend fun `show progress animation`() {
    val progressBarLength = 30
    var currentPosition = 0
    while (true) {
        print("\r")
        val progressbar = (0 until progressBarLength).map {if (it == currentPosition) ">" else "-"}.joinToString("")
        print(progressbar)
        delay(50)
        if (currentPosition == progressBarLength) currentPosition = 0
        currentPosition++
    }
}

fun using_coroutines_for_asynchronous_concurrent_execution_with_results_handling() {
    comment("""
    see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)
    """)
    run {
        `print current thread name`()
        GlobalScope.launch {
            println("Starting progressbar animation on ${getCurrentThreadName()}")
            `show progress animation`()
        }
        val future = GlobalScope.async {
            println("Starting computations on ${getCurrentThreadName()}")
            `caculate the anwser to life the universe and everything`()
        }
        println("\n${getCurrentThreadName()} thread is not blocked while tasks are in progress")

        runBlocking {
            println("\nThe anwser to life the universe and everything: ${future.await()}")
            `print current thread name`()
        }
    }
}
fun applying_coroutines_for_asynchronous_data_processing() {}
fun easy_coroutine_cancelation() {}
fun building_a_REST_API_client_with_Retrofit_and_coroutines_adapter() {}
fun wrapping_third_party_callback_style_APIs_with_coroutines() {}