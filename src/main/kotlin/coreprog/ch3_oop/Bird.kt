package coreprog.ch3_oop

class Bird1 {
    val weight: Double = 500.0
    val color: String = "blue"
    val age: Int = 1
    fun fly() {}
}

class Bird2(
    val weight: Double = 500.0,
    val color: String = "blue",
    val age: Int = 1
) {
    fun fly() {}
}

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
        this.age = age
    }

    init {
        this.color = color
    }
}

class Bird4(val weight: Double, val age: Int, private val color: String) {
    val sex : String by lazy {
        if (color == "yellow") "male" else "female"
    }
}

class Bird5(val weight: Double, val age: Int, private val color: String) {
    lateinit var sex : String
}

fun main() {
    Bird2(color="red")
}