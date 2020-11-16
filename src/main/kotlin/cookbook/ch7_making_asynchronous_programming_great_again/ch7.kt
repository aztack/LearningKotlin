package cookbook.ch7_making_asynchronous_programming_great_again

import code
import com.google.gson.annotations.SerializedName
import comment
import h1
import h2
import html
import kotlinx.coroutines.*
import list
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import text
import run
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.system.measureTimeMillis

fun ch7() {
    h1("Ch7. Making Asynchronous Programming Great Again")
    list(arrayOf(
        "[Kotlin Reference: Coroutine Basic](https://kotlinlang.org/docs/reference/coroutines/basics.html)",
        "[KotlinConf 2017 - Deep Dive into Coroutines on JVM by Roman Elizarov](https://www.youtube.com/watch?v=YrrUCSi72E8)",
        "[Kotlin’s suspend functions compared to JavaScript’s async/await](https://medium.com/@joffrey.bion/kotlins-suspend-functions-are-not-javascript-s-async-they-are-javascript-s-await-f95aae4b3fd9)",
        "[What does the suspend function mean in a Kotlin Coroutine?](https://stackoverflow.com/questions/47871868/what-does-the-suspend-function-mean-in-a-kotlin-coroutine)"
    ))

    text("Quote from the third article above")
    comment("""
    In short, Kotlin explicitly declares at the call site that the call is asynchronous via the GlobalScope.async() function call. 
    On the other hand, if something looks like a normal function call, it is implicitly synchronous and we can expect a result directly.
    In JavaScript, normal calls of an async function are implicitly asynchronous, because they return a Promise. JavaScript is explicit 
    about making these calls synchronous via await.
    """.trimIndent())

    html("""
    Language  | Asynchronous Function Keyword | Waiting for Async Function | Require a Promise/Deferred
    ----------|-------------------------------|----------------------------|---------------------------
    Kotlin    | suspend                       | fun()                      | GlobalScope.async{ suspendedFun() }
    JavaScript| async                         | await fun()                | asyncFun()
    
    In Kotlin, calling a suspended function will block the thread unless call via the GlobalScope.async() explicitly.
    """.trimIndent())

    comment("""
    [Kotlin Coroutines vs Javascript Async/await](https://ducaale.github.io/Kotlin-Coroutines-vs-Javascript-Async-await/):
    Kotlin makes use of suspend keyword instead of `async` keyword.
    Notice the lack of await in kotlin. that is because __kotlin executes suspend functions in sequential fashion by default__.
    """.trimIndent())

    code("""
    // JavaScript
    // returns a Promise because of "async", even if we can see "return 42" in the body
    async function somethingDeep() { 
      // ... some long running operation here
      return 42; 
    }
    
    // returns a Promise because of "async", even if we can see "return value" in the body
    // async keyword is necessary because we use await
    async function callSomethingDeep() {
      // this synchronously waits for somethingDeep() to finish ('value' is a number, not a Promise)
      const value = await somethingDeep()
      return value;
    }
    
    // returns a Promise in order to draw a parallel with Kotlin's Deferred<Int>
    function normalFunction() {
      // this returns immediately
      // normal call to async function = implicitely asynchronous because it returns a Promise
      const promise = callSomethingDeep();
      return promise
    }
    """, "javascript")

    code("""
    // Kotlin
    // returns an actual Int, the declaration matches the body
    suspend fun somethingDeep(): Int {
        // ... some long running operation here
        return 42
    }
    
    // returns an actual Int, the declaration matches the body
    // suspend keyword is necessary because we call a suspending function
    suspend fun callSomethingDeep(): Int {
        // this synchronously waits for somethingDeep() to finish
        val value: Int = somethingDeep()
        return value
    }
    
    // returns a Deferred<Int> in order to draw a parallel with JavaScript's Promise
    fun normalFunction(): Deferred<Int> {
        // this returns immediately
        // 'async' explicitly makes this call asynchronous and returns a Deferred<Int>
        val deferred: Deferred<Int> = GlobalScope.async { callSomethingDeep() }
        return deferred
    }
    """)

    html("""
    Mimic `Promise.all` in Kotlin:
    """)

    code("""
    // JavaScript
    // await multiple promise
    const doStuff = async () => {
        const id = 1
        const user = await getUsers(id)

        const postsPromise = getPosts(user) // return Promise by default
        const commentsPromise = getComments(user)

        const posts = await postsPromise
        const comments = await commentsPromise

        // or you can just do 
        // Promise.all([postsPromise, commentsPromise])
    }
    """.trimIndent(), "javascript")

    code("""
    // kotlin
    // await multiple deferred
    suspend fun doStuff() {
        val id = 1
        val user =  getUsers(id) 


        val postsDeferred = GlobalScope.async { getPosts(user) } // return deferred by calling with GlobalScope.async
        val commentsDeferred = GlobalScope.async { getComments(user) }

        val posts = postsDeferred.await()
        val comments = commentsDeferred.await()
    }
    """.trimIndent())

    html("Calling suspended function in normal function:")

    code("""
    // Kotlin
    fun normalBoringFunction() {
        GlobalScope.launch() {
            // now you can use your suspending functions
        }
    }
    """.trimIndent())

    html("""
    if you want to block the main thread like in your main function you can wrap launch coroutine builder 
    with another coroutine builder called runBlocking
    """.trimIndent())
    code("""
    // kotlin
    // now your main exit because runBlocking is blocking it
    fun main(args: Array<String>) = runBlocking<Unit> {
        launch() {
            // now you can use your suspending functions
        }
    }
    """.trimIndent())


    h2(arrayOf(
        ::executing_tasks_in_the_background_using_threads,
        ::background_threads_synchronization,
        ::using_coroutines_for_asynchronous_concurrent_execution_of_tasks,
        ::using_coroutines_for_asynchronous_concurrent_execution_with_results_handling,
        ::applying_coroutines_for_asynchronous_data_processing,
        ::easy_coroutine_cancellation,
        ::building_a_REST_API_client_with_Retrofit_and_coroutines_adapter,
        ::wrapping_third_party_callback_style_APIs_with_coroutines
    ))
}

val skipAsyncOperation = System.getenv("SKIP_ASYNC_OP") == "true"

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
        if (skipAsyncOperation) return@run false
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
        if (skipAsyncOperation) return@run false
        `print current thread name`()
        val sushiCookingJob: Job
        sushiCookingJob = GlobalScope.launch(newSingleThreadContext("SushiThread")) {
            `print current thread name`()
            val riceCookingJob = GlobalScope.launch {
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
        val progressbar = (0 until progressBarLength).joinToString("") { if (it == currentPosition) ">" else "-" }
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
        if (skipAsyncOperation) return@run false
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

suspend fun <T, R> Iterable<T>.mapConcurrent(transform: suspend (T) -> R) =
        this.map {
            GlobalScope.async { transform(it)}
        }.map {
            it.await()
        }

fun applying_coroutines_for_asynchronous_data_processing() {
    code("""
    suspend fun <T, R> Iterable<T>.mapConcurrent(transform: suspend (T) -> R) =
        this.map {
            GlobalScope.async { transform(it)} // return Deferred
        }.map {
            it.await() // wait deferred to return
        }
    fun fn() {
        // calling of suspend function must surrounded with `runBlocking`
        runBlocking {
            val totalTime = measureTimeMillis {
                (0..10).mapConcurrent {
                    delay(100L * it)
                    it * it
                }.map { println(it) }
            }
            println("Total time: ${'$'}totalTime ms")
        } 
    }
    """)

    text("Equals to:")

    code("""
    // JavaScript equivalent:
    Array.prototype.mapConcurrent = function (transform) {
        return Promise.all(this.map(transform))
    }
    (async function fn(){
        const totalTime = console.time('mapConcurrent)');
        const transformed = await (new Array(10).fill(0).map((_, index) => index)).mapConcurrent((it) => {
            return new Promise((rs, rj) => {
                setTimeout(() => rs(it * it), 100*it);
            });
        })
        transformed.forEach(console.log);
        
        console.log("Total time:");
        console.timeEnd('mapConcurrent'));
    })();
    """)

    run {
        runBlocking {
            val totalTime = measureTimeMillis {
                (0..10).mapConcurrent {
                    delay(100L * it)
                    it * it
                }.map { println(it) }
            }
            println("Total time: $totalTime ms")
        }
    }
}

fun easy_coroutine_cancellation() {
    code("""
    runBlocking {
        val job = GlobalScope.launch { `show progress animation`() }
        delay(5000)
        job.cancel()
        job.join()
        println("Cancelled")
    }
    """)

    run {
        runBlocking {
            val job = GlobalScope.launch { `show progress animation`() }
            delay(5000)
            job.cancel()
            job.join()
            println("Cancelled")
        }
    }

    comment("[Bluebird Docs: Cancellation](http://bluebirdjs.com/docs/api/cancellation.html)")
}
data class Repository(
        val id: Long?,
        val name: String?,
        val description: String?,
        @SerializedName("full_name") val fullName: String?,
        @SerializedName("html_url") val url: String?,
        @SerializedName("stargazers_count") val starts: Long?)

data class Response(@SerializedName("items") val list: Collection<Repository>)
interface GithubApi {
    @GET("/search/repositories")
    fun searchRepositoriesAsync(@Query("q") searchQuery: String): Call<Response>
}

private inline fun <reified T> createApiOn(baseUrl: String): T {
    return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
}
fun building_a_REST_API_client_with_Retrofit_and_coroutines_adapter() {
    comment("""
    see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)
    """)
    run {
        runBlocking {
            val api = createApiOn<GithubApi>("https://api.github.com")

            val downloadedRepos = api.searchRepositoriesAsync("Kotlin").await().list
            downloadedRepos.sortedBy { it.starts }
                    .take(5).forEach {
                        it.apply {
                            println("$fullName ⭐️$starts\n$description\n$url\n")
                        }
                    }
        }
    }
}
fun wrapping_third_party_callback_style_APIs_with_coroutines() {
    comment("""
    see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)
    """)
    data class Result(val displayName: String);
    fun getResultAsync(callback: (List<Result>) -> Unit) =
            thread {
                val results = mutableListOf<Result>()
                Thread.sleep(1000)
                results.add(Result("a"))
                results.add(Result("b"))
                results.add(Result("c"))
                callback(results)
            }

    suspend fun getResult(): List<Result> =
            suspendCoroutine {
                continuation: Continuation<List<Result>> ->
                    getResultAsync { continuation.resume(it) }
            }

    runBlocking {
        val results = GlobalScope.async { getResult() }
        println("getResults() is running in background. Main thread is not blocked.")
        results.await().map { println(it.displayName) }
        println("getResults() completed")
    }
}