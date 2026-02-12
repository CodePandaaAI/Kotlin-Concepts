package practice.networks

fun main() {
    val api: AuthApi = AuthApiImpl()
    val tokenStorage = SimpleTokenStorageImpl()
    val repository = LoginRepository(api, tokenStorage)

    // Check initial state
    println("Is logged in: ${repository.isLoggedIn()}")

    // Login with valid credentials
    println("\n--- Login attempt (valid) ---")
    val result1 = repository.login(username = "alice", password = "password123")
    printResult(result1)
    println("Is logged in: ${repository.isLoggedIn()}")

    // Try to login again (already logged in)
    println("\n--- Login attempt (already logged in) ---")
    val result2 = repository.login(username = "alice", password = "password123")
    printResult(result2)

    // Get current token
    println("\n--- Current token ---")
    println("Token: ${repository.getToken()}")

    // Logout
    println("\n--- Logout ---")
    repository.logout()
    println("Is logged in: ${repository.isLoggedIn()}")
    println("Token: ${repository.getToken()}")

    // Login with invalid credentials
    println("\n--- Login attempt (invalid) ---")
    val result3 = repository.login(username = "alice", password = "wrong")
    printResult(result3)
    println("Is logged in: ${repository.isLoggedIn()}")
}

fun printResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Success: ${result.data}")
        is Result.Error -> println("Error: ${result.message}")
    }
}

interface AuthApi {
    fun login(userName: String, password: String): Result<AppUser>
}

class AuthApiImpl : AuthApi {
    val listOfRegisteredUsers = listOf(
        AppUser(id = 1, username = "alice", email = "rishi.com", password = "password123"),
        AppUser(id = 2, username = "Gar", email = "gar.com", password = "2025ok"),
        AppUser(id = 3, username = "vikas", email = "vikas.com", password = "okYou"),
        AppUser(id = 4, username = "Romit", email = "romit.com", password = "ILoveYou"),
        AppUser(id = 5, username = "Shubham", email = "brazers.com", password = "Game")
    )

    override fun login(userName: String, password: String): Result<AppUser> {
        for (user in listOfRegisteredUsers) {
            if (user.username == userName && user.password == password) {

                return Result.Success(user)
            }
        }
        return Result.Error("Either user don't exist or invalid credentials")
    }
}

class LoginRepository(private val api: AuthApi, private val simpleTokenStorage: SimpleTokenStorage) {
    private var currentLoggedInUser: AppUser? = null

    fun login(username: String, password: String): Result<String> {
        currentLoggedInUser?.let { return Result.Success("Success! Welcome ${it.username}") }
        val result = api.login(username, password)
        if (result is Result.Success) {
            simpleTokenStorage.generateToken()
            currentLoggedInUser = result.data
            return Result.Success("Success! Welcome ${result.data.username}")
        }
        return Result.Error("Either user don't exist or invalid credentials")
    }

    fun isLoggedIn(): Boolean = currentLoggedInUser != null

    fun getToken(): String? = simpleTokenStorage.getToken()

    fun removeToken() = simpleTokenStorage.removeToken()

    fun logout() {
        currentLoggedInUser = null
        removeToken()
    }
}

interface SimpleTokenStorage {

    fun generateToken()
    fun getToken(): String?

    fun removeToken()
}

class SimpleTokenStorageImpl : SimpleTokenStorage {
    private var currentLoginToken: String? = null

    override fun generateToken() {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val tokenLength = 24
        currentLoginToken = (1..tokenLength)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun getToken(): String? {
        return currentLoginToken
    }

    override fun removeToken() {
        currentLoginToken = null
    }
}

data class AppUser(val id: Int? = null, val username: String, val email: String? = null, val password: String? = null)
