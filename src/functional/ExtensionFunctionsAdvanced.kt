package functional

/**
 * ============================================
 * KOTLIN CONCEPT: ADVANCED EXTENSION FUNCTIONS
 * ============================================
 * 
 * This file covers advanced extension function concepts:
 * 1. Extension functions with generics
 * 2. Inline functions
 * 3. Reified type parameters
 * 4. Custom collection operations
 * 
 * ADVANCED TOPICS:
 * ----------------
 * - Generic extension functions
 * - Inline functions for performance
 * - Reified generics (runtime type checking)
 * - Higher-order functions with extensions
 */

fun main() {
    val list = listOf(1, 2, 3, 4, 5, 5, 6, 7, 8)
    
    // ============================================
    // EXAMPLE 1: Custom Filter Extension Function
    // ============================================
    // This demonstrates how built-in functions like filter() work internally
    val evenNumbers = list.myCustomFilter { it % 2 == 0 }
    println("Even numbers: $evenNumbers")
    
    // ============================================
    // EXAMPLE 2: Extension Function with Sealed Result
    // ============================================
    // Using sealed interfaces for type-safe results
    val result = list.isValidAndUseful()
    when (result) {
        is Result.IsUseful -> println(result.message)
        is Result.NotUseful -> println("Not useful")
    }
    
    // ============================================
    // EXAMPLE 3: Enhanced ForEach
    // ============================================
    // Custom forEach implementation
    print("Enhanced forEach: ")
    list.forEachEnhanced { print("$it ") }
    println()
}

/**
 * GENERIC EXTENSION FUNCTION WITH TYPE CONSTRAINT
 * -----------------------------------------------
 * 
 * fun <T : Number> List<T>.filterEven(): List<T>
 *      ^              ^
 *      |              |
 *      |         Extension on List
 *      |
 *   Type parameter with constraint
 *   (T must be a Number or its subtype)
 * 
 * TYPE CONSTRAINT EXPLANATION:
 * - <T : Number> means T must be a Number (Int, Double, Float, etc.)
 * - This allows us to use Number methods like toLong()
 * - Provides type safety at compile time
 * 
 * WHY THE CONSTRAINT?
 * - We need to convert to Long to check if even
 * - Not all types can be converted to Long
 * - Constraint ensures we only work with numeric types
 */
fun <T : Number> List<T>.filterEven(): List<T> {
    return this.filter { it.toLong() % 2 == 0L }
}

/**
 * EXTENSION FUNCTION RETURNING SEALED INTERFACE
 * ----------------------------------------------
 * 
 * This demonstrates:
 * 1. Extension functions can return complex types
 * 2. Sealed interfaces for type-safe results
 * 3. Pattern matching with when expressions
 * 
 * SEALED INTERFACE BENEFITS:
 * - Exhaustive when expressions (compiler checks all cases)
 * - Type-safe result handling
 * - Clear API contract
 */
fun <T> List<T>.isValidAndUseful(): Result {
    return if (this.isEmpty()) {
        Result.NotUseful
    } else {
        Result.IsUseful("Very Useful and valid this is very good of you")
    }
}

/**
 * SEALED INTERFACE FOR RESULTS
 * -----------------------------
 * 
 * Sealed interfaces are perfect for representing a fixed set of result types.
 * The compiler ensures you handle all possible cases in when expressions.
 */
sealed interface Result {
    data class IsUseful(val message: String) : Result
    data object NotUseful : Result
}

