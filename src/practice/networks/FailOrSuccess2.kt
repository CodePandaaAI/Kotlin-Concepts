package practice.networks

fun main() {
    // Test 1: Get a User
    val userResult = getUser(id = 1)
    when (userResult) {
        is Result.Success -> println("Got user: ${userResult.data.name}")  // data is User
        is Result.Error -> println("Error: ${userResult.message}")
    }

    // Test 2: Get a number
    val numberResult = getNumber(valid = true)
    when (numberResult) {
        is Result.Success -> println("Got number: ${numberResult.data}")  // data is Int
        is Result.Error -> println("Error: ${numberResult.message}")
    }

    // Test 3: Failure case
    val failResult = getUser(id = 999)
    when (failResult) {
        is Result.Success -> println("Got user: ${failResult.data.name}")
        is Result.Error -> println("Error: ${failResult.message}")
    }
}

// Returns Result<User> - caller KNOWS data will be User
fun getUser(id: Int): Result<User> {
    return if (id == 1) {
        Result.Success(User(name = "Alice"))
    } else {
        Result.Error("User not found")
    }
}

// Returns Result<Int> - caller KNOWS data will be Int
fun getNumber(valid: Boolean): Result<Int> {
    return if (valid) {
        Result.Success(42)
    } else {
        Result.Error("Invalid")
    }
}

// The whole interface is generic!
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String) : Result<Nothing>  // Nothing works for any T
}