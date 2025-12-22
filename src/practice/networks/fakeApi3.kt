package practice.networks

fun main() {
    val api: FakeUserApi = FakeUserApiImpl()

    // Fetch user with id 1
    val result1 = api.fetchUser(1)
    handleResult(result1)

    // Fetch user with id 2
    val result2 = api.fetchUser(2)
    handleResult(result2)

    // Fetch user with id 999 (doesn't exist)
    val result3 = api.fetchUser(999)
    handleResult(result3)
}

fun handleResult(result: Result<User>) {
    when (result) {
        is Result.Success -> println("✓ Found: ${result.data}")
        is Result.Error -> println("✗ Error: ${result.message}")
    }
}

interface FakeUserApi {
    fun fetchUser(id: Int): Result<User>
}

class FakeUserApiImpl : FakeUserApi {
    override fun fetchUser(id: Int): Result<User> {
        return when (id) {
            1 -> Result.Success(User(id = id, name = "Alice", email = "rom@gmail.com"))
            2 -> Result.Success(User(id = id, name = "Bob", email = "rom@gmail.com"))
            else -> Result.Error("No User found with id$id")
        }
    }
}

data class User(val id: Int? = null, val name: String, val email: String? = null)