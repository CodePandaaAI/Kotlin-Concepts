package collections

fun main() {
    val setA = setOf("Kotlin", "Java", "Python")
    val setB = setOf("Python", "C++", "JavaScript")

    val unionSet = setA union setB

    println("Union: $unionSet")
}
