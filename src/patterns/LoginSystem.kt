package patterns

// ============================================
//  KOTLIN PATTERN: LOGIN SYSTEM (Repository Pattern)
// ============================================
//
//  This file combines MULTIPLE Kotlin concepts into one practical example:
//    1. Interfaces — defining contracts
//    2. Sealed interfaces — type-safe results
//    3. Data classes — models
//    4. Generics + variance — flexible error handling
//    5. Dependency injection — swappable implementations
//
//  ARCHITECTURE:
//    API (data source) → Repository (business logic) → Main (consumer)
//
//  This is simplified, but the STRUCTURE is identical
//  to what professional Kotlin/Android apps use.

fun main() {

    // ============================================
    //  STEP 1: WIRE UP DEPENDENCIES
    // ============================================
    //
    //  We create our implementations and pass them to the repository.
    //  This is "dependency injection" — giving the repository
    //  what it needs instead of letting it create things internally.
    //
    //  WHY? So we can swap implementations:
    //    Testing → FakeLoginApi (no network needed)
    //    Production → RealLoginApi (actual HTTP calls)

    val api: LoginApi = FakeLoginApi()
    val tokenStorage: TokenStorage = InMemoryTokenStorage()
    val repository = LoginRepository(api, tokenStorage)



    // ============================================
    //  STEP 2: TRY LOGGING IN
    // ============================================

    println("=== Login (valid credentials) ===")
    val result1 = repository.login("alice", "password123")
    printResult(result1)
    println("Logged in: ${repository.isLoggedIn()}")
    println("Token: ${repository.getToken()}")



    // ============================================
    //  STEP 3: TRY AGAIN (already logged in)
    // ============================================

    println("\n=== Login (already logged in) ===")
    val result2 = repository.login("alice", "password123")
    printResult(result2)



    // ============================================
    //  STEP 4: LOGOUT + INVALID LOGIN
    // ============================================

    println("\n=== Logout ===")
    repository.logout()
    println("Logged in: ${repository.isLoggedIn()}")

    println("\n=== Login (wrong password) ===")
    val result3 = repository.login("alice", "wrongpassword")
    printResult(result3)
    println("Logged in: ${repository.isLoggedIn()}")
}



// ============================================
//  RESULT TYPE
// ============================================
//
//  Same pattern as ResultPattern.kt, but named LoginResult
//  to avoid naming conflicts.
//  See ResultPattern.kt for the full explanation of how this works.

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


// ============================================
//  DATA MODEL
// ============================================

data class AppUser(
    val id: Int,
    val username: String,
    val email: String
)


// ============================================
//  API LAYER — "Where does the data come from?"
// ============================================
//
//  An INTERFACE defines the contract:
//    "Any login API must have a login() function."
//
//  The interface doesn't care HOW login works.
//  It could be HTTP, a local database, or a fake.
//  That's the power of interfaces — abstraction.

interface LoginApi {
    fun login(username: String, password: String): LoginResult<AppUser>
}

// FAKE implementation — for testing and learning.
// In a real app, this would make HTTP calls to a server.
class FakeLoginApi : LoginApi {
    private val registeredUsers = listOf(
        Triple("alice", "password123", AppUser(1, "alice", "alice@example.com")),
        Triple("bob", "securepass", AppUser(2, "bob", "bob@example.com")),
    )

    override fun login(username: String, password: String): LoginResult<AppUser> {
        val match = registeredUsers.find { it.first == username && it.second == password }
        return if (match != null) {
            LoginResult.Success(match.third)
        } else {
            LoginResult.Error("Invalid username or password")
            // Error is LoginResult<Nothing>
            // Expected: LoginResult<AppUser>
            // Works because of 'out' + Nothing!
        }
    }
}


// ============================================
//  TOKEN STORAGE — "Where do I save the login token?"
// ============================================
//
//  Another interface! Another abstraction.
//    Testing → InMemoryTokenStorage (RAM, fast)
//    Production → SharedPrefsTokenStorage (persistent)

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


// ============================================
//  REPOSITORY — "The Brain"
// ============================================
//
//  The repository coordinates between API and local storage.
//  It contains the BUSINESS LOGIC:
//    - Already logged in? Don't call API again.
//    - Login successful? Save the token.
//    - Login failed? Pass the error through.
//
//  DEPENDENCY INJECTION in action:
//    class LoginRepository(
//        private val api: LoginApi,            ← injected
//        private val tokenStorage: TokenStorage ← injected
//    )
//
//  The repository doesn't CREATE its dependencies.
//  It RECEIVES them. This makes it easy to:
//    - Test with fake implementations
//    - Swap storage strategies
//    - Change API without touching business logic

class LoginRepository(
    private val api: LoginApi,
    private val tokenStorage: TokenStorage
) {
    private var currentUser: AppUser? = null

    fun login(username: String, password: String): LoginResult<String> {
        // Already logged in? Return early.
        currentUser?.let {
            return LoginResult.Success("Already logged in as ${it.username}")
        }

        // Call API and handle result
        return when (val result = api.login(username, password)) {
            is LoginResult.Success -> {
                currentUser = result.data
                val token = generateToken()
                tokenStorage.saveToken(token)
                LoginResult.Success("Welcome, ${result.data.username}! Token: $token")
            }
            is LoginResult.Error -> result
            //  'result' is LoginResult<Nothing>
            //  Function returns LoginResult<String>
            //  LoginResult<Nothing> <: LoginResult<String> (via 'out')
            //  Error passes through unchanged. ✅
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


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Why not just use try-catch and exceptions?"
//
//  A: With exceptions:
//       - The caller might FORGET to try-catch → runtime crash
//       - The function signature doesn't tell you it can fail
//       - Exceptions are expensive (stack trace capture)
//
//     With Result:
//       - The caller MUST handle success AND error (compiler enforces it)
//       - The function signature says Result<T> → "I might fail"
//       - Pattern matching via 'when' is clear and concise
//
//
//  Q: "Why interfaces for LoginApi and TokenStorage?"
//
//  A: Swappability. In your app you'd have:
//       // For testing:
//       val api: LoginApi = FakeLoginApi()
//
//       // For production:
//       val api: LoginApi = RetrofitLoginApi(httpClient)
//
//     Same variable type, different implementation.
//     The repository doesn't care which one it gets.
//     That's the power of coding to interfaces.
//
//
//  Q: "Is this how real Android apps work?"
//
//  A: Very close! Real apps add:
//       - Coroutines (suspend functions for async)
//       - Hilt/Koin for automatic dependency injection
//       - Room for local database
//       - Retrofit for network calls
//     But the PATTERN is identical: API → Repository → ViewModel → UI.
