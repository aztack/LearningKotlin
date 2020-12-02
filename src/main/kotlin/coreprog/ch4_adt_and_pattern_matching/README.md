# 第四章 代数数据类型和模式匹配

## 4.1 [代数数据类型](https://en.wikipedia.org/wiki/Algebraic_data_type)

> ADT是一种组合类型。例如，一个类型由掐类型组合而成
> 常见的两个代数类型是Sum和Product类型

这里所谓的代数数据类型，其实是类型的『运算』，常见的有『加法』和『乘法』两种『运算』：

- Sum(A, B) = A + B
- Product(A, B) = A * B

单链表就是一个Sum类型（由2个Variant组成：Nil和 Cons a (List a)）
```haskell
data List a = Nil | Cons a (List a)
```
另外，还有枚举类型：枚举类型的值只能是某一个枚举值，不能同时是多个枚举值。

Product类型最常见的就是Record类型和Tuple类型

> 新类型的值的集合是由组成它的类型的值集合的『乘积』或者『和』构成。

## 4.2 模式匹配

- Kotlin（1.2)并没有完全支持模式匹配

### 4.2.2 常见的模式

常量模式

```kotlin
fun constantPattern(a: Int) = when (a) {
    1 -> "One"
    2 -> "Two"
    else -> "Greater than two"
}
```

翻译为

```java
public final class Ch4Kt {
   @NotNull
   public static final String constantPattern(int a) {
      String var10000;
      switch(a) {
      case 1:
         var10000 = "One";
         break;
      case 2:
         var10000 = "Two";
         break;
      default:
         var10000 = "Greater than two";
      }

      return var10000;
   }
}
```

类型模式

```kotlin
enum class WeekDay {
    SUN,
    MON,
    TUE,
    WED,
    THU,
    FRI,
    SAT
}

sealed class Day {
    object SUN: Day();
    object MON: Day();
    object TUE: Day();
    object WED: Day();
    object THU: Day();
    object FRI: Day();
    object SAT: Day();
}

fun schedule(day: Day): String {
    return when (day) {
        is Day.SUN -> "fishing"
        is Day.MON -> "work"
        is Day.TUE -> "study"
        is Day.WED -> "library"
        is Day.THU -> "writing"
        is Day.FRI -> "appointment"
        is Day.SAT -> "basketball"
    }
}
```
中`when`被翻译为

```java
public final class Ch4Kt {
   @NotNull
   public static final String schedule(@NotNull Day day) {
      Intrinsics.checkParameterIsNotNull(day, "day");
      String var10000;
      if (day instanceof Day.SUN) {
         var10000 = "fishing";
      } else if (day instanceof Day.MON) {
         var10000 = "work";
      } else if (day instanceof Day.TUE) {
         var10000 = "study";
      } else if (day instanceof Day.WED) {
         var10000 = "library";
      } else if (day instanceof Day.THU) {
         var10000 = "writing";
      } else if (day instanceof Day.FRI) {
         var10000 = "appointment";
      } else {
         if (!(day instanceof Day.SAT)) {
            throw new NoWhenBranchMatchedException();
         }

         var10000 = "basketball";
      }

      return var10000;
   }
}
```

逻辑表达式模式

```kotlin
fun logicPattern(a: Int) = when (a) {
    in 2..11 -> "$a is smaller than 10 and bigger than 1"
    else -> "Maybe $a is bigger than 10 or smaller than 1"
}
```
翻译为
```java
public final class Ch4Kt {
  @NotNull
   public static final String logicPattern(int a) {
      String var10000;
      if (2 <= a) {
         if (11 >= a) {
            var10000 = a + " is smaller than 10 and bigger than 1";
            return var10000;
         }
      }

      var10000 = "Maybe " + a + " is bigger than 10 or smaller than 1";
      return var10000;
   }
}
```

以上三种模式匹配都被翻译为常见的`switch`和`if-else`语句。

### 4.2.3 处理嵌套表达式

```kotlin
fun simpifyExpr(expr: Expr): Expr = when(expr) {
    is Expr.Num -> expr
    is Expr.Operate -> when (expr) {
        Expr.Operate("+", Expr.Num(0), expr.right) -> simpifyExpr(expr.right)
        Expr.Operate("+", expr.left, Expr.Num(0)) -> simpifyExpr(expr.left)
        else -> expr
    }
}
```
翻译为
```java
public final class Ch4Kt {
   @NotNull
   public static final Expr simpifyExpr(@NotNull Expr expr) {
      Intrinsics.checkParameterIsNotNull(expr, "expr");
      Expr var10000;
      if (expr instanceof Expr.Num) {
         var10000 = expr;
      } else {
         if (!(expr instanceof Expr.Operate)) {
            throw new NoWhenBranchMatchedException();
         }

         var10000 = Intrinsics.areEqual(expr,
            new Expr.Operate("+", (Expr)(new Expr.Num(0)), ((Expr.Operate)expr).getRight()))
            ? simpifyExpr(((Expr.Operate)expr).getRight())
            : (Intrinsics.areEqual(expr,
                new Expr.Operate("+", ((Expr.Operate)expr).getLeft(), (Expr)(new Expr.Num(0))))
                ? simpifyExpr(((Expr.Operate)expr).getLeft())
                : expr);
      }

      return var10000;
   }
}
```
可见模式匹配最终都被翻译为某种形式的『相等』判断。
将这种复杂的判断交给编译器来做。使源码在形式上变得易读、易理解

## 4.3 增强Kotlin的模式匹配

## 4.4 用代数数据类型抽象业务

利用ADT抽象业务并编写代码是我们的终极目的。