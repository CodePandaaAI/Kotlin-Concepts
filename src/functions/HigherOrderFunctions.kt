package functions

// KOTLIN CONCEPT: Higher-Order Functions
//
// Function that takes a function (lambda) or returns one.
// `filter`, `map`, `forEach` are just loops — you pass the "what to do" as a lambda.
//
// `inline` copies the function body at the call site (faster, no lambda object).
// `reified T` only works with `inline` — keeps type `T` at runtime for `is T` checks.
//
// Prerequisite: Lambdas.kt

fun main() {

    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8)

    println("Evens: ${numbers.myFilter { it % 2 == 0 }}")
    println("Doubled: ${numbers.myMap { it * 2 }}")
    println("Labels: ${numbers.myMap { "Number $it" }}")

    print("Items: ")
    numbers.myForEach { print("$it ") }
    println()

    checkType<String>("hello")
    checkType<Int>("hello")
    checkType<Int>(42)
}

// predicate: (T) -> Boolean — keep item when true
inline fun <T> List<T>.myFilter(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        if (predicate(item)) result.add(item)
    }
    return result
}

// T = input type, R = output type (can differ)
inline fun <T, R> Iterable<T>.myMap(transform: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(transform(item))
    }
    return result
}

inline fun <T> Iterable<T>.myForEach(action: (T) -> Unit) {
    for (item in this) action(item)
}

inline fun <reified T> checkType(item: Any) {
    if (item is T) {
        println("✅ Match! '$item' IS a ${T::class.simpleName}")
    } else {
        println("❌ No match. '$item' is ${item::class.simpleName}, not ${T::class.simpleName}")
    }
}
