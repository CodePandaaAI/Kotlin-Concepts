package basics

/**
 * ============================================
 * KOTLIN CONCEPT: EXTENSION FUNCTIONS (BASIC)
 * ============================================
 * 
 * WHAT ARE EXTENSION FUNCTIONS?
 * -----------------------------
 * Extension functions allow you to add new functions to existing classes
 * WITHOUT modifying their source code. It's like adding methods to classes
 * you don't own (like String, List, etc.).
 * 
 * SYNTAX:
 * fun ReceiverType.functionName(): ReturnType { ... }
 *      ^            ^
 *      |            |
 *   The class   The new function
 *   you're      you're adding
 *   extending
 * 
 * WHY USE EXTENSION FUNCTIONS?
 * ----------------------------
 * 1. Readability: Makes code read more naturally
 * 2. Organization: Group related functionality together
 * 3. No Inheritance: Add functionality without subclassing
 * 4. Null Safety: Can be called on nullable types safely
 */

fun main() {
    val text = "hello world"
    
    // ============================================
    // COMPARISON: Normal Function vs Extension Function
    // ============================================
    
    // NORMAL FUNCTION CALL:
    // - Function name comes first
    // - Object is passed as parameter
    // - Less natural reading flow
    println("Normal function: ${addExclamation(text)}")
    // Reads: "add exclamation to text"
    
    // EXTENSION FUNCTION CALL:
    // - Object comes first
    // - Function looks like a method
    // - More natural, reads like English
    println("Extension function: ${text.addExclamation2()}")
    // Reads: "text, add exclamation"
    
    // ============================================
    // KEY INSIGHT: They're the Same Under the Hood!
    // ============================================
    // Both functions do the same thing, but extension functions
    // provide better readability and feel more "Kotlin-like"
}

/**
 * NORMAL FUNCTION
 * ---------------
 * This is a regular function that takes a String parameter.
 * 
 * Usage: addExclamation(text)
 *        ^            ^
 *        |            |
 *    function    parameter
 */
fun addExclamation(s: String): String {
    return "$s!"
}

/**
 * EXTENSION FUNCTION
 * ------------------
 * This adds a new method to the String class.
 * 
 * Syntax breakdown:
 * fun String.addExclamation2(): String
 *     ^     ^                ^
 *     |     |                |
 *     |     |         Return type
 *     |     |
 *     |  Function name
 *     |
 *  Receiver type (the class being extended)
 * 
 * Inside the function:
 * - 'this' refers to the String object the function was called on
 * - In text.addExclamation2(), 'this' = "hello world"
 * 
 * HOW IT WORKS:
 * 1. You call: text.addExclamation2()
 * 2. Kotlin converts it to: addExclamation2(text)
 * 3. The 'this' keyword inside refers to 'text'
 * 
 * IMPORTANT NOTES:
 * - Extension functions are STATIC - they don't have access to private members
 * - They're resolved statically (at compile time), not dynamically (at runtime)
 * - If a class already has a method with the same signature, the class method wins
 */
fun String.addExclamation2(): String {
    // 'this' refers to the String instance that called this function
    // If you called "hello".addExclamation2(), then this = "hello"
    return "$this!"
}

