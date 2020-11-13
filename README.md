![Kotlin Cookbook Code](https://user-images.githubusercontent.com/782871/98759671-8be23f80-240c-11eb-985c-5fed790c5ffa.png)

# Ch1. Ranges, Progressions, and Sequences

## 1. Exploring the use of rang expression to iterate through alphabet characters

```kotlin
 for (letter in 'Z' downTo 'A') print(letter)")
```

Output:

```
ZYXWVUTSRQPONMLKJIHGFEDCBA
```

> downTo is an infix function of Char


```kotlin
 val ten = 10
 for (letter in 9 downTo 'A') print(letter)")
```

Output:

```
109876543210
```

> here, downTo is an infix function of Int


```kotlin
 val oneToTen = 1..10
 for (number in oneToTen) print(number)
```

Output:

```
10987654321
```

## 2. Traversing through ranges using progression with a custom step value

```kotlin
 val progression: IntProgression = 0..10 step 2
 for (i in progression) print(i)    
```

Output:

```
0246810
```

## 3. Building custom progressions to traverse dates

 > not implemented or left blank intentionally

## 4. Using range expressions with flow control statements

```kotlin
 val randomInt = Random().nextInt()
 val range = 0..10
 if (randomInt in range) {
     println("$randomInt belongs to $range\")
 } else {
     println(\"$randomInt does't not belong to $range\")
 }
```

Output:

```
183057113 doesn't not belong to 0..10

```
- There is no good way to escape dollar sign in Kotlin here document, you have to use string interpolation: `${'$'}`
- `in` is an operator. see [Operator overloading](https://kotlinlang.org/docs/reference/operator-overloading.html)
- [Provide a way for escaping the dollar sign symbol ("$") in raw strings and string templates](https://youtrack.jetbrains.com/issue/KT-2425)


```kotlin
 var score = Random().nextInt(100)
 var grade = when(score) {
     in 90..100 -> "Excellent"
     in 75 until 90 -> "Great"
     in 60 until 75 -> "OK"
     else -> "Bad"
 }
 print("$score belongs to grade $grade")
```

Output:

```
65 belongs to grade OK
```

> You can inspect Kotlin bytecode in IntelliJ>Tools>Kotlin>Show Kotlin bytecode


> Above `when` expression is translate into following bytecode:

<pre>
        LINENUMBER 104 L1
        ILOAD 1
        ISTORE 3
       L2
        LINENUMBER 105 L2
        BIPUSH 100
        BIPUSH 90
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L3
        POP
        GOTO L4
       L3
        ILOAD 4
        IF_ICMPLT L4
       L5
        LDC "Excellent"
        GOTO L6
       L4
        LINENUMBER 106 L4
        BIPUSH 90
        BIPUSH 75
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L7
        POP
        GOTO L8
       L7
        ILOAD 4
        IF_ICMPLE L8
       L9
        LDC "Great"
        GOTO L6
       L8
        LINENUMBER 107 L8
        BIPUSH 75
        BIPUSH 60
        ILOAD 3
        ISTORE 4
        ILOAD 4
        IF_ICMPLE L10
        POP
        GOTO L11
       L10
        ILOAD 4
        IF_ICMPLE L11
       L12
        LDC "OK"
        GOTO L6
       L11
        LINENUMBER 108 L11
        LDC "Bad"
       L13
        LINENUMBER 104 L13
       L6
        ASTORE 2
       L14</pre>

## 5. Discovering the concept of sequences

`Sequence delays any operations on its elements until they are finally consumed.`


```kotlin
 val seq = ('a'..'z').asSequence()
 for (c in seq.map{it.toUpperCase()}.take(3)) print(c)
```

Output:

```

sequence:
in map: a
A
in map: b
B
in map: c
C

range:
in map: a
in map: b
in map: c
in map: d
in map: e
in map: f
in map: g
in map: h
A
B
C

```

> map() was applied only to the first 3 elements of the sequence,
> event though the take() functtion was called after the mapping.
> see [Java Streams vs. Kotlin Sequences](https://proandroiddev.com/java-streams-vs-kotlin-sequences-c9ae080abfdc)


## 6. Applying sequences to solve alghorithmic problems

```kotlin
 fun fibonacci(): Sequence<Int> {
     return generateSequence(1 to 1) {
         Pair(it.second, it.first + it.second)
     }.map { it.first }
 }
 println(fibonacci().take(6).toList())
```

Output:

```
[1, 1, 2, 3, 5, 8]

```

> `generateSequence` is like a mixture of `reduce` and `map`:
> it take an seed value (like reduce) optionally and take the return value of every call to the lambda
> as sequence elements


# Ch2. Expressive Functions and Adjustable Interfaces

## 1. Declaring adjustable functions with default paramaters

```kotlin
 fun calculateDisplacement(initialSpeed: Float = 0f, acceleration: Float, duration: Long = 10)
     = initialSpeed * duration + 0.5 * acceleration * duration * duration
 println(calculateDisplacement(acceleration =  9.81f, duration = 1000))
 println(calculateDisplacement(acceleration =  9.81f))
```

Output:

```
4905000.20980835
490.50002098083496

```

> Default parameters in Kotlin can be put any where in the parameter list, not necessarily the last like TypeScript.
> Function arguments can have different order than parameter list and can be named.
> Above language features make functions in Kotlin adjustable.


## 2. Declaring interfaces containing default implementations

```kotlin
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
             println("Email is correct, thanks ${getUserName()}")
         } else {
             println("Wait! You've entered a wrong email: $input")
         }
     }
 }
 RegistrationForm("aztack@163.com").onInputTextUpdated("aztack163.com");
```

Output:

```
Wait! You've entered a wrong email: aztack163.com

```

> Important: `interface` can have default function implementation but we are not able to instantiate default values
> for interface properties. Unlike the class properties, properties of an interface are abstract. They don't have
> backing fields that could hold a current value.
> 


```kotlin
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
```

Output:

```
A::foo
B::foo
I'm the first one here

```

> You can call methods of inherited interface with `super<interface_name>.method(args)`


## 3. Extending functionalities of classes

```kotlin
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
```

Output:

```
[a,c,b,d]

```

> You can extend a class like this:
> `fun SomeClass.newFunctionName(args): ReturnType { ... }


```kotlin
 // at top level
 val <T> List<T>.lastIndex: Int get() = size - 1
 fun f() {
     val list = listOf("a", "b", "c")
     println(list.lastIndex)
 }
```

Output:

```
2

```

> Properties extension MUST declared at top level, not inside a function    


## 4. Destructuring types
[Destructuring Declarations](https://kotlinlang.org/docs/reference/multi-declarations.html)
The standard library provides `Pair` and `Triple`

```kotlin
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
 println("Light bulb is turned ${if (turnedOn) "on" else "off"}")
```

Output:

```
Light bulb is turned on

```
- Destructuring is available for data classes out of the box
- Destructuring is NOT available explicitly with custom, non-data class and Java classes
- You can implement destructuring for them by extend them with `operator fun componentN()`
- You can skip unnecessary variables with underscore: `_` like in other programming languages.


> Important: Destructuring declarations in Kotlin are `position-based`, NOT name-based.
> `N` in `componentN()` is the position of property (begin with 1, NOT 0).
> [Data class](https://kotlinlang.org/docs/reference/data-classes.html)


```kotlin
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
```

Output:

```
name: aztack, domain: 163.com
key: 1, value: banana
[1]

```

> underscore can also be used in array-destructuring, pair-destructuring, parameter-destructuring   


## 5. Inlining parameters of closure type

```kotlin
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
```


is translate into:


```kotlin
 public static final void inlining_parameters_of_closure_type() {
    Lock lock$$iv = (Lock)(new ReentrantLock());
    int$i$$f$$performHavingLock = false;
    lock$$iv.lock();
 
    try {
       int var2 = false;
       String var3 = "Wait for it!";
       boolean var4 = false;
       System.out.println(var3);
    } finally {
       lock$$iv.unlock();
    }
 }
```

Output:

```
Wait for it!

```
- inline function MUST be a top level declaration
- You can only inline some of the lambdas by mark others by `noinline` like this: `inline fun foo(inlined: () -> Uint, noinline notInlined: () -> Uint)`
- Kotlin also allows declaring inline class properties


## 6. Infix notations for functions

```kotlin
 val pair = 1 to 3
 println(pair)
 
 val seq1 = 1 until 3
 val seq2 = 3 downTo 1
 println(seq1)
 println(seq2)
```

Output:

```
(1, 3)
1..2
3 downTo 1 step 1

```
- `to()` in `Tuples.kt`: `public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)`
- infix functions are displayed in the same color with normal function in IntelliJ：![color](https://user-images.githubusercontent.com/782871/98649756-eaf37600-2372-11eb-8339-883b12a40500.png)


## 7. Smart types checking with generic reified parameters

```kotlin
 //Only reified generic parameter can be accessed during runtime
 inline fun <reified T> Gson.fromJson(json: String): T {
     return fromJson(json, T::class.java)
 }
 data class TestModel(
     val id: Int,
     val description: String
 )
 fun fn() {
     val json = """{"id":1,"description":"Parsed from string"}"""
     val response = Gson().fromJson<TestModel>(json);
     println(response)
 }
```

Output:

```
TestModel(id=1, description=Parsed from string)

```
- Only reified generic parameter can be accessed during runtime
- Using reified generic parameter, you can avoid pass java class around


## 8. Overloading operators

```kotlin
 data class Position(val x: Float, val y: Float, val z: Float) {
     operator fun plus(other: Position) = Position(x + other.x, y + other.y, z + other.z)
     operator fun minus(other: Position) = Position(x - other.x, y - other.y, z - other.z)
 }
 fun fn() {
     val p1 = Position(132.5f, 4f, 3.43f)
     val p2 = Position(1.5f, 400f, 11.56f)
     println(p1 - p2) // cmd+click on minus sign will jump to above Position::minus()
 }
```

Output:

```
Position(x=131.0, y=-396.0, z=-8.13)

```
- [Kotlin Reference: Operator Overloading](https://kotlinlang.org/docs/reference/operator-overloading.html)
- Implement invoke(...) will make an object callable
- Implement get(...) will make an object act like a Map


# Ch3. Shaping code with Kotlin functional programming

## 1. Working effectively with lambda expressions

 > not implemented or left blank intentionally

## 2. Discovering basic scoping functions

The Kotlin standard library contains several functions whose sole purpose is to execute a block of code within the context of an object


```kotlin
 fun getPlayers() : List<Player> = listOf(
     Player("Stefan Madej", 109),
     Player("Adam Ondra", 323),
     Player("Chris Charma", 329)
 )
 getPlayers().let { it ->
     it.also {
         println("${it.size} players records fetched")
         println(it)
     }.let { it ->
         it.sortedByDescending { it.bestScore }.first()
     }.apply {
         val name = this.name
         print("Best Player: $name")
     }
 }
```

Output:

```
3 players records fetched
[Player(name=Stefan Madej, bestScore=109), Player(name=Adam Ondra, bestScore=323), Player(name=Chris Charma, bestScore=329)]
Best Player: Chris Charma
Player(name=Chris Charma, bestScore=329)

```

Detail implementation of [Scope functions](https://kotlinlang.org/docs/reference/scope-functions.html): （some code are removed for for simplicity sake


```kotlin
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
```


There are two main differences between each scope function:

- The way to refer to the context object
- The return value.

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

> [What does “.()” mean in Kotlin?](https://stackoverflow.com/questions/44427382/what-does-mean-in-kotlin):
> 
> There is a misunderstanding that T.() -> Y is (T.()) -> Y, but actually is T.(()->Y). As we know (X)->Y is a lambda, so T.(X)->Y is an extension on T.


Conventions for using `apply`


```kotlin
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
```


Conventions for using `also`


```kotlin
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
```


Conventions for using `let`


```kotlin
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
```


Conventions for using `with`


```kotlin
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
```


Conventions for using `run`


```kotlin
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
```


## 3. Initializing objects the clean way using the run scoping (Kotlin reflection is not available)

```kotlin
 Calendar.Builder().run {
     setCalendarType("iso8601")
     setDate(2020, 11, 11)
     setTimeZone(TimeZone.getTimeZone("GMT+8:00"))
     build()
 }.also {
     print(it.time)
 }
```

Output:

```
Fri Dec 11 00:00:00 CST 2020
```

## 4. Working with higher order functions

 > not implemented or left blank intentionally

## 5. Functions curring

 > not implemented or left blank intentionally

## 6. Function composition

 > not implemented or left blank intentionally

## 7. Implement the Either Monad design pattern

 > not implemented or left blank intentionally

## 8. Approach to automatic functions memoization

```kotlin
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
         println("Execution time $it")
     }.run {
         measureTime { println(memoizedFactorial(n)) }.also {
             println("Execution time $it")
             println("${(((this - it) / this) * 100).roundToInt()}% faster")
         }
     }
 }
```

Output:

```
93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000
Execution time 3.20ms
93326215443944152681699238856266700490715968264381621468592963895217599993229915608941463976156518286253697920827223758251185210916864000000000000000000000000
Execution time 1.57ms
51% faster

```

> Question: Above memoize only support one-argument function. How to support multiple arguments? curry? 


# Ch4. Powerful Data Processing

## 1. Composing and consuming collections the easy way

```kotlin
 val sentMessages = listOf(
     Message("Hi Agat, any plans for the evening?", "Samuel"),
     Message("Great, I'll take some wine too", "Samuel")
 )
 val inboxMessages = mutableListOf(
     Message("Let's go out of town and watch the stars tonight!", "Agat"),
     Message("Excelent!", "Agat")
 )
 val allMessages = sentMessages + inboxMessages
 allMessages.forEach{ (text, _) ->
     println(text)
 }
```

Output:

```
Hi Agat, any plans for the evening?
Great, I'll take some wine too
Let's go out of town and watch the stars tonight!
Excellent!

```

> Collection<T> override operator plus/minus. You can union/subtract collection with `+` and `-`    


## 2. Filtering datasets

```kotlin
 (sentMessages + inboxMessages).filter { it.sender == "Samuel" }.forEach(::println)
```

Output:

```
Message(text=Hi Agat, any plans for the evening?, sender=Samuel, timestamp=2020-11-13T03:41:54.330Z)
Message(text=Great, I'll take some wine too, sender=Samuel, timestamp=2020-11-13T03:41:54.330Z)

```
- `::` in Kotlin is about meta-programming, including method references, property references and class literals. 
- [Kotlin Reference: Reflect](https://kotlinlang.org/docs/reference/reflection.html)
- [kotlin.reflect](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/)


## 3. Automatic null removal

```kotlin
 data class News(val title: String, val url: String) {
     override fun toString(): String = "[$title]($url)"
 }
 fun getNews() = listOf(
     News("Kotlin 1.4.0 is out!", "https://blog.jetbrains.com/kotlin"),
     News("Google launches Android KTX Kotlin extensions for developers", "http://android-developers.googleblog.com"),
     null,
     null,
     News("How to Pick a Career", "https://waitbutwhy.com")
 )
 getNews().filterNotNull()
     .forEachIndexed { index, news ->
         println("$index. $news")
     }
```

Output:

```
0. [Kotlin 1.4.0 is out!](https://blog.jetbrains.com/kotlin)
1. [Google launches Android KTX Kotlin extensions for developers](http://android-developers.googleblog.com)
2. [How to Pick a Career](https://waitbutwhy.com)

```

## 4. Sorting data with custom comparator

```kotlin
 data class Message2(val text: String,
                 val sender: String,
                 val receiver: String,
                 val time: Instant = Instant.now());
 val sentMessages = listOf(
     Message2(
         "I'm programming in Kotlin, of course",
         "Samuel",
         "Agat",
         Instant.parse("2018-12-18T10:13:35Z")
     ),
     Message2(
         "Sure!",
         "Samuel",
         "Agat",
         Instant.parse("2018-12-18T10:15:35Z")
     )
 )
 val inboxMessages = mutableListOf(
     Message2(
         "Hey Sam, any plans for the evening?",
         "Samuel",
         "Agat",
         Instant.parse("2018-12-18T10:12:35Z")
     ),
     Message2(
         "That's cool, can I join you?",
         "Samuel",
         "Agat",
         Instant.parse("2018-12-18T10:14:35Z")
     )
 )
 val allMessages = sentMessages + inboxMessages
 allMessages.sortedBy { it.time }
     .forEach { println(it.text) }
```

Output:

```
Hey Sam, any plans for the evening?
I'm programming in Kotlin, of course
That's cool, can I join you?
Sure!

```

## 5. Building strings based on dataset elements

 > not implemented or left blank intentionally

## 6. Dividing data into subsets

```kotlin
 val messages = listOf(
     Message("Any plans for the evening?"),
     Message("Learning Kotlin, of course"),
     Message("I'm going to watch the new Star Wars movie"),
     Message("Would u like to join?"),
     Message("Meh, I don't know"),
     Message("See you later!"),
     Message("I like ketchup"),
     Message("Did you send CFP for Kotlin Conf?"),
     Message("Sure!")
 )
 messages.windowed(4, partialWindows = true, step = 4) {
     it.map { it.text }
 }.run {
     forEachIndexed { index, it ->
         println("Group ${index + 1}. $it")
     }
 }
```

Output:

```
Group 1. [Any plans for the evening?, Learning Kotlin, of course, I'm going to watch the new Star Wars movie, Would u like to join?]
Group 2. [Meh, I don't know, See you later!, I like ketchup, Did you send CFP for Kotlin Conf?]
Group 3. [Sure!]

```

> You can find many useful extension functions for Collections [here](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/)


## 7. Data transformation with map and flatMap

 > not implemented or left blank intentionally

## 8. Folding and reducing data sets

```kotlin
 data class Track(val title: String, val durationInSeconds: Int)
 data class Album(val name: String, val tracks: List<Track>)
 
 fun Album.getStartTime(name: String): Int {
     val index = tracks.indexOfFirst { track -> track.title == name}
     if (index < 0) throw IllegalArgumentException("No such track")
     return tracks.take(index)
         .map {(name, duration) -> duration}
         .fold(0) {acc, i -> acc + i}
 }
 Album(
     "Sunny side up", listOf(
         Track("10/10", 176),
         Track("Coming Up Easy", 292),
         Track("Growing Up Beside You", 191),
         Track("Candy", 303),
         Track("Tricks of the Trade", 151)
     )
 ).run {
     arrayOf("Growing Up Beside You", "Coming Up Easy").forEach {
         println("\"$it\" started at ${getStartTime(it)} seconds")
     }
 }
```

Output:

```
"Growing Up Beside You" started at 468 seconds
"Coming Up Easy" started at 176 seconds

```
- `fun <T, R> Iterable<T>.fold(initial: R, operation: (acc: R, T) -> R): R ` is the equivalent of `Array.prototype.reduce` in JavaScipt
- `fun <S, T : S> Iterable<T>.reduce(operation: (acc: S, T) -> S): S` is different from JavaScript equivalent. It use first element as initial value


## 9. Grouping data

 > not implemented or left blank intentionally

 > not implemented or left blank intentionally

# Ch6. Friendly I/O Operations

## 1. Reading the contents of a file

```kotlin
 import java.io.File.separator as SEP
 val path = "src/main/resources/file1.txt"
 val filePathName = if (SEP == "/") path else path.replace("/", SEP)
 File(filePathName).run {
     println(readText())
 }
```

Output:

```
  |\_/|
 / @ @ \
( > º < )
 `>>x<<´
 /  O  \

```

## 2. Ensuring stream closing with the use (Kotlin reflection is not available)

```kotlin
 val path = "src/main/resources/file1.txt"
 val filePathName = if (SEP == "/") path else path.replace("/", SEP)
 File(filePathName).inputStream().use {
     it.readBytes().also { a: ByteArray -> println(String(a)) }
 }
```

Output:

```
  |\_/|
 / @ @ \
( > º < )
 `>>x<<´
 /  O  \

```

## 3. Reading the contents of a file line by line

```kotlin
 val path = "src/main/resources/file1.txt"
 val filePathName = if (SEP == "/") path else path.replace("/", SEP)
 File(filePathName).readLines().forEach(::println)
```

Output:

```
  |\_/|
 / @ @ \
( > º < )
 `>>x<<´
 /  O  \

```

## 4. Writing the contents to a file

```kotlin
 val byAndyHUnt = "..." // omitted for simplicity
 val path = "src/main/resources/temp1.tmp"
 val filePathName = if (SEP == "/") path else path.replace("/", SEP)
 File(filePathName).writeText(byANdyHunt)
```

Output:

```

```

## 5. Appending a file

```kotlin
 val path = "src/main/resources/temp2.tmp"
 val filePathName = if (SEP == "/") path else path.replace("/", SEP)
 with(File(filePathName)) {
     if (exists()) delete()
     appendText("""
     A language that doesn't affect the way you think
     about programming
     is worth knowing
     """.trimIndent())
     appendText("\n")
     appendBytes("Alan Perlis".toByteArray())
 }
```

Output:

```

```

## 6. Easy file copying

 > not implemented or left blank intentionally

## 7. Traversing files in a directory

```kotlin
 val path = "src/main/resources/"
 val dir = if (SEP == "/") path else path.replace("/", SEP)
     File(dir).walk()
         .filter { it.isFile }
         .filter { it.extension == "txt" || it.extension == "tmp" }
         .filter { it.length() > 0}
         .forEachIndexed { index, file ->
             println("$index) ${file.path}")
             println("  ${file.readText()}")
             println()
         }
```

Output:

```
0) src/main/resources/temp1.tmp
  No one in the brief history of coputing
has ever written a piece of perfect softeware.
It's unlikely that you'll be the first - Andy Hunt

1) src/main/resources/file2.txt
  "Testing can show the presence of errors, but not their absence." - E. W. Dijkstra

2) src/main/resources/temp2.tmp
  A language that doesn't affect the way you think
about programming
is worth knowing
Alan Perlis

3) src/main/resources/file1.txt
    |\_/|
 / @ @ \
( > º < )
 `>>x<<´
 /  O  \

4) src/main/resources/subdirectory/sample_file_2.txt
  "Weeks of coding can save you hours of planning." - Unknown


```

# Ch7. Making Asynchronous Programming Great Again
- [Kotlin Reference: Coroutine Basic](https://kotlinlang.org/docs/reference/coroutines/basics.html)
- [KotlinConf 2017 - Deep Dive into Coroutines on JVM by Roman Elizarov](https://www.youtube.com/watch?v=YrrUCSi72E8)
- [Kotlin’s suspend functions compared to JavaScript’s async/await](https://medium.com/@joffrey.bion/kotlins-suspend-functions-are-not-javascript-s-async-they-are-javascript-s-await-f95aae4b3fd9)


## 1. Executing tasks in the background using threads

```kotlin
 fun getCurrentThreadName(): String = Thread.currentThread().name
 fun `5 sec long task` () = Thread.sleep(5000)
 fun `2 sec long task` () = Thread.sleep(2000)
 
 println("Running on ${getCurrentThreadName()}")
 thread {
     println("Starting async operation on ${getCurrentThreadName()}")
     `5 sec long task`()
     println("Ending async operation on ${getCurrentThreadName()}")
 }
 thread {
     println("Starting async operation on ${getCurrentThreadName()}")
     `2 sec long task`()
     println("Ending async operation on ${getCurrentThreadName()}")
 }
```

Output:

```

Running on main
Starting async operation on Thread-0
Starting async operation on Thread-1
Ending async operation on Thread-1
Ending async operation on Thread-0


```

## 2. Background threads synchronization

```kotlin
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
```

Output:

```
Running on main
Starting async operation on Thread-2
Ending async operation on Thread-2
Starting async operation on Thread-3
Ending async operation on Thread-3

```

## 3. Using coroutines for asynchronous concurrent execution of tasks
Output:

```
Running on main

Running on SushiThread

Current thread is not blocked while rice is being cooked
Starting to prepare fish on SushiThread
Starting to cook rice on DefaultDispatcher-worker-1
Fish prepared
Starting to cut vegetables on SushiThread
Vegetables ready
Rice cooked
Starting to roll the sushi on SushiThread
Sushi rolled
Total time: 1223 ms

```

> see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)    


## 4. Using coroutines for asynchronous concurrent execution with results handling

> see [source file](src/main/kotlin/cookbook/ch7_making_asynchronous_programming_great_again/ch7.kt)

Output:

```
Running on main

Starting progressbar animation on DefaultDispatcher-worker-1
 -----------------------------
main thread is not blocked while tasks are in progress
Starting computations on DefaultDispatcher-worker-2
---- -------------------------
The anwser to life the universe and everything: 42
Running on main


```

## 5. Applying coroutines for asynchronous data processing

## 6. Easy coroutine cancelation

## 7. Building a REST API client with Retrofit and coroutines adapter

## 8. Wrapping third party callback style APIs with coroutines
