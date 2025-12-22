package practice.networks

fun main() {
    // Now it can return different types!
    val stringResult: ResultO<String> = safeApiCall { fakeStringApi() }
    println(stringResult)

    val userResult: ResultO<UserO> = safeApiCall { fakeUserApi() }
    println(userResult)

    val intResult: ResultO<Int> = safeApiCall { fakeIntApi() }
    println(intResult)
}

data class UserO(val name: String)
data class FakeResponse<T>(val status: Int, val body: T)

fun fakeStringApi(): FakeResponse<String> = FakeResponse(200, "Hello")
fun fakeUserApi(): FakeResponse<UserO> = FakeResponse(200, UserO("Alice"))
fun fakeIntApi(): FakeResponse<Int> = FakeResponse(200, 42)

// Generic version - T can be String, User, Int, anything!
fun <T> safeApiCall(apiCall: () -> FakeResponse<T>): ResultO<T> {
    return try {
        val response = apiCall()

        if (response.status in 200..299) {
            ResultO.Success(response.body)  // body is type T
        } else {
            ResultO.Error("HTTP ${response.status}")
        }
    } catch (e: Exception) {
        ResultO.Error("Exception: ${e.message}")
    }
}

sealed interface ResultO<out T> {
    data class Success<T>(val data: T) : ResultO<T>
    data class Error(val message: String) : ResultO<Nothing>
}