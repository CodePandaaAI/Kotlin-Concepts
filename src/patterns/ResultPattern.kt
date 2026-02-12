package patterns

// ============================================
//  KOTLIN PATTERN: RESULT<T> (Type-Safe Error Handling)
// ============================================
//
//  THE PROBLEM:
//  Traditional error handling uses try-catch and exceptions.
//  But exceptions are INVISIBLE in function signatures:
//
//    fun getUser(id: Int): User { ... }
//
//  Can this throw? Maybe. Maybe not. The signature doesn't tell you.
//  You just hope someone wrote documentation. Hope is not a strategy.
//
//  THE SOLUTION:
//  Instead of throwing exceptions, RETURN the error:
//
//    fun getUser(id: Int): Result<User> { ... }
//
//  Now the return type TELLS you: "This might fail."
//  And the compiler FORCES you to handle both cases.
//  No hope needed. The type system enforces it.
//
//  THIS PATTERN USES:
//    1. Sealed interface — closed set of outcomes
//    2. Generics with 'out T' — covariance
//    3. Nothing type — universal error type
//
//  PREREQUISITE: Read generics/Variance.kt first for out/Nothing!

fun main() {

    println("=== Result Pattern Demo ===\n")

    // ============================================
    //  STEP 1: USE RESULT IN A FUNCTION
    // ============================================

    val user1 = getUserById(1)
    val user2 = getUserById(999)

    // The compiler FORCES you to handle both cases:
    when (user1) {
        is Result.Success -> println("✅ Found: ${user1.data}")
        is Result.Error -> println("❌ Error: ${user1.message}")
    }

    when (user2) {
        is Result.Success -> println("✅ Found: ${user2.data}")
        is Result.Error -> println("❌ Error: ${user2.message}")
    }

    // No try-catch. No forgotten error paths.
    // The type system handles it.



    // ============================================
    //  STEP 2: CHAIN RESULTS WITH map()
    // ============================================
    //
    //  map() transforms the SUCCESS value without unwrapping.
    //  If it's an Error, it passes through unchanged.

    val greeting = getUserById(1).map { user ->
        "Hello, ${user.name}! Welcome back."
    }

    when (greeting) {
        is Result.Success -> println("\n${greeting.data}")
        is Result.Error -> println("\nCan't greet: ${greeting.message}")
    }

    // getUserById(1) returns Result<User>
    // .map { ... } transforms User → String
    // Result is now Result<String>
    //
    // If getUserById returned Error, map() skips the transformation.
    // Error passes through. No crash. No exception.
}


// ============================================
//  THE RESULT SEALED INTERFACE
// ============================================
//
//  Let's break down every part:
//
//  sealed interface Result<out T>
//  ^^^^^^ ^^^^^^^^^        ^^^
//  |      |                |
//  |      |          Covariant type parameter
//  |      |          "T only comes OUT"
//  |      |
//  |   Interface (no instances, just a contract)
//  |
//  Sealed: "Only Success and Error can implement me."

sealed interface Result<out T> {

    //  data class Success<T>(val data: T) : Result<T>
    //
    //  "But wait — val data: T means T goes IN via constructor.
    //   Doesn't 'out' forbid that?"
    //
    //  No! Constructor is a ONE-TIME EXCEPTION.
    //  After creation, 'data' is val (read-only) → T only comes OUT.
    //  Kotlin gives constructors a pass because it's safe.
    //
    //  "Why does Success need its own <T>?"
    //
    //  Success is a STANDALONE class. It doesn't get Result's <T> for free.
    //  Success<T> declares its OWN T, then connects it: : Result<T>
    //  "My T is the same as Result's T."

    data class Success<T>(val data: T) : Result<T>

    //  data class Error(val message: String) : Result<Nothing>
    //
    //  "Why Nothing and not Error<T>?"
    //
    //  Error has NO data of type T. Just a String message.
    //  Nothing = "I have no T. I HONESTLY have nothing to give."
    //
    //  Because of 'out':
    //    Nothing <: String → Result<Nothing> <: Result<String> ✅
    //    Nothing <: Int → Result<Nothing> <: Result<Int> ✅
    //    Nothing <: User → Result<Nothing> <: Result<User> ✅
    //
    //  ONE Error type fits into ANY Result<T>. Everywhere.
    //
    //  If we used Error<T> instead:
    //    Error<String>("err") ← for Result<String>
    //    Error<Int>("err")    ← for Result<Int> (IDENTICAL but different type!)
    //  Redundant. With Nothing, one Error serves all.

    data class Error(val message: String) : Result<Nothing>

    //  BONUS: Transform the success value
    fun <R> map(transform: (T) -> R): Result<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Error -> this  // Error passes through
        }
    }
}


// ============================================
//  EXAMPLE DATA
// ============================================

data class User(val id: Int, val name: String, val email: String)

private val users = listOf(
    User(1, "Alice", "alice@example.com"),
    User(2, "Bob", "bob@example.com"),
    User(3, "Charlie", "charlie@example.com")
)

fun getUserById(id: Int): Result<User> {
    val user = users.find { it.id == id }
    return if (user != null) {
        Result.Success(user)
        //  Success(User) : Result<User> ✅ — direct match
    } else {
        Result.Error("User with id=$id not found")
        //  Error : Result<Nothing>
        //  Function wants Result<User>
        //  Nothing <: User (always true)
        //  'out' means: Result<Nothing> <: Result<User>
        //  FITS! ✅
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Result is an interface. You can't create an instance of an interface.
//      So how can the return type be Result<User>?"
//
//  A: The return type says WHAT to expect, not WHAT to create.
//     You can't create a Result object directly:
//       return Result<User>()  // ❌ Can't instantiate interface!
//
//     But you CAN return objects that IMPLEMENT Result:
//       return Result.Success(user)  // ✅ Success IS-A Result
//       return Result.Error("msg")   // ✅ Error IS-A Result
//
//     This is polymorphism. The ACTUAL object is Success or Error.
//     The return type Result<User> just describes WHAT the caller gets.
//
//
//  Q: "How does Error fit into Result<User> if Error has no User?"
//
//  A: The full chain:
//       Error : Result<Nothing>            ← Error is Result<Nothing>
//       Nothing <: User                     ← Nothing is subtype of everything
//       'out' means: Result<Nothing> <: Result<User>  ← covariant subtyping
//       So Error fits into Result<User>    ✅
//
//
//  Q: "What if I want to add more info to Error, like error codes?"
//
//  A: Just add properties:
//       data class Error(val message: String, val code: Int = -1) : Result<Nothing>
//     Still uses Nothing. Still fits everywhere.
//
//
//  NEXT STEP: See LoginSystem.kt for a real-world example
//  using this pattern with API calls and repositories.
