package advanced

/**
 * ============================================
 * KOTLIN CONCEPT: NOTHING TYPE AND VARIANCE
 * ============================================
 * 
 * WHAT IS NOTHING?
 * ----------------
 * Nothing is a special type in Kotlin that represents:
 * - A value that NEVER exists
 * - A function that NEVER returns normally (always throws)
 * - The bottom type (subtype of all types)
 * 
 * NOTHING IN VARIANCE:
 * --------------------
 * When used with variance, Nothing allows you to represent
 * "no value" or "error state" in a type-safe way.
 * 
 * KEY INSIGHT:
 * ------------
 * NetworkResult<Nothing> means "a result that contains nothing"
 * This is perfect for error cases where there's no success data.
 */

fun main() {
    // ============================================
    // EXAMPLE: Using Nothing in Sealed Interfaces
    // ============================================
    
    // Create an error result that contains Nothing
    // Nothing means "no value" - perfect for errors!
    val error: NetworkResultt<Nothing> = NetworkResultt.Error("Failed")
    
    // ============================================
    // THE MAGIC: Nothing allows universal assignment
    // ============================================
    // Because Nothing is a subtype of EVERY type, and we used 'out T',
    // NetworkResult<Nothing> can be assigned to NetworkResult<AnyType>
    
    // Can be used as any NetworkResult type:
    val intResult: NetworkResultt<Int> = error      // ✅ Works!
    val stringResult: NetworkResultt<String> = error // ✅ Works!
    val anyResult: NetworkResultt<Any> = error      // ✅ Works!
    
    println("Error result: $intResult")
    
    // ============================================
    // WHY THIS IS USEFUL
    // ============================================
    // You can have a single error type that works with all result types:
    // - NetworkResult<Int>.Error
    // - NetworkResult<String>.Error
    // - NetworkResult<User>.Error
    // All can use the same Error case with Nothing!
}

/**
 * SEALED INTERFACE WITH VARIANCE AND NOTHING
 * -------------------------------------------
 * 
 * sealed interface NetworkResult<out T>
 *                      ^
 *                      |
 *                  Covariant (out)
 * 
 * WHY 'out T'?
 * ------------
 * - Success<T> produces a T (output)
 * - Error produces Nothing (no output)
 * - We only read from NetworkResult, never write
 * - Therefore, covariance is safe and useful
 * 
 * NOTHING IN ERROR CASE:
 * ----------------------
 * Error(val message: String) : NetworkResult<Nothing>
 *                                      ^
 *                                      |
 *                                  Nothing = no value
 * 
 * This means:
 * - Error doesn't contain any success data
 * - Error can be used with any NetworkResult type
 * - Type-safe error handling
 */
sealed interface NetworkResultt<out T> {
    /**
     * SUCCESS CASE
     * ------------
     * Contains actual data of type T
     * 
     * Example: Success(42) → NetworkResult<Int>
     *          Success("hello") → NetworkResult<String>
     */
    data class Success<T>(val data: T) : NetworkResultt<T>
    
    /**
     * ERROR CASE
     * ----------
     * Contains no data (Nothing)
     * 
     * Why Nothing?
     * - Errors don't have success data
     * - Nothing is subtype of all types
     * - Allows Error to work with any NetworkResult<T>
     * 
     * Example:
     *   NetworkResult.Error("Failed") → NetworkResult<Nothing>
     *   But can be used as NetworkResult<Int>, NetworkResult<String>, etc.
     */
    data class Error(val message: String) : NetworkResultt<Nothing>
}

/**
 * ============================================
 * HOW NOTHING WORKS
 * ============================================
 * 
 * TYPE HIERARCHY:
 * ---------------
 * 
 *        Any?
 *         ↑
 *    (all types)
 *         ↑
 *      Nothing
 *    (bottom type)
 * 
 * Nothing is a subtype of EVERY type:
 * - Nothing <: Int
 * - Nothing <: String
 * - Nothing <: Any
 * - Nothing <: Any?
 * 
 * WITH VARIANCE:
 * --------------
 * Because Nothing <: T (for any T), and we have 'out T':
 * - NetworkResult<Nothing> <: NetworkResult<T> (for any T)
 * - This allows Error to work with all result types
 * 
 * PRACTICAL EXAMPLE:
 * ------------------
 * 
 * fun processResult(result: NetworkResult<Int>) {
 *     when (result) {
 *         is Success -> println(result.data)  // data is Int
 *         is Error -> println(result.message)  // No data, just message
 *     }
 * }
 * 
 * val error: NetworkResult<Nothing> = NetworkResult.Error("Failed")
 * processResult(error)  // ✅ Works! Error can be NetworkResult<Int>
 */

/**
 * ============================================
 * OTHER USES OF NOTHING
 * ============================================
 * 
 * 1. FUNCTIONS THAT NEVER RETURN:
 *    fun fail(): Nothing = throw Exception("Error")
 * 
 * 2. EMPTY COLLECTIONS:
 *    val empty: List<Nothing> = emptyList()
 *    val anyList: List<Any> = empty  // ✅ Works!
 * 
 * 3. TYPE-SAFE NULL HANDLING:
 *    val x: String? = null
 *    val y: String = x ?: throw Exception()  // throw returns Nothing
 * 
 * KEY TAKEAWAYS:
 * --------------
 * 1. Nothing = "no value" or "never returns"
 * 2. Nothing is subtype of all types (bottom type)
 * 3. Perfect for error cases in sealed interfaces
 * 4. Enables type-safe error handling with variance
 */

