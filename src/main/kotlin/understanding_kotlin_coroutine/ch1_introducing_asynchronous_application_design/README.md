
# 第一章 异步程序设计介绍

## 1. 异步程序设计介绍

### 1.1.1 程序的执行
- 指令的执行顺序有两种，按顺序的执行的情形叫做同步执行，反之则称为异步执行。
- 世界中的事件不一定是按顺序发生的。多个事件之间可能存在或不存在以来关系。
- 程序需要用某种方式来描述这些事件彼此相关或不相关的事件





### 1.1.2 异步与回调

> Asynchrony, in computer programming, refers to the occurrence of events independent of the main program flow and ways to deal with such events.


```kotlin
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
```

Output:

```
A
B

```
Output:

```
A
B
C

```

### 回调地狱

 > not implemented or left blank intentionally

## 2. 异步程序设计的关键问题

### 结果传递

### 异常处理

### 取消响应

### 复杂分支

## 3. 常见异步程序设计思路

### 1.3 常见的异步程序设计思路
- JDK1.5引入的`Future<T>`
- JDK1.8引入的`CompletableFuture<T>`
- Promise与async/await
- 响应式编程
- 协程


> 其中JDK的CompletableFuture<T>是对Future<T>的完善。其API的使用已经很接近Promise的用法了。


> Promise与async/await对于前端程序员来说就很熟悉了，不再赘述。


> 以下几个语言也支持async/await特性：

- C# 5.0
- Python 3.5
- Rust 1.39.0

