package collections

fun main() {
    val setA = setOf("Kotlin", "Java", "Python")
    val setB = setOf("Python", "C++", "JavaScript")

    val intersectionSet = setA intersect setB

    println("Intersection: $intersectionSet")
}

