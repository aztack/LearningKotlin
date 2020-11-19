package understanding_kotlin_coroutine.ch1_introducing_asynchronous_application_design

import comment
import h1
import h2
import h3
import html
import kotlinx.coroutines.delay
import run
import kotlin.concurrent.thread

fun ch1() {
    h1("第一章 异步程序设计介绍")
    html("## 1.1 异步程序的概念")
    sec1_1_2()
}

fun sec1_1_2() {
    h3("1.1.2 程序的执行")
    comment("""
    Asynchrony, in computer programming, refers to the occurrence of events independent of the main program flow and ways to deal with such events.
    """.trimIndent())
    run {
        sync_exec()
    }
    run {
        async_exec()
    }
}

fun sync_exec() {
    println("A")
    println("B")
}

fun async_exec() {
    val task = {
        println("C")
    }
    println("A")
    thread(block = task)
    println("B")
}