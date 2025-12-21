package basics

/**
 * ============================================
 * KOTLIN CONCEPT: BASIC GENERICS
 * ============================================
 * 
 * WHAT ARE GENERICS?
 * ------------------
 * Generics allow you to write code that works with different types while maintaining type safety.
 * Instead of writing separate classes for String-Int pairs, String-String pairs, etc., 
 * you write ONE generic class that works with ANY types.
 * 
 * WHY USE GENERICS?
 * -----------------
 * 1. Type Safety: The compiler ensures you use the correct types
 * 2. Code Reusability: Write once, use with many types
 * 3. No Casting: No need for unsafe type casts
 * 
 * KEY CONCEPTS:
 * - Type Parameters: <A, B> are placeholders for actual types
 * - Type Arguments: When you use Pair2<String, Int>, String and Int are the type arguments
 * - Type Inference: Kotlin can often figure out types automatically
 */

fun main() {
    // ============================================
    // EXAMPLE 1: Creating a Generic Pair Class
    // ============================================
    
    // Create a pair with String and Int
    val nameAgePair = Pair2("Alice", 30)
    println("Name: ${nameAgePair.first}, Age: ${nameAgePair.second}")
    
    // The compiler KNOWS the types:
    val name: String = nameAgePair.first  // ✅ Safe - compiler guarantees this is String
    val age: Int = nameAgePair.second     // ✅ Safe - compiler guarantees this is Int
    
    // This would cause a compile error:
    // val wrongType: Int = nameAgePair.first  // ❌ Compile error: Type mismatch
    
    // ============================================
    // EXAMPLE 2: Different Type Combinations
    // ============================================
    
    val coordinates = Pair2(10.5, 20.3)  // Double, Double
    val status = Pair2("OK", true)        // String, Boolean
    
    println("\nCoordinates: (${coordinates.first}, ${coordinates.second})")
    println("Status: ${status.first} = ${status.second}")
}

/**
 * GENERIC CLASS DEFINITION
 * ------------------------
 * 
 * class Pair2<A, B>
 *         ^   ^
 *         |   |
 *    Type parameters - these are placeholders
 * 
 * When you create an instance:
 * Pair2<String, Int>("Alice", 30)
 *        ^      ^
 *        |      |
 *   Type arguments - actual types replacing A and B
 * 
 * HOW IT WORKS:
 * - A and B are type parameters (like variables, but for types)
 * - When you create an instance, you specify what A and B actually are
 * - The compiler generates type-safe code for those specific types
 */
class Pair2<A, B>(
    val first: A,   // A can be any type: String, Int, Double, etc.
    val second: B   // B can be any type: String, Int, Double, etc.
) {
    /**
     * This class can hold ANY two types.
     * 
     * Examples:
     * - Pair2<String, Int> - first is String, second is Int
     * - Pair2<Double, Double> - both are Double
     * - Pair2<List<String>, Map<String, Int>> - even complex types!
     */
}

