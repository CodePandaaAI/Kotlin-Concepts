package patterns

// KOTLIN PATTERN: Result<T>
//
// Return success or error in the type — no hidden throws. Caller must handle both with `when`.
// Uses: sealed interface (fixed cases), `out T` (covariance), `Nothing` on Error (one Error fits any Result<T>).
//
// Prerequisite: generics/Variance.kt, oop/SealedClasses.kt

fun main() {

    println("=== Result Pattern Demo ===\n")

    val user1 = getUserById(1)
    val user2 = getUserById(999)

    when (user1) {
        is Result.Success -> println("✅ Found: ${user1.data}")
        is Result.Error -> println("❌ Error: ${user1.message}")
    }

    when (user2) {
        is Result.Success -> println("✅ Found: ${user2.data}")
        is Result.Error -> println("❌ Error: ${user2.message}")
    }

    val greeting = getUserById(1).map { user ->
        "Hello, ${user.name}! Welcome back."
    }

    when (greeting) {
        is Result.Success -> println("\n${greeting.data}")
        is Result.Error -> println("\nCan't greet: ${greeting.message}")
    }
}

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>

    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }
}

data class User(val id: Int, val name: String, val email: String)

private val users = listOf(
    User(1, "Alice", "alice@example.com"),
    User(2, "Bob", "bob@example.com"),
    User(3, "Charlie", "charlie@example.com")
)

fun getUserById(id: Int): Result<User> {
    val user = users.find { it.id == id }
    return if (user != null) Result.Success(user)
    else Result.Error("User with id=$id not found")
}
