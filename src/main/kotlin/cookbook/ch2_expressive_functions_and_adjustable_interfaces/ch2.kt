package cookbook.ch2_expressive_functions_and_adjustable_interfaces

import LightBulb
import code
import comment
import h1
import h2
import html
import link
import list
import run
import text
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

fun ch2() {
    h1("Ch2. Expressive Functions and Adjustable Interfaces")
    h2(arrayOf(
        ::declaring_adjustable_functions_with_default_paramaters,
        ::declaring_interfaces_containing_dfault_implementations,
        ::extending_functionalities_of_classes,
        ::destructuring_types,
        ::inlining_parameters_of_closure_type
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
        "You can only inline some of the lambdas by mark others by `noinline` like this: `inline fun foo(inlined: () -> Uint, noinline notInlined: () -> Uint)`"
    ))
}