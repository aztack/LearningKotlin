package cookbook.ch2_expressive_functions_and_adjustable_interfaces

import LightBulb
import code
import comment
import h1
import h2
import html
import img
import link
import list
import run
import text
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import com.google.gson.*

fun ch2() {
    h1("Ch2. Expressive Functions and Adjustable Interfaces")
    h2(arrayOf(
        ::declaring_adjustable_functions_with_default_paramaters,
        ::declaring_interfaces_containing_dfault_implementations,
        ::extending_functionalities_of_classes,
        ::destructuring_types,
        ::inlining_parameters_of_closure_type,
        ::infix_notations_for_functions,
        ::smart_types_checking_with_generic_reified_parameters,
        ::overloading_operators
    ))
}

fun declaring_adjustable_functions_with_default_paramaters() {
    fun calculateDisplacement(initialSpeed: Float = 0f, acceleration: Float, duration: Long = 10)
            = initialSpeed * duration + 0.5 * acceleration * duration * duration
    code("""
    fun calculateDisplacement(initialSpeed: Float = 0f, acceleration: Float, duration: Long = 10)
        = initialSpeed * duration + 0.5 * acceleration * duration * duration
    println(calculateDisplacement(acceleration =  9.81f, duration = 1000))
    println(calculateDisplacement(acceleration =  9.81f))
    """)
    run {
        println(calculateDisplacement(acceleration =  9.81f, duration = 1000))
        println(calculateDisplacement(acceleration =  9.81f))
    }
    comment("""
    Default parameters in Kotlin can be put any where in the parameter list, not necessarily the last like TypeScript.
    Function arguments can have different order than parameter list and can be named.
    Above language features make functions in Kotlin adjustable.
    """)
}
interface EmailValidator {
    var input: String
    fun isValid(): Boolean = input.contains("@")
    fun getUserName(): String = input.substringBefore("@")
}

class RegistrationForm(override var input: String) : EmailValidator {
    fun onInputTextUpdated(newText: String) {
        this.input = newText
        if (isValid()) {
            println("Email is correct, thanks ${getUserName()}")
        } else {
            println("Wait! You've entered a wrong email: $input")
        }
    }
}
interface A {
    fun foo(){
        println("A::foo")
    }
}
interface B {
    fun foo(){
        println("B::foo")
    }
}
class MyClass: A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
        println("I'm the first one here")
    }
}
fun declaring_interfaces_containing_dfault_implementations() {
    code("""
    interface EmailValidator {
        var input: String
        fun isValid(): Boolean = input.contains("@")
        fun getUserName(): String = input.substringBefore("@")
    }
    /* override interface properties */
    class RegistrationForm(override var input: String) : EmailValidator {
        fun onInputTextUpdated(newText: String) {
            this.input = newText
            if (isValid()) {
                println("Email is correct, thanks ${'$'}{getUserName()}")
            } else {
                println("Wait! You've entered a wrong email: ${'$'}input")
            }
        }
    }
    RegistrationForm("aztack@163.com").onInputTextUpdated("aztack163.com");
    """)
    run {
        RegistrationForm("aztack@163.com").onInputTextUpdated("aztack163.com");
    }
    comment("""
    Important: `interface` can have default function implementation but we are not able to instantiate default values
    for interface properties. Unlike the class properties, properties of an interface are abstract. They don't have
    backing fields that could hold a current value.
    
    """)

    code("""
    interface A {
        fun foo(){
            println("A::foo")
        }
    }
    interface B {
        fun foo(){
            println("B::foo")
        }
    }
    class MyClass: A, B {
        override fun foo() {
            super<A>.foo()
            super<B>.foo()
            println("I'm the first one here")
        }
    }
    MyClass().foo()
    """)

    run {
        MyClass().foo()
    }
    comment("""
    You can call methods of inherited interface with `super<interface_name>.method(args)`
    """)
}

val <T> List<T>.lastIndex: Int get() = size - 1

