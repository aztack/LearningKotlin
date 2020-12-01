package coreprog.ch3_oop

fun main() {
    shallow_copy_of_data_class()
}

fun shallow_copy_of_data_class() {
    data class Bird(var color: String = "blue", var list: List<String>)
    val list = mutableListOf("a")
    val b1 = Bird("green", list)
    val b2 = b1.copy()
    list.add("b")
    println(b1.toString())
    println(b2.toString())
}