# 第三章 面向对象

## 3.1 类和构造方法

```kotlin
class Bird1 {
    val weight: Double = 500.0
    val color: String = "blue"
    val age: Int = 1
    fun fly() {}
}
```
生成下面的Java类。没有构造函数。

- 属性默认private final
- 类默认public。而Java默认包内可见

```java
public final class Bird1 {
   private final double weight = 500.0D;
   @NotNull
   private final String color = "blue";
   private final int age = 1;

   public final double getWeight() {
      return this.weight;
   }

   @NotNull
   public final String getColor() {
      return this.color;
   }

   public final int getAge() {
      return this.age;
   }

   public final void fly() {
   }
}

```

```kotlin
class Bird2(
    val weight: Double = 500.0,
    val color: String = "blue",
    val age: Int = 1
) {
    fun fly() {}
}
```
生成下面的Java类。有构造函数。

```java
public final class Bird2 {
   private final double weight;
   @NotNull
   private final String color;
   private final int age;
   public final void fly() {}
   public final double getWeight() {return this.weight;}
   @NotNull
   public final String getColor() {return this.color;}
   public final int getAge() {return this.age; }

   public Bird2(double weight, @NotNull String color, int age) {
      Intrinsics.checkParameterIsNotNull(color, "color");
      super();
      this.weight = weight;
      this.color = color;
      this.age = age;
   }

   // $FF: synthetic method
   public Bird2(double var1, String var3, int var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 1) != 0) {
         var1 = 500.0D;
      }

      if ((var5 & 2) != 0) {
         var3 = "blue";
      }

      if ((var5 & 4) != 0) {
         var4 = 1;
      }

      this(var1, var3, var4);
   }

   public Bird2() {
      this(0.0D, (String)null, 0, 7, (DefaultConstructorMarker)null);
   }
}
```

```kotlin
fun main() {
    Bird2(color="red")
}
```
构造函数命名参数的调用会被翻译成下面代码。编译器知道对应参数是第几个。
将其他位置上的参数填入默认参数。指定参数填上你给的值。
```java
public final class BirdKt {
    public static final void main() {
      new Bird2(0.0D, "red", 0, 5, (DefaultConstructorMarker)null);
    }
}
```

- 在类名后面加上括号，并且括号中包含构造参数时，编译器才会生成构造函数。如果只有括号也不会生成构造函数。
- Kotlin的构造参数前有`val`l或`var`时才会生成对应的属性+getter/setter。这个类似TypeScript的[Parameter Property](https://www.typescriptlang.org/docs/handbook/classes.html#parameter-properties)

下面去掉`val`，然后增加三个`init`语句块（注意，不是函数）。我故意调整了一下属性和`init`块的顺序
```kotlin
class Bird3(
    weight: Double = 500.0,
    color: String = "blue",
    age: Int = 1
) {
    private val weight: Double
    
    init {
        this.weight = weight
    }

    private val color: String
    private val age: Int

    init {
        this.age = 1
    }

    init {
        this.color = "blue"
    }
}
```
被翻译为
```java
public final class Bird3 {
   private final double weight;
   private final String color;
   private final int age;

   public Bird3(double weight, @NotNull String color, int age) {
      Intrinsics.checkParameterIsNotNull(color, "color");
      super();
      this.weight = weight;
      this.age = 1;
      this.color = "blue";
   }

   // $FF: synthetic method
   public Bird3(double var1, String var3, int var4, int var5, DefaultConstructorMarker var6) {
      if ((var5 & 1) != 0) {
         var1 = 500.0D;
      }

      if ((var5 & 2) != 0) {
         var3 = "blue";
      }

      if ((var5 & 4) != 0) {
         var4 = 1;
      }

      this(var1, var3, var4);
   }

   public Bird3() {
      this(0.0D, (String)null, 0, 7, (DefaultConstructorMarker)null);
   }
}
```
可以看到
- 多个`init`语句块被按声明顺序合并到构造函数中
- 整体翻译结果基本一模一样。只不过后者没有getter/setter

### Lazy和lateinit

```kotlin
class Bird4(val weight: Double, val age: Int, private val color: String) {
    val sex : String by lazy {
        if (color == "yellow") "male" else "female"
    }
}
```
翻译成
```java
public final class Bird4 {
   @NotNull
   private final Lazy sex$delegate;
   private final double weight;
   private final int age;
   private final String color;

   @NotNull
   public final String getSex() {
      Lazy var1 = this.sex$delegate;
      Object var3 = null;
      boolean var4 = false;
      return (String)var1.getValue();
   }

   public final double getWeight() {
      return this.weight;
   }

   public final int getAge() {
      return this.age;
   }

   public Bird4(double weight, int age, @NotNull String color) {
      Intrinsics.checkParameterIsNotNull(color, "color");
      super();
      this.weight = weight;
      this.age = age;
      this.color = color;
      this.sex$delegate = LazyKt.lazy((Function0)(new Function0() {
         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke() {
            return this.invoke();
         }

         @NotNull
         public final String invoke() {
            return Intrinsics.areEqual(Bird4.this.color, "yellow") ? "male" : "female";
         }
      }));
   }
}
```
`by lazy`的属性翻译后是一个Lazy实例，并在构造函数中初始化。
相应的`getter`也会调用`Lazy`的`getValue`来获取

```kotlin
class Bird5(val weight: Double, val age: Int, private val color: String) {
    lateinit var sex : String
}
```
被翻译为
```java
public final class Bird5 {
   public String sex;
   private final double weight;
   private final int age;
   private final String color;

   @NotNull
   public final String getSex() {
      String var10000 = this.sex;
      if (var10000 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sex");
      }

      return var10000;
   }

   public final void setSex(@NotNull String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
      this.sex = var1;
   }

   public final double getWeight() {
      return this.weight;
   }

   public final int getAge() {
      return this.age;
   }

   public Bird5(double weight, int age, @NotNull String color) {
      Intrinsics.checkParameterIsNotNull(color, "color");
      super();
      this.weight = weight;
      this.age = age;
      this.color = color;
   }
}
```
课件`lateinit`只是允许`val`属性晚些初始化并不强制。并在`getter`中进行检查。

## 3.2 不同的访问控制原则

## 3.3 解决多继承问题

- 继承多接口
- 使用委托
- 内部类解决多继承

## 3.4 真正的数据类型

### copy()


### 解构

- 类似ES6中的结构，不过需要自己实现componentN
- data class会帮你实现componentN
- [Destructuring Types](/src/main/kotlin/cookbook#4-destructuring-types)

## 3.5 从static到object
