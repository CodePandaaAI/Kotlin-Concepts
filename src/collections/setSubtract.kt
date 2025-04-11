package collections

fun main() {
    val setA = setOf("Kotlin", "Java", "Python")
    val setB = setOf("Python", "C++")

    val diff = setA subtract setB

    println("Set Difference (A - B): $diff")
}
