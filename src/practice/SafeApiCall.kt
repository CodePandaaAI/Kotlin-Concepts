package practice

// ============================================
//  PRACTICE: SAFE API CALL WRAPPER
// ============================================
//
//  Real apps make API calls that can fail:
//    - Network timeout
//    - Server error (500)
//    - Invalid response
//
//  Instead of try-catching every single API call,
//  we write ONE generic wrapper that handles ALL errors.
//
//  This file combines:
//    - Generics (<T>) — works for ANY API response type
//    - Higher-order functions — passing the API call as a lambda
//    - Sealed interfaces — type-safe Result
//    - Try-catch — catching exceptions
//
//  TRY THIS:
//  1. Run the code and see both success and failure outputs
//  2. Change fakeStringApi to return status 404
//  3. Add a new API for a custom data class

fun main() {

    // ============================================
    //  STEP 1: WRAP DIFFERENT API CALLS
    // ============================================
    //
    //  safeApiCall wraps ANY API function and catches errors.
    //  One wrapper, any API. That's the power of generics + lambdas.

    val stringResult: ApiResult<String> = safeApiCall { fakeStringApi() }
    println("String API:  $stringResult")

    val userResult: ApiResult<UserData> = safeApiCall { fakeUserApi() }
    println("User API:    $userResult")

    val intResult: ApiResult<Int> = safeApiCall { fakeIntApi() }
    println("Int API:     $intResult")



    // ============================================
    //  STEP 2: HANDLE FAILURES GRACEFULLY
    // ============================================

    val failResult: ApiResult<String> = safeApiCall { fakeBrokenApi() }
    println("Broken API:  $failResult")

    // No crash! The exception was caught inside safeApiCall
    // and returned as an Error instead of crashing the app.



    // ============================================
    //  STEP 3: HANDLE THE RESULT
    // ============================================

    when (val result = safeApiCall { fakeUserApi() }) {
        is ApiResult.Success -> {
            println("\nWelcome, ${result.data.name}!")
        }
        is ApiResult.Error -> {
            println("\nFailed: ${result.message}")
            // Show error UI, retry, log to analytics, etc.
        }
    }
}


// ============================================
//  RESULT TYPE
// ============================================

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val message: String) : ApiResult<Nothing>
}


// ============================================
//  FAKE API RESPONSES
// ============================================

data class UserData(val name: String)
data class ApiResponse<T>(val status: Int, val body: T)

fun fakeStringApi(): ApiResponse<String> = ApiResponse(200, "Hello from API!")
fun fakeUserApi(): ApiResponse<UserData> = ApiResponse(200, UserData("Alice"))
fun fakeIntApi(): ApiResponse<Int> = ApiResponse(200, 42)
fun fakeBrokenApi(): ApiResponse<String> = throw RuntimeException("Network timeout!")


// ============================================
//  THE STAR: GENERIC SAFE API CALL
// ============================================
//
//  fun <T> safeApiCall(apiCall: () -> ApiResponse<T>): ApiResult<T>
//       ^              ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^   ^^^^^^^^^^^
//       |              |                                |
//    Generic        Lambda parameter                 Return type
//    "Works for     "Give me a function that         "You get back
//     any T"         returns ApiResponse<T>"           ApiResult<T>"
//
//  HOW IT WORKS:
//    1. Call the lambda inside try-catch
//    2. Check the HTTP status code
//    3. Return Success with the body, or Error with the message
//    4. If the lambda throws, catch it and return Error
//
//  ONE function. Handles ANY API call. Catches ALL exceptions.

fun <T> safeApiCall(apiCall: () -> ApiResponse<T>): ApiResult<T> {
    return try {
        val response = apiCall()  // Call the API

        if (response.status in 200..299) {
            ApiResult.Success(response.body)
            // body is type T, so this is ApiResult<T> ✅
        } else {
            ApiResult.Error("HTTP Error: ${response.status}")
            // Error is ApiResult<Nothing>
            // Function wants ApiResult<T>
            // Nothing <: T → ApiResult<Nothing> <: ApiResult<T> (via 'out')
            // Fits! ✅
        }
    } catch (e: Exception) {
        ApiResult.Error("Exception: ${e.message}")
        // Same thing — Error fits into any ApiResult<T>
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "In a real app, would apiCall be a suspend function?"
//
//  A: Yes! In production:
//       suspend fun <T> safeApiCall(apiCall: suspend () -> ApiResponse<T>): ApiResult<T>
//     The 'suspend' keyword makes it async (coroutines).
//     But the pattern is identical.
//
//
//  Q: "Why pass the API call as a lambda instead of calling it directly?"
//
//  A: Because we want ONE wrapper for ALL API calls:
//       safeApiCall { getUsers() }
//       safeApiCall { getProducts() }
//       safeApiCall { login(username, password) }
//     The lambda lets each caller provide THEIR specific call.
//     The wrapper provides the shared error handling.
//
//
//  Q: "Can I add retry logic?"
//
//  A: Yes! Just wrap the try-catch in a loop:
//       repeat(3) {
//           val result = try { ... } catch { ... }
//           if (result is ApiResult.Success) return result
//       }
//       return ApiResult.Error("Failed after 3 retries")