![Kotlin Cookbook Code](https://user-images.githubusercontent.com/782871/98621983-76eda980-2343-11eb-9cae-54a47db5dd0b.png)

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

 > not implemented

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
-559153553 doesn't not belong to 0..10

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
45 belongs to grade Bad
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


## 2. Declaring interfaces containing dfault implementations

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

# Q&A

Process finished with exit code 0