/**
 * INLINE FUNCTION: CUSTOM FILTER
 * -------------------------------
 * 
 * WHAT IS 'inline'?
 * -----------------
 * The 'inline' keyword tells the compiler to copy the function's code
 * directly into the call site instead of making a function call.
 * 
 * BENEFITS:
 * 1. Performance: No function call overhead
 * 2. Lambda optimization: Lambda code is inlined too
 * 3. Reified generics: Can use 'reified' keyword (see below)
 * 
 * WHEN TO USE:
 * - Small, frequently called functions
 * - Functions with lambda parameters
 * - When you need reified generics
 * 
 * WHEN NOT TO USE:
 * - Large functions (increases code size)
 * - Recursive functions
 * - Functions with many parameters
 * 
 * HOW IT WORKS:
 * -------------
 * Without inline:
 *   list.filter { it % 2 == 0 }
 *   → Creates a function call
 *   → Creates a lambda object
 *   → More memory, slower
 * 
 * With inline:
 *   list.myCustomFilter { it % 2 == 0 }
 *   → Code is copied directly here
 *   → Lambda code is inlined
 *   → Less memory, faster
 * 
 * PREDICATE EXPLANATION:
 * ----------------------
 * A predicate is a function that takes an element and returns Boolean.
 * It "predicates" (asserts) something about the element.
 * 
 * Examples:
 * - { it % 2 == 0 } → "Is it even?"
 * - { it > 10 } → "Is it greater than 10?"
 * - { it.startsWith("A") } → "Does it start with A?"
 */
inline fun <T> List<T>.myCustomFilter(predicate: (T) -> Boolean): List<T> {
    val resultList = mutableListOf<T>()
    
    // Iterate through each element
    for (item in this) {
        // Call the predicate function (the lambda you passed)
        // If it returns true, add the item to results
        if (predicate(item)) {
            resultList.add(item)
        }
    }
    
    return resultList
}

/**
 * INLINE FUNCTION: CUSTOM MAP
 * ----------------------------
 * 
 * Map transforms each element of a collection.
 * 
 * TRANSFORM FUNCTION:
 * - Takes an element of type T
 * - Returns a transformed element of type R
 * - Applied to every element in the collection
 * 
 * EXAMPLE:
 *   listOf(1, 2, 3).customMap { it * 2 }
 *   → Returns [2, 4, 6]
 */
inline fun <T, R> Iterable<T>.customMap(transform: (T) -> R): List<R> {
    val resultList = mutableListOf<R>()
    for (item in this) {
        resultList.add(transform(item))  // Transform and add
    }
    return resultList
}

/**
 * INLINE FUNCTION: ENHANCED FOR EACH
 * -----------------------------------
 * 
 * Similar to standard forEach, but demonstrates inline usage.
 * 
 * ACTION FUNCTION:
 * - Takes an element
 * - Performs a side effect (like printing)
 * - Returns Unit (nothing)
 */
inline fun <T> Iterable<T>.forEachEnhanced(action: (T) -> Unit): Unit {
    for (item in this) {
        action(item)  // Execute the action for each item
    }
}

/**
 * REIFIED TYPE PARAMETERS
 * -----------------------
 * 
 * WHAT IS 'reified'?
 * ------------------
 * Normally, generic type information is erased at runtime (type erasure).
 * With 'reified', you can check the actual type at runtime.
 * 
 * REQUIREMENTS:
 * - Function must be 'inline'
 * - Type parameter must be 'reified'
 * 
 * HOW IT WORKS:
 * -------------
 * Without reified:
 *   fun <T> checkType(item: Any) {
 *       if (item is T) { ... }  // ❌ Error: Cannot check type T at runtime
 *   }
 * 
 * With reified:
 *   inline fun <reified T> checkType(item: Any) {
 *       if (item is T) { ... }  // ✅ Works! Type T is known at runtime
 *   }
 * 
 * USE CASES:
 * ----------
 * - Type checking at runtime
 * - Creating instances of generic types
 * - Reflection-like operations
 * 
 * EXAMPLE USAGE:
 *   checkType<String>("hello")  // ✅ It's a match!
 *   checkType<Int>("hello")     // Not a match
 */
inline fun <reified T> checkType(item: Any) {
    // This is only possible because T is reified!
    // At runtime, we know what T actually is
    if (item is T) {
        println("It's a match! Item is of type ${T::class.simpleName}")
    } else {
        println("No match. Item is ${item::class.simpleName}, not ${T::class.simpleName}")
    }
}

