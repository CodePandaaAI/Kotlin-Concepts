package advanced

/**
 * ============================================
 * KOTLIN CONCEPT: MVVM ARCHITECTURE PATTERN
 * ============================================
 * 
 * WHAT IS MVVM?
 * -------------
 * MVVM (Model-View-ViewModel) is an architectural pattern that separates:
 * - Model: Data and business logic
 * - View: UI (not shown here, but represented by ViewModel)
 * - ViewModel: Connects View and Model, manages UI state
 * 
 * ARCHITECTURE LAYERS:
 * --------------------
 * 1. Data Layer: HTTP Client, API interfaces
 * 2. Domain Layer: Repository (business logic)
 * 3. Presentation Layer: ViewModel (UI logic)
 * 
 * BENEFITS:
 * ---------
 * - Separation of concerns
 * - Testability (each layer can be tested independently)
 * - Maintainability (clear responsibilities)
 * - Reusability (ViewModel can be reused across platforms)
 */

fun main() {
    // ============================================
    // DEPENDENCY INJECTION SETUP
    // ============================================
    // In real apps, you'd use a DI framework (Koin, Hilt, etc.)
    // Here we manually wire dependencies to show the pattern
    
    // 1. Create the HTTP client (lowest layer)
    val client = FakeHttpClient()
    
    // 2. Create API implementation with client (data layer)
    val authApi: AuthApi = AuthApiImpl(client)
    
    // 3. Create repository with API (domain layer)
    val repository = AuthRepository(authApi)
    
    // 4. ViewModel uses repository (presentation layer)
    val viewModel = LoginViewModel(repository)
    
    // 5. User action triggers the chain (simulating button click)
    viewModel.login("user123", "password")
}

/**
 * DATA LAYER: HTTP CLIENT
 * -----------------------
 * 
 * This simulates an HTTP client (like Retrofit, Ktor, OkHttp)
 * In real apps, this would make actual network calls.
 * 
 * RESPONSIBILITIES:
 * - Making HTTP requests
 * - Handling network protocols
 * - Parsing responses
 */
class FakeHttpClient {
    /**
     * Simulates a POST request
     * 
     * @param url The endpoint URL
     * @param body The request body (could be JSON, form data, etc.)
     * @return FakeResponse with status code and body
     */
    fun post(url: String, body: Any): FakeResponse {
        println("POST $url with body: $body")
        // Simulate successful response
        return FakeResponse(200, """{"token": "abc123"}""")
    }
}

/**
 * Response data class
 * Represents the raw HTTP response
 */
data class FakeResponse(val status: Int, val body: String)

/**
 * DATA LAYER: API INTERFACE
 * --------------------------
 * 
 * This defines the contract for authentication operations.
 * Interfaces allow for:
 * - Easy testing (mock implementations)
 * - Multiple implementations (real API, fake API, etc.)
 * - Clear API contracts
 */
interface AuthApi {
    /**
     * Login operation
     * 
     * @param username User's username
     * @param password User's password
     * @return NetworkResult containing either success data or error
     */
    fun login(username: String, password: String): NetworkResult<String>
}

/**
 * DATA LAYER: API IMPLEMENTATION
 * ------------------------------
 * 
 * This implements the AuthApi interface using the HTTP client.
 * 
 * RESPONSIBILITIES:
 * - Convert domain models to network models
 * - Handle network errors
 * - Parse responses
 * - Return domain-friendly results
 */
class AuthApiImpl(private val client: FakeHttpClient) : AuthApi {
    /**
     * IMPLEMENTATION DETAILS:
     * -----------------------
     * 1. Prepare request data
     * 2. Make network call
     * 3. Handle response/errors
     * 4. Return typed result
     */
    override fun login(username: String, password: String): NetworkResult<String> {
        return try {
            // Prepare request body
            val requestBody = mapOf(
                "username" to username,
                "password" to password
            )
            
            // Make network call
            val response = client.post("/auth/login", requestBody)
            
            // Handle response
            if (response.status == 200) {
                NetworkResult.Success(response.body)
            } else {
                NetworkResult.Error("Login failed")
            }
        } catch (e: Exception) {
            // Handle network errors
            NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}

/**
 * DOMAIN LAYER: REPOSITORY
 * ------------------------
 * 
 * Repository pattern abstracts data sources.
 * 
 * BENEFITS:
 * - Single source of truth
 * - Can combine multiple data sources (API + cache)
 * - ViewModel doesn't need to know about data source details
 * 
 * RESPONSIBILITIES:
 * - Coordinate data operations
 * - Cache management (not shown here)
 * - Business logic
 */
class AuthRepository(private val authApi: AuthApi) {
    /**
     * Login operation
     * 
     * This could add additional logic like:
     * - Caching tokens
     * - Validating input
     * - Combining multiple data sources
     */
    fun login(username: String, password: String): NetworkResult<String> {
        println("Repository: Calling API...")
        return authApi.login(username, password)
    }
}

/**
 * PRESENTATION LAYER: VIEWMODEL
 * ------------------------------
 * 
 * ViewModel manages UI-related data and logic.
 * 
 * RESPONSIBILITIES:
 * - Handle user actions
 * - Manage UI state
 * - Coordinate with repository
 * - Transform data for UI
 * 
 * IN ANDROID:
 * - Survives configuration changes
 * - No reference to View (avoids memory leaks)
 * - Can be observed by View (LiveData, StateFlow)
 */
class LoginViewModel(private val repository: AuthRepository) {
    /**
     * Handle login action (called from View when user clicks login button)
     * 
     * @param username User input
     * @param password User input
     * 
     * In real apps, this would:
     * - Update loading state
     * - Update UI state (LiveData/StateFlow)
     * - Handle navigation
     */
    fun login(username: String, password: String) {
        println("ViewModel: Starting login...")
        
        // Call repository
        when (val result = repository.login(username, password)) {
            is NetworkResult.Success -> {
                // Handle success
                println("ViewModel: Login success! Token: ${result.data}")
                // In real app: update UI state, navigate to next screen
            }
            is NetworkResult.Error -> {
                // Handle error
                println("ViewModel: Login failed: ${result.message}")
                // In real app: show error message to user
            }
        }
    }
}

/**
 * SEALED INTERFACE: NETWORK RESULT
 * --------------------------------
 * 
 * Type-safe way to represent network operation results.
 * 
 * WHY SEALED INTERFACE?
 * - Exhaustive when expressions
 * - Type safety
 * - Clear success/error distinction
 * 
 * WHY GENERIC?
 * - Can represent results of any type
 * - Success<String>, Success<User>, etc.
 * 
 * WHY 'out T'?
 * - Covariance: NetworkResult<Child> IS-A NetworkResult<Parent>
 * - Only used for reading results, never writing
 */
sealed interface NetworkResult<out T> {
    /**
     * Success case with data
     */
    data class Success<T>(val data: T) : NetworkResult<T>
    
    /**
     * Error case with message
     * Uses Nothing because errors don't have success data
     */
    data class Error(val message: String) : NetworkResult<Nothing>
}

/**
 * ============================================
 * DATA FLOW DIAGRAM
 * ============================================
 * 
 * User Action (Button Click)
 *         ↓
 * ViewModel.login()
 *         ↓
 * Repository.login()
 *         ↓
 * AuthApi.login()
 *         ↓
 * HttpClient.post()
 *         ↓
 * Network Call
 *         ↓
 * Response flows back up
 *         ↓
 * ViewModel updates UI state
 * 
 * ============================================
 * KEY PRINCIPLES
 * ============================================
 * 
 * 1. DEPENDENCY INJECTION:
 *    - Dependencies passed as constructor parameters
 *    - Makes testing easier (can inject mocks)
 *    - Loose coupling
 * 
 * 2. SINGLE RESPONSIBILITY:
 *    - Each class has one job
 *    - Client: network calls
 *    - API: data operations
 *    - Repository: business logic
 *    - ViewModel: UI logic
 * 
 * 3. INTERFACE SEGREGATION:
 *    - Use interfaces for contracts
 *    - Easy to swap implementations
 * 
 * 4. DEPENDENCY INVERSION:
 *    - Depend on abstractions (interfaces), not concretions
 *    - Repository depends on AuthApi interface, not AuthApiImpl
 */

