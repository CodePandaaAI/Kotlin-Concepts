package generics

// KOTLIN CONCEPT: Generic Variance (out / in / Nothing)
//
// String is subtype of Any, but MutableList<String> is NOT subtype of MutableList<Any>.
// Reason: both names could point at same list — adding Int through Any list breaks String list.
//
// out T — T only used in output (read). Allows List<String> where List<Any> expected.
// in T  — T only used in input (write). Subtyping goes opposite direction.
// Nothing — subtype of every type, no instances. Lets Error fit any Result<T> with out.
//
// Prerequisite: basics/BasicGenerics.kt

fun main() {
    part1_invariance()
    part2_covariance()
    part3_contravariance()
    part4_nothing()
    part5_resultPattern()
}

fun part1_invariance() {
    println("=== Part 1: Invariance ===")

    val s: Any = "hello"  // String → Any works

    val strings: MutableList<String> = mutableListOf("A", "B")
    // val anything: MutableList<Any> = strings  // compile error

    // If this were allowed:
    //   anything.add(123)
    //   val bad: String = strings[2]  // crash — list promised String only

    println("MutableList<String> and MutableList<Any> = no subtyping relationship\n")
}

fun part2_covariance() {
    println("=== Part 2: Covariance (out) ===")

    val strings: List<String> = listOf("Hello", "World")
    val objects: List<Any> = strings  // List is read-only — safe

    val burgerBox: ReadOnlyBox<Burger> = ReadOnlyBox(Burger())
    val foodBox: ReadOnlyBox<Food> = burgerBox
    println("ReadOnlyBox<Burger> → ReadOnlyBox<Food> = works")
    println("Got: ${foodBox.get()}\n")
}

fun part3_contravariance() {
    println("=== Part 3: Contravariance (in) ===")

    val numberConsumer = object : MyConsumer<Number> {
        override fun consume(item: Number) {
            println("  Consuming: $item")
        }
    }

    val doubleConsumer: MyConsumer<Double> = numberConsumer
    doubleConsumer.consume(3.14)

    // out: Child container → Parent container (read)
    // in:  Parent container → Child container (write)
    println("MyConsumer<Number> → MyConsumer<Double> = works\n")
}

fun part4_nothing() {
    println("=== Part 4: Nothing ===")

    // Nothing <: every type. No values exist.
    val strings: List<String> = emptyList()  // emptyList() is List<Nothing>
    val ints: List<Int> = emptyList()

    println("emptyList() is List<Nothing> — fits any List<T>\n")
}

fun part5_resultPattern() {
    println("=== Part 5: Result<out T> ===")

    val success = fetchUser(1)
    val failure = fetchUser(999)
    when (success) {
        is NetworkResult.Success -> println("Got: ${success.data}")
        is NetworkResult.Error -> println("Error: ${success.message}")
    }
    when (failure) {
        is NetworkResult.Success -> println("Got: ${failure.data}")
        is NetworkResult.Error -> println("Error: ${failure.message}")
    }

    val error = NetworkResult.Error("Server down")
    val asString: NetworkResult<String> = error
    val asInt: NetworkResult<Int> = error
    println("\nError is NetworkResult<Nothing> — fits any NetworkResult<T> (Nothing <: T + out)\n")
}

fun fetchUser(id: Int): NetworkResult<String> =
    if (id == 1) NetworkResult.Success("User: Alice")
    else NetworkResult.Error("User not found")

open class Food { override fun toString() = "Food" }
class Burger : Food() { override fun toString() = "🍔 Burger" }

class ReadOnlyBox<out T>(private val item: T) {
    fun get(): T = item
    // fun put(newItem: T) { }  // out forbids T in input position
}

interface MyConsumer<in T> {
    fun consume(item: T)
}

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val message: String) : NetworkResult<Nothing>
}
