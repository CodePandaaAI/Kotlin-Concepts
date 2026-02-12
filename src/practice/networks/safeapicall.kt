package practice.networks

fun main() {
    // Now it can return different types!
    val stringResult: Result<String> = safeApiCall { fakeStringApi() }
    println(stringResult)

    val userResult: Result<UserO> = safeApiCall { fakeUserApi() }
    println(userResult)

    val intResult: Result<Int> = safeApiCall { fakeIntApi() }
    println(intResult)
}

data class UserO(val name: String)
data class FakeResponse<T>(val status: Int, val body: T)

fun fakeStringApi(): FakeResponse<String> = FakeResponse(200, "Hello")
fun fakeUserApi(): FakeResponse<UserO> = FakeResponse(200, UserO("Alice"))
fun fakeIntApi(): FakeResponse<Int> = FakeResponse(200, 42)

// Generic version - T can be String, User, Int, anything!
fun <T> safeApiCall(apiCall: () -> FakeResponse<T>): Result<T> {
    return try {
        val response = apiCall()

        if (response.status in 200..299) {
            Result.Success(response.body)  // body is type T
        } else {
            Result.Error("HTTP ${response.status}")
        }
    } catch (e: Exception) {
        Result.Error("Exception: ${e.message}")
    }
}