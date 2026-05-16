package patterns

// KOTLIN PATTERN: Login System (Repository)
//
// Layers: LoginApi (data) → LoginRepository (rules) → main (caller).
// Interfaces let you swap FakeLoginApi / real HTTP for tests vs production.
// LoginResult<T> = same sealed Result pattern as ResultPattern.kt.
//
// Prerequisite: patterns/ResultPattern.kt

fun main() {

    val api: LoginApi = FakeLoginApi()
    val tokenStorage: TokenStorage = InMemoryTokenStorage()
    val repository = LoginRepository(api, tokenStorage)

    println("=== Login (valid credentials) ===")
    val result1 = repository.login("alice", "password123")
    printResult(result1)
    println("Logged in: ${repository.isLoggedIn()}")
    println("Token: ${repository.getToken()}")

    println("\n=== Login (already logged in) ===")
    printResult(repository.login("alice", "password123"))

    println("\n=== Logout ===")
    repository.logout()
    println("Logged in: ${repository.isLoggedIn()}")

    println("\n=== Login (wrong password) ===")
    printResult(repository.login("alice", "wrongpassword"))
    println("Logged in: ${repository.isLoggedIn()}")
}

sealed interface LoginResult<out T> {
    data class Success<T>(val data: T) : LoginResult<T>
    data class Error(val message: String) : LoginResult<Nothing>
}

fun printResult(result: LoginResult<String>) {
    when (result) {
        is LoginResult.Success -> println("✅ ${result.data}")
        is LoginResult.Error -> println("❌ ${result.message}")
    }
}

data class AppUser(
    val id: Int,
    val username: String,
    val email: String
)

interface LoginApi {
    fun login(username: String, password: String): LoginResult<AppUser>
}

class FakeLoginApi : LoginApi {
    private val registeredUsers = listOf(
        Triple("alice", "password123", AppUser(1, "alice", "alice@example.com")),
        Triple("bob", "securepass", AppUser(2, "bob", "bob@example.com")),
    )

    override fun login(username: String, password: String): LoginResult<AppUser> {
        val match = registeredUsers.find { it.first == username && it.second == password }
        return if (match != null) LoginResult.Success(match.third)
        else LoginResult.Error("Invalid username or password")
    }
}

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}

class InMemoryTokenStorage : TokenStorage {
    private var token: String? = null
    override fun saveToken(token: String) { this.token = token }
    override fun getToken(): String? = token
    override fun clearToken() { token = null }
}

class LoginRepository(
    private val api: LoginApi,
    private val tokenStorage: TokenStorage
) {
    private var currentUser: AppUser? = null

    fun login(username: String, password: String): LoginResult<String> {
        currentUser?.let {
            return LoginResult.Success("Already logged in as ${it.username}")
        }

        return when (val result = api.login(username, password)) {
            is LoginResult.Success -> {
                currentUser = result.data
                val token = generateToken()
                tokenStorage.saveToken(token)
                LoginResult.Success("Welcome, ${result.data.username}! Token: $token")
            }
            is LoginResult.Error -> result
        }
    }

    fun isLoggedIn(): Boolean = currentUser != null
    fun getToken(): String? = tokenStorage.getToken()

    fun logout() {
        currentUser = null
        tokenStorage.clearToken()
    }

    private fun generateToken(): String {
        val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..24).map { chars.random() }.joinToString("")
    }
}
