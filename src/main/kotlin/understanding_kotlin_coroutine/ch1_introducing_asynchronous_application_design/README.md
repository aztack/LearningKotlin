# 第一章 异步程序设计介绍

## 1.1 异步程序的概念

### 1.1.1 程序的执行

[Asynchrony (computer programming)](https://en.wikipedia.org/wiki/Asynchrony_(computer_programming))

> Asynchrony, in computer programming, refers to the occurrence of events independent of the main program flow and ways to deal with such events.

### 1.1.2 异步与回调

```kotlin
// 同步代码
println("A")
println("A")
```


```kotlin
// 异步代码
val task = {println("C")}
println("A")
thread(block = task)
println("B")
```

## 1.2 异步程序设计的关键问题


## 1.3 常见异步程序设计思路

