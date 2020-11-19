package understanding_kotlin_coroutine.ch1_introducing_asynchronous_application_design

import code
import comment
import h1
import h2
import h3
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import list
import not_implemented
import run
import runAsync
import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun ch1() {
    h1("第一章 异步程序设计介绍")
    h2(arrayOf(
        ::异步程序设计介绍,
        ::异步程序设计的关键问题,
        ::常见异步程序设计思路
    ))
}

fun 异步程序设计介绍() {
    sec1_1_1()
    sec1_1_2()
    sec1_1_3()
}

fun sec1_1_1() {
    h3("1.1.1 程序的执行")
    list(arrayOf(
        "指令的执行顺序有两种，按顺序的执行的情形叫做同步执行，反之则称为异步执行。",
        "世界中的事件不一定是按顺序发生的。多个事件之间可能存在或不存在以来关系。",
        "程序需要用某种方式来描述这些事件彼此相关或不相关的事件"
    ))

    comment("""
        
    """.trimIndent())
}

fun sec1_1_2() {
    h3("1.1.2 异步与回调")
    comment("""
    Asynchrony, in computer programming, refers to the occurrence of events independent of the main program flow and ways to deal with such events.
    """.trimIndent())

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

    code("""
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
    sync_exec()
    async_exec()
    """.trimIndent())
    run {
        sync_exec()
    }
    runAsync {
        async_exec()
    }
}

fun sec1_1_3() {
    h3("回调地狱")
    not_implemented()
}

fun 异步程序设计的关键问题() {
    sec1_2_1()
    sec1_2_2()
    sec1_2_3()
    sec1_2_4()
}

fun sec1_2_1() {
    h3("结果传递")
}
fun sec1_2_2() {
    h3("异常处理")
}
fun sec1_2_3() {
    h3("取消响应")
}
fun sec1_2_4() {
    h3("复杂分支")
}

fun 常见异步程序设计思路() {
    h3("1.3 常见的异步程序设计思路")
    list(arrayOf(
        "JDK1.5引入的`Future<T>`",
        "JDK1.8引入的`CompletableFuture<T>`",
        "Promise与async/await",
        "响应式编程",
        "协程"
    ))

    comment("""
    其中JDK的CompletableFuture<T>是对Future<T>的完善。其API的使用已经很接近Promise的用法了。
    """.trimIndent())

    comment("""
    Promise与async/await对于前端程序员来说就很熟悉了，不再赘述。
    """.trimIndent())

    comment("""
    以下几个语言也支持async/await特性：
    """.trimIndent())

    list(arrayOf(
        "C# 5.0",
        "Python 3.5",
        "Rust 1.39.0"
    ))

    comment("""
    响应式编程(Reactive Programming)主要关注的是`数据流的变换和流转`。
    因此它更注重描述数据的输入和输出之间的关系。
    """.trimIndent())
}