fun extending_functionalities_of_classes() {
    code("""
        fun <T> Array<T>.swap(a: T, b: T) {
            val positionA = this.indexOf(a)
            val positionB = this.indexOf(b)
            if (positionA >= 0 || positionB >= 0 && positionA != positionB) {
                val tmp = this[positionA]
                this[positionA] = this[positionB]
                this[positionB] = tmp
            }
        }

        val array = arrayOf("a", "b", "c", "d")
        array.swap("c", "b")
        println("[${'$'}{array.joinToString(",")}]")   
    """)
    run {
        fun <T> Array<T>.swap(a: T, b: T) {
            val positionA = this.indexOf(a)
            val positionB = this.indexOf(b)
            if (positionA >= 0 || positionB >= 0 && positionA != positionB) {
                val tmp = this[positionA]
                this[positionA] = this[positionB]
                this[positionB] = tmp
            }
        }

        val array = arrayOf("a", "b", "c", "d")
        array.swap("c", "b")
        println("[${array.joinToString(",")}]")
    }
    comment("""
    You can extend a class like this:
    `fun SomeClass.newFunctionName(args): ReturnType { ... }
    """)

    code("""
    // at top level
    val <T> List<T>.lastIndex: Int get() = size - 1
    fun f() {
        val list = listOf("a", "b", "c")
        println(list.lastIndex)
    }
    """)
    run {
        val list = listOf("a", "b", "c")
        println(list.lastIndex)
    }
    comment("""
    Properties extension MUST declared at top level, not inside a function    
    """)
}


fun destructuring_types() {
    html(link("Destructuring Declarations", "https://kotlinlang.org/docs/reference/multi-declarations.html"))
    html("The standard library provides `Pair` and `Triple`")
    code("""
    // LightBulb.java
    public class LightBulb {
        private final int id;
        private boolean turnedOn = false;
        public LightBulb(int id) {
            this.id = id;
        }
        public void setTurnedOn(boolean turnedOn) {
            this.turnedOn = turnedOn;
        }
        public boolean getTurnedOn() {
            return turnedOn;
        }
        public int getId() {
            return id;
        }
    }
    operator fun LightBulb.component1() = this.id
    operator fun LightBulb.component2() = this.turnedOn

    val bulb = LightBulb(1)
    bulb.turnedOn = true
    val (_ , turnedOn) = bulb
    println("Light bulb is turned ${'$'}{if (turnedOn) "on" else "off"}")
    """)
    operator fun LightBulb.component1() = this.id
    operator fun LightBulb.component2() = this.turnedOn
    run {
        val bulb = LightBulb(1)
        bulb.turnedOn = true
        val (_ , turnedOn) = bulb
        println("Light bulb is turned ${if (turnedOn) "on" else "off"}")
    }
    list(arrayOf(
        "Destructuring is available for data classes out of the box",
        "Destructuring is NOT available explicitly with custom, non-data class and Java classes",
        "You can implement destructuring for them by extend them with `operator fun componentN()`",
        "You can skip unnecessary variables with underscore: `_` like in other programming languages."
    ))
    comment("""
    Important: Destructuring declarations in Kotlin are `position-based`, NOT name-based.
    `N` in `componentN()` is the position of property (begin with 1, NOT 0).
    [Data class](https://kotlinlang.org/docs/reference/data-classes.html)
    """)

    code("""
    val (name, domain) = "aztack@163.com".split("@")
    println("name: ${'$'}name, domain: ${'$'}domain")   
     
    val (key, value) = (1 to "banana")
    println("key: ${'$'}key, value: ${'$'}value")
    
    val l1 = LightBulb(1)
    val l2 = LightBulb(2)
    l1.turnedOn = true
    println(listOf(l1, l2)
            .filter { (_, isOn) -> isOn }
            .map{(id, _) -> id})
    """)
    run {
        val (name, domain) = "aztack@163.com".split("@")
        println("name: $name, domain: $domain")

        val (key, value) = (1 to "banana")
        println("key: $key, value: $value")

        val l1 = LightBulb(1)
        val l2 = LightBulb(2)
        l1.turnedOn = true
        println(listOf(l1, l2)
                .filter { (_, isOn) -> isOn }
                .map{(id, _) -> id})
    }
    comment("""
    underscore can also be used in array-destructuring, pair-destructuring, parameter-destructuring   
    """)
}

inline fun performHavingLock(lock: Lock, task: () -> Unit) {
    lock.lock()
    try {
        task()
    } finally {
        lock.unlock()
    }
}

fun inlining_parameters_of_closure_type() {
    code("""
    inline fun performHavingLock(lock: Lock, task: () -> Unit) {
        lock.lock()
        try {
            task()
        } finally {
            lock.unlock()
        }
    }
    fun inlining_parameters_of_closure_type {
        performHavingLock(ReentrantLock()) {
            println("Wait for it!")
        }
    }
    """)
    text("is translate into:")
    code("""
       public static final void inlining_parameters_of_closure_type() {
          Lock lock$${'$'}iv = (Lock)(new ReentrantLock());
          int${'$'}i$${'$'}f$${'$'}performHavingLock = false;
          lock$${'$'}iv.lock();

          try {
             int var2 = false;
             String var3 = "Wait for it!";
             boolean var4 = false;
             System.out.println(var3);
          } finally {
             lock$${'$'}iv.unlock();
          }
       }
    """)
    run {
        performHavingLock(ReentrantLock()) {
            println("Wait for it!")
        }
    }
    list(arrayOf(
        "inline function MUST be a top level declaration",
        "You can only inline some of the lambdas by mark others by `noinline` like this: `inline fun foo(inlined: () -> Uint, noinline notInlined: () -> Uint)`",
        "Kotlin also allows declaring inline class properties"
    ))
}

fun infix_notations_for_functions() {
    code("""
    val pair = 1 to 3
    println(pair)

    val seq1 = 1 until 3
    val seq2 = 3 downTo 1
    println(seq1)
    println(seq2)
    """)
    run {
        val pair = 1 to 3
        println(pair)

        val seq1 = 1 until 3
        val seq2 = 3 downTo 1
        println(seq1)
        println(seq2)
    }
    list(arrayOf(
        "`to()` in `Tuples.kt`: `public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)`",
        "infix functions are displayed in the same color with normal function in IntelliJ：${img("color","https://user-images.githubusercontent.com/782871/98649756-eaf37600-2372-11eb-8339-883b12a40500.png")}"
    ))
}

inline fun <reified T> Gson.fromJson(json: String): T {
    return fromJson(json, T::class.java)
}
data class TestModel(
    val id: Int,
    val description: String
)
fun smart_types_checking_with_generic_reified_parameters() {
    code("""
    //Only reified generic parameter can be accessed during runtime
    inline fun <reified T> Gson.fromJson(json: String): T {
        return fromJson(json, T::class.java)
    }
    data class TestModel(
        val id: Int,
        val description: String
    )
    fun fn() {
        val json = ""${'"'}{"id":1,"description":"Parsed from string"}""${'"'}
        val response = Gson().fromJson<TestModel>(json);
        println(response)
    }
    """)
    run {
        val json = """{"id":1,"description":"Parsed from string"}"""
        val response = Gson().fromJson<TestModel>(json);
        println(response)
    }
    list(arrayOf(
        "Only reified generic parameter can be accessed during runtime",
        "Using reified generic parameter, you can avoid pass java class around"
    ))
}

data class Position(val x: Float, val y: Float, val z: Float) {
    operator fun plus(other: Position) = Position(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Position) = Position(x - other.x, y - other.y, z - other.z)
}

fun overloading_operators() {
    code("""
    data class Position(val x: Float, val y: Float, val z: Float) {
        operator fun plus(other: Position) = Position(x + other.x, y + other.y, z + other.z)
        operator fun minus(other: Position) = Position(x - other.x, y - other.y, z - other.z)
    }
    fun fn() {
        val p1 = Position(132.5f, 4f, 3.43f)
        val p2 = Position(1.5f, 400f, 11.56f)
        println(p1 - p2) // cmd+click on minus sign will jump to above Position::minus()
    }
    """)
    run {
        val p1 = Position(132.5f, 4f, 3.43f)
        val p2 = Position(1.5f, 400f, 11.56f)
        println(p1 - p2)
    }
    list(arrayOf(
        "[Kotlin Reference: Operator Overloading](https://kotlinlang.org/docs/reference/operator-overloading.html)",
        "Implement invoke(...) will make an object callable",
        "Implement get(...) will make an object act like a Map"
    ))
}