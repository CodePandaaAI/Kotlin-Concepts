package collections

fun main() {
    val fruits = mutableSetOf("Apple", "Banana", "Mango")

    println("Original Set: $fruits")

    // Trying to add duplicate
    fruits.add("Banana")
    println("After trying to add duplicate 'Banana': $fruits") // No change

    // Adding new item
    fruits.add("Orange")
    println("After adding 'Orange': $fruits")

    // Removing item
    fruits.remove("Mango")
    println("After removing 'Mango': $fruits")
}
