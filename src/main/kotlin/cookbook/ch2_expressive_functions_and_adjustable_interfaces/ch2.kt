package cookbook.ch2_expressive_functions_and_adjustable_interfaces

import code
import comment
import h1
import h2

fun ch2() {
    h1("Ch1. Expressive Functions and Adjustable Interfaces")
    h2(arrayOf(::declaring_adjustable_functions_with_default_paramaters))
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