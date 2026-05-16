package functions

// KOTLIN CONCEPT: Scope Functions (let, apply, also, run, with)
//
// Run a block on one object. Pick by two questions:
//   1. Return the object, or return the lambda result?
//   2. Inside the block: `this` (you are the object) or `it` (you hold the object)?
//
//   apply / also → return object     |  let / run / with → return lambda result
//   apply / run / with → `this`      |  let / also → `it`
//
// Prerequisite: Lambdas.kt

data class User(var name: String, var age: Int, var email: String = "")

fun main() {

    // let — `it`, returns lambda result. Common for null-safe work and transforms.
    val name: String? = "Alice"
    name?.let { println("Name length: ${it.length}") }
    println("hello".let { it.uppercase() })

    // apply — `this`, returns the same object. Configure properties.
    val user = User("Bob", 25).apply {
        email = "bob@example.com"
        age = 26
    }
    println(user)

    // also — `it`, returns the same object. Side effects (log) without breaking a chain.
    val numbers = mutableListOf(1, 2, 3)
        .also { println("Before: $it") }
        .also { it.add(4) }
        .also { println("After: $it") }

    // run — `this`, returns lambda result. Compute something from the object.
    val greeting = "Hello".run { "$this World! Length: $length" }
    println(greeting)

    nullableUser()?.run { "User: $name, Age: $age" }?.let { println(it) }

    // with — same idea as run, but not an extension: with(obj) { }
    val dave = User("Dave", 28, "dave@example.com")
    val description = with(dave) {
        "Name: $name, Age: $age, Email: $email"
    }
    println(description)
}

private fun nullableUser(): User? = User("Eve", 30)
