package cookbook.ch3_shaping_code_with_kotlin_functional_programming

import code
import comment
import h1
import h2
import html
import link
import list
import run
import not_implemented
import text
import java.math.BigInteger
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.roundToInt
import kotlin.time.*

fun ch3() {
    h1("Ch3. Shaping code with Kotlin functional programming")
    h2(arrayOf(
        ::working_effectively_with_lambda_expressions,
        ::discovering_basic_scoping_functions,
        ::initializing_objects_the_clean_way_using_the_run_scoping_function,
        ::working_with_higher_order_functions,
        ::functions_curring,
        ::function_composition,
        ::implement_the_Either_Monad_design_pattern,
        ::approach_to_automatic_functions_memoization
    ))
}

fun working_effectively_with_lambda_expressions() {
 not_implemented()
}

data class Player(val name: String, val bestScore: Int)
@Suppress("SimplifiableCallChain")
fun discovering_basic_scoping_functions() {
    fun getPlayers() : List<Player> = listOf(
        Player("Stefan Madej", 109),
        Player("Adam Ondra", 323),
        Player("Chris Charma", 329)
    )

    text("""
        The Kotlin standard library contains several functions whose sole purpose is to execute a block of code within the context of an object
    """.trimIndent())

    code("""
    fun getPlayers() : List<Player> = listOf(
        Player("Stefan Madej", 109),
        Player("Adam Ondra", 323),
        Player("Chris Charma", 329)
    )
    getPlayers().let { it ->
        it.also {
            println("${'$'}{it.size} players records fetched")
            println(it)
        }.let { it ->
            it.sortedByDescending { it.bestScore }.first()
        }.apply {
            val name = this.name
            print("Best Player: ${'$'}name")
        }
    }
    """)

    run {
        getPlayers().let { it ->
            it.also {
                println("${it.size} players records fetched")
                println(it)
            }.let { it ->
                it.sortedByDescending { it.bestScore }.first()
            }.apply {
                println("Best Player: $name")
            }.run {
                println(this)
            }
        }
    }

    text("Detail implementation of ${link(
        "Scope functions", "https://kotlinlang.org/docs/reference/scope-functions.html"
    )}: （some code are removed for for simplicity sake")

    code("""
    public inline fun <T> T.also(block: (T) -> Unit): T {
        block(this)
        return this
    }
    
    public inline fun <T, R> T.let(block: (T) -> R): R {
        return block(this)
    }
    
    public inline fun <T> T.apply(block: T.() -> Unit): T {
        block()
        return this
    }
    
    public inline fun <T, R> with(receiver: T, block: T.() -> R): R {
        return receiver.block()
    }
    
    public inline fun <T, R> T.run(block: T.() -> R): R {
        return block()
    }
    
    public inline fun <T> T.takeIf(predicate: (T) -> Boolean): T? {
        return if (predicate(this)) this else null
    }
    
    public inline fun <T> T.takeUnless(predicate: (T) -> Boolean): T? {
        return if (!predicate(this)) this else null
    }
    """)

    list(title = "There are two main differences between each scope function", items = arrayOf(
        "The way to refer to the context object",
        "The return value."
    ))
    html("""
    function | implicit receiver   | return              | first arg of block
    -------- | ------------------- | ------------------- | ------------------
      run    |         yes         | block result        | -         
      with   |         yes         | block result        | -       
      apply  |         yes         | this                | -
      also   |         no          | this                | this
      let    |         no          | block result        | this          
      take*  |         no          | block result or null| this  
      
    Mnemonic:
    
    - There are 6 scope functions: `run with apply, also let take`. The first three got implicit receiver
      * `run with apply`: do not need to pass `this` to block, since you can access receiver with `this`
      * `also let take` : pass `this` to block, you can access object with `it` and return `it` or any other things
    - Only `also apply` begin with letter `a` and return `this` and usually used in value initialization (since both of them return `this`)
    """.trimIndent())

    text("Conventions for using `apply`")
    code("""
        // Compare with `also` in following snippets
        // `apply` only access properties in apply block!
        val peter = Person().apply {
            name = "Peter"
            age = 18
        }
        // equivalent to:
        val clark = Person()
        clark.name = "Clark"
        clark.age = 18
    """)

    text("Conventions for using `also`")
    code("""
        // Following code is very readable:
        // initialize property author with parameter author
        // and `also` require it age not null and print it's name
        class Book(author: Person) {
            val author = author.also {
              requireNotNull(it.age)
              print(it.name)
            }
        }
        // equivalent to:
        class Book(val author: Person) {
            init {
              requireNotNull(author.age)
              print(author.name)
            }
        }
    """)

    text("Conventions for using `let`")
    code("""
        getNullablePerson()?.let {
            // only executed when not-null
            promote(it)
        }
        val driversLicence: Licence? = getNullablePerson()?.let {
            // convert nullable person to nullable driversLicence
            licenceService.getDriversLicence(it) 
        }
        val person: Person = getPerson()
        getPersonDao().let { dao -> 
            // scope of dao variable is limited to this block
            dao.insert(person)
        }
        // equivalent to:
        val person: Person? = getPromotablePerson()
        if (person != null) {
          promote(person)
        }
        val driver: Person? = getDriver()
        val driversLicence: Licence? = if (driver == null) null else
            licenceService.getDriversLicence(it)
        val person: Person = getPerson()
        val personDao: PersonDao = getPersonDao()
        personDao.insert(person)
    """)

    text("Conventions for using `with`")
    code("""
        val person: Person = getPerson()
        with(person) {
            print(name)
            print(age)
        }
        // equivalent to:
        val person: Person = getPerson()
        print(person.name)
        print(person.age)
        // better rewrite as:
        val person = getPerson().also {
            print(name)
            print(age)
        }
    """)

    text("Conventions for using `run`")
    code("""
        val inserted: Boolean = run {
            val person: Person = getPerson()
            val personDao: PersonDao = getPersonDao()
            personDao.insert(person)
        }
        fun printAge(person: Person) = person.run {
            print(age)
        }
        // equivalent to:
        val person: Person = getPerson()
        val personDao: PersonDao = getPersonDao()
        val inserted: Boolean = personDao.insert(person)
        fun printAge(person: Person) = {
            print(person.age)
        }
    """)
}

fun initializing_objects_the_clean_way_using_the_run_scoping_function() {
    code("""
    Calendar.Builder().run {
        setCalendarType("iso8601")
        setDate(2020, 11, 11)
        setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
        build()
    }.also {
        print(it.time)
    }
    """)
    run {
        Calendar.Builder().run {
            setCalendarType("iso8601")
            setDate(2020, 11, 11)
            setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
            build()
        }.also {
            print(it.time)
        }
    }
}

fun working_with_higher_order_functions () {
    not_implemented()
}

fun functions_curring() {
    not_implemented()
}

fun function_composition() {
    not_implemented()
}

sealed class Either<out L, out R> {
    data class Left<out L>(val left: L): Either<L, Nothing>()
    data class Right<out R>(val right: R): Either<Nothing, R>()
    companion object {
        fun <R> right(value: R): Either<Nothing, R> = Either.Right(value)
        fun <L> left(value: L): Either<L, Nothing> = Either.Left(value)
    }
}
fun implement_the_Either_Monad_design_pattern() {
    not_implemented()
}

typealias MemoizedFun<P, R> = (P) -> R;
class Memoizer<P, R> private constructor () {
    private val map = ConcurrentHashMap<P, R>()
    private fun doMemoize(function: MemoizedFun<P, R>): MemoizedFun<P, R> = { param: P ->
        map.computeIfAbsent(param) { param: P ->
            function(param)
        }
    }

    companion object {
        fun <T, U> memoize(fn: MemoizedFun<T, U>): MemoizedFun<T, U> = Memoizer<T, U>().doMemoize(fn)
    }
}

fun <P, R> (MemoizedFun<P, R>).memoize() : MemoizedFun<P, R> = Memoizer.memoize<P, R>(this)

@OptIn(ExperimentalTime::class)
fun approach_to_automatic_functions_memoization() {
    code("""
    typealias MemoizedFun<P, R> = (P) -> R;
    class Memoizer<P, R> private constructor () {
        private val map = ConcurrentHashMap<P, R>()
        private fun doMemoize(function: MemoizedFun<P, R>): MemoizedFun<P, R> = { param: P ->
            map.computeIfAbsent(param) { param: P ->
                function(param)
            }
        }
    
        companion object {
            fun <T, U> memoize(fn: MemoizedFun<T, U>): MemoizedFun<T, U> = Memoizer<T, U>().doMemoize(fn)
        }
    }
    fun <P, R> (MemoizedFun<P, R>).memoize() : MemoizedFun<P, R> = Memoizer.memoize<P, R>(this)

    fun fn() {
        fun factorial(n: BigInteger): BigInteger =
            if (n == BigInteger.ONE) n else n * factorial(n - BigInteger.ONE)
        val memoizedFactorial = ::factorial.memoize()
        val n = 100.toBigInteger()
        measureTime { println(factorial(n)) }.also {
            println("Execution time ${'$'}it")
        }.run {
            measureTime { println(memoizedFactorial(n)) }.also {
                println("Execution time ${'$'}it")
                println("${'$'}{(((this - it) / this) * 100).roundToInt()}% faster")
            }
        }
    }
    """)
    run {
        fun factorial(n: BigInteger): BigInteger =
            if (n == BigInteger.ONE) n else n * factorial(n - BigInteger.ONE)

        val memoizedFactorial = ::factorial.memoize()

        val n = 100.toBigInteger()

        measureTime { println(factorial(n)) }.also {
            println("Execution time $it")
        }.run {
            measureTime { println(memoizedFactorial(n)) }.also {
                println("Execution time $it")
                println("${(((this - it) / this) * 100).roundToInt()}% faster")
            }
        }
    }

    comment("""
    Question: Above memoize only support one-argument function. How to support multiple arguments? curry? 
    """)
}