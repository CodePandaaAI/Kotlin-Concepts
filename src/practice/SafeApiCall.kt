package practice

// PRACTICE: Safe API Call Wrapper
//
// One generic function wraps any API lambda: try/catch + status check → ApiResult<T>.
// Combines generics, higher-order functions, sealed Result, Nothing on Error.
//
// Prerequisite: patterns/ResultPattern.kt, functions/HigherOrderFunctions.kt

fun main() {

    println("String API:  ${safeApiCall { fakeStringApi() }}")
    println("User API:    ${safeApiCall { fakeUserApi() }}")
    println("Int API:     ${safeApiCall { fakeIntApi() }}")
    println("Broken API:  ${safeApiCall { fakeBrokenApi() }}")

    when (val result = safeApiCall { fakeUserApi() }) {
        is ApiResult.Success -> println("\nWelcome, ${result.data.name}!")
        is ApiResult.Error -> println("\nFailed: ${result.message}")
    }
}

sealed interface ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error(val message: String) : ApiResult<Nothing>
}

data class UserData(val name: String)
data class ApiResponse<T>(val status: Int, val body: T)

fun fakeStringApi(): ApiResponse<String> = ApiResponse(200, "Hello from API!")
fun fakeUserApi(): ApiResponse<UserData> = ApiResponse(200, UserData("Alice"))
fun fakeIntApi(): ApiResponse<Int> = ApiResponse(200, 42)
fun fakeBrokenApi(): ApiResponse<String> = throw RuntimeException("Network timeout!")

fun <T> safeApiCall(apiCall: () -> ApiResponse<T>): ApiResult<T> {
    return try {
        val response = apiCall()
        if (response.status in 200..299) {
            ApiResult.Success(response.body)
        } else {
            ApiResult.Error("HTTP Error: ${response.status}")
        }
    } catch (e: Exception) {
        ApiResult.Error("Exception: ${e.message}")
    }
}
