package advanced

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * ============================================
 * KOTLIN CONCEPT: SEALED CLASSES & COROUTINES
 * ============================================
 * 
 * WHAT ARE SEALED CLASSES?
 * -------------------------
 * Sealed classes represent a restricted class hierarchy where:
 * - All subclasses must be declared in the same file/module
 * - The compiler knows ALL possible subtypes
 * - Enables exhaustive when expressions (compiler checks all cases)
 * 
 * WHY USE SEALED CLASSES?
 * -----------------------
 * 1. Type Safety: Compiler ensures you handle all cases
 * 2. State Management: Perfect for representing UI states
 * 3. Result Types: Great for success/error scenarios
 * 4. Pattern Matching: Works beautifully with when expressions
 * 
 * COROUTINES INTEGRATION:
 * -----------------------
 * This example shows how sealed classes work with coroutines
 * for managing async state transitions.
 */

/**
 * SEALED CLASS FOR UI STATE
 * --------------------------
 * 
 * This represents all possible states of a screen:
 * - Loading: Initial state, fetching data
 * - HomeScreen: Successfully loaded, showing content
 * - Error: Something went wrong
 * 
 * KEY FEATURES:
 * - 'sealed' keyword restricts inheritance
 * - All subclasses must be in same file
 * - Compiler knows all possible states
 * - Exhaustive when expressions
 */
sealed class SealedState {
    /**
     * LOADING STATE
     * -------------
     * 'object' means singleton - only one instance exists
     * Use when you don't need to store data, just represent a state
     */
    object Loading : SealedState()
    
    /**
     * HOME SCREEN STATE
     * ------------------
     * 'data class' means it can hold data
     * Use when you need to pass information with the state
     * 
     * Default parameter allows: SealedState.HomeScreen() or SealedState.HomeScreen("Custom")
     */
    data class HomeScreen(val name: String = "Home Screen") : SealedState()
    
    /**
     * ERROR STATE
     * -----------
     * Another singleton - errors don't need extra data in this example
     * (though you could make it a data class to include error messages)
     */
    object Error : SealedState()
}

/**
 * SUSPEND FUNCTION
 * ----------------
 * 
 * WHAT IS 'suspend'?
 * ------------------
 * - Suspend functions can be paused and resumed
 * - They can call other suspend functions (like delay())
 * - They don't block the thread
 * - They're the building blocks of coroutines
 * 
 * WHY SUSPEND HERE?
 * -----------------
 * - We use delay() which is a suspend function
 * - Suspend functions can only be called from coroutines or other suspend functions
 * - This allows non-blocking async operations
 */
suspend fun screen(state: SealedState) {
    // ============================================
    // EXHAUSTIVE WHEN EXPRESSION
    // ============================================
    // The compiler ensures you handle ALL possible states
    // If you forget a case, you get a compile error!
    
    when (state) {
        is SealedState.Loading -> {
            println("Its Loading Please Wait")
        }
        
        is SealedState.HomeScreen -> {
            // Smart cast: After 'is' check, compiler knows 'state' is HomeScreen
            // So we can access 'state.name' without casting
            println("We Are At ${state.name}")
            countTen()  // Simulate loading profile data
        }
        
        is SealedState.Error -> {
            println("Error Occurred")
        }
        
        // If we added a new state and forgot to handle it here,
        // the compiler would give us an error! That's the power of sealed classes.
    }
}

/**
 * SUSPEND FUNCTION: SIMULATING ASYNC DATA LOADING
 * ------------------------------------------------
 * 
 * This function simulates:
 * 1. Loading profile data (with dots animation)
 * 2. Displaying the loaded data
 * 
 * COROUTINE BENEFITS:
 * -------------------
 * - delay() doesn't block the thread
 * - Other coroutines can run while this waits
 * - Non-blocking async operations
 * - Better performance than Thread.sleep()
 */
suspend fun countTen() {
    // Simulate loading with animated dots
    print("Loading Profile")
    for (i in 1..10) {
        delay(300)  // Wait 300ms (non-blocking!)
        print(".")
    }
    println()
    
    // Simulate data being loaded
    println("Profile loaded successfully!")
    delay(800)
    
    // Display the loaded data
    println("Name: John Doe")
    println("Age: 30")
    println("Email: john.doe@example.com")
    delay(300)
    println("Location: New York")
    println("Bio: Software Developer passionate about Kotlin")
    delay(300)
}

/**
 * MAIN FUNCTION WITH COROUTINE SCOPE
 * -----------------------------------
 * 
 * runBlocking { ... }
 * - Creates a coroutine scope
 * - Blocks the current thread until all coroutines complete
 * - Used in main() to bridge blocking and non-blocking code
 * 
 * WHY runBlocking IN MAIN?
 * ------------------------
 * - main() is a regular function (not suspend)
 * - We need a coroutine scope to call suspend functions
 * - runBlocking provides that scope
 * - In real apps, you'd use lifecycleScope or viewModelScope
 */
fun main() = runBlocking {
    // Start with HomeScreen state
    // This triggers the when expression in screen()
    screen(SealedState.HomeScreen(name = "Hallo Loite Guten Tag"))
}

/**
 * ============================================
 * REAL-WORLD USAGE PATTERNS
 * ============================================
 * 
 * 1. UI STATE MANAGEMENT (Android/Multiplatform):
 *    sealed class UiState {
 *        object Loading : UiState()
 *        data class Success(val data: List<Item>) : UiState()
 *        data class Error(val message: String) : UiState()
 *    }
 * 
 * 2. NETWORK RESULTS:
 *    sealed class Result<out T> {
 *        data class Success<T>(val data: T) : Result<T>()
 *        data class Error(val exception: Exception) : Result<Nothing>()
 *    }
 * 
 * 3. NAVIGATION STATES:
 *    sealed class NavigationState {
 *        object Home : NavigationState()
 *        data class Detail(val id: String) : NavigationState()
 *        object Back : NavigationState()
 *    }
 * 
 * KEY BENEFITS:
 * -------------
 * - Compiler-enforced exhaustiveness
 * - Type-safe state transitions
 * - Clear API contracts
 * - Perfect for functional programming patterns
 */

