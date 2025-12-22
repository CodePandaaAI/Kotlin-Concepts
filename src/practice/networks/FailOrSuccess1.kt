package practice.networks

// For Some professional code for your answer i can think of this solution, for the input and output
//fun main() {
//    when(val result1 = doSomething(success = true)) {
//        is Result.Success -> println("Success! Data: ${result1.data}")
//        is Result.Error -> println("Failed! Error Message: ${result1.message}")
//    }
//
//    when(val result2 = doSomething(success = false)) {
//        is Result.Success -> println("Success! Data: ${result2.data}")
//        is Result.Error -> println("Failed! Error Message: ${result2.message}")
//    }
//}
//
//private fun doSomething(success: Boolean): Result {
//    return when {
//        success -> Result.Success("Here is your data")
//        else -> Result.Error("Something went wrong")
//    }
//}
//
//private sealed interface Result {
//    data class Success(val data: String) : Result
//    data class Error(val message: String) : Result
//}

/** If it's very simple and exactly what u said for input and output,
 *  then this can work as well, while this own its own has no meaning, this code is BS,
 *  but for just saying on the most simplest level i think of this code or just if-else
 *
 */
//fun main() {
//    val result1 = doSomething(success = true)
//    println(result1)
//    val result2 = doSomething(success = true)
//    println(result2)
//}
//
//fun doSomething(success: Boolean): String {
//    return when {
//        success -> "Success! Data: Here is your data"
//        else -> "Failed! Error: Something went wrong"
//    }
//}