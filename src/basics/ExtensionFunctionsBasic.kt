package basics

// KOTLIN CONCEPT: Extension Functions
//
// Add a method to a type you don't own. `text.shout()` instead of `shout(text)`.
// Compiler turns `receiver.method()` into `method(receiver)`. Class source unchanged.
//
// Inside the function, `this` is the receiver object.

fun main() {

    val text = "hello"

    // Normal function — argument first
    println(shoutNormal(text))  // HELLO!!!

    // Extension — reads like a method on the string
    println(text.shout())  // HELLO!!!

    println(5.isEven())    // false
    println(8.isEven())    // true

    val numbers = listOf(1, 2, 3, 4, 5)
    println(numbers.secondOrNull())  // 2
    println(listOf("only one").secondOrNull())  // null

    // Receiver type can be nullable
    val name: String? = null
    println(name.orDefault("Unknown"))  // Unknown
    println("Alice".orDefault("Unknown"))  // Alice
}

fun shoutNormal(s: String): String = s.uppercase() + "!!!"

// fun <ReceiverType>.methodName(...): ReturnType
// `this` = the value before the dot
fun String.shout(): String = this.uppercase() + "!!!"

fun Int.isEven(): Boolean = this % 2 == 0

fun <T> List<T>.secondOrNull(): T? =
    if (size >= 2) this[1] else null

fun String?.orDefault(default: String): String = this ?: default
