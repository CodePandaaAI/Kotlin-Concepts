package oop

/**
 * ============================================
 * KOTLIN CONCEPT: ANONYMOUS OBJECTS
 * ============================================
 * 
 * WHAT ARE ANONYMOUS OBJECTS?
 * ----------------------------
 * Anonymous objects are objects created on-the-fly without a class name.
 * They're useful when you need to implement an interface or extend a class
 * but don't want to create a full class definition.
 * 
 * WHEN TO USE:
 * ------------
 * 1. One-time implementations: When you only need an object once
 * 2. Quick implementations: For simple interface implementations
 * 3. Callbacks: Implementing listeners or callbacks inline
 * 4. Testing: Creating mock objects quickly
 * 
 * SYNTAX:
 * -------
 * object : InterfaceName { ... }
 * object : ClassName() { ... }
 */

fun main() {
    // ============================================
    // APPROACH 1: Using a Named Class
    // ============================================
    // This is the traditional OOP approach:
    // 1. Define an interface
    // 2. Create a class that implements it
    // 3. Create an instance of that class
    
    val greeter1 = EnglishGreeter()
    greeter1.greet("Alice")  // Output: Hello, Alice!
    
    // ============================================
    // APPROACH 2: Using an Anonymous Object
    // ============================================
    // This is the Kotlin way when you only need the object once:
    // 1. Define an interface (same as before)
    // 2. Create an object inline that implements it
    // 3. No separate class needed!
    
    val greeter2 = object : Greeter {
        // This is an anonymous object - it has no name!
        // It implements Greeter interface inline
        override fun greet(name: String) {
            println("Bonjour, $name!")  // French greeting
        }
    }
    greeter2.greet("Bob")  // Output: Bonjour, Bob!
    
    // ============================================
    // KEY DIFFERENCES
    // ============================================
    // Named Class (EnglishGreeter):
    // - Can be reused multiple times
    // - Has a name you can reference
    // - Better for complex implementations
    
    // Anonymous Object (greeter2):
    // - Created once, used once
    // - No class name needed
    // - Perfect for simple, one-off implementations
}

/**
 * INTERFACE DEFINITION
 * --------------------
 * This interface defines a contract: any class implementing it
 * must provide a greet function that takes a String.
 */
interface Greeter {
    fun greet(name: String)
}

/**
 * NAMED CLASS IMPLEMENTATION
 * ---------------------------
 * This is a full class that implements the Greeter interface.
 * 
 * Pros:
 * - Can be reused
 * - Can have additional methods/properties
 * - Can be extended further
 * 
 * Cons:
 * - More verbose for simple cases
 * - Creates a separate class file
 */
class EnglishGreeter : Greeter {
    override fun greet(name: String) {
        println("Hello, $name!")
    }
}

/**
 * ANONYMOUS OBJECT EXPLANATION
 * -----------------------------
 * 
 * val greeter2 = object : Greeter { ... }
 *                ^      ^
 *                |      |
 *                |   Interface to implement
 *                |
 *            'object' keyword creates an anonymous object
 * 
 * INSIDE THE OBJECT:
 * - You can override interface methods
 * - You can add additional properties/methods
 * - You can access variables from the enclosing scope
 * 
 * REAL-WORLD USE CASES:
 * ---------------------
 * 1. Event Listeners:
 *    button.setOnClickListener(object : View.OnClickListener {
 *        override fun onClick(v: View) { ... }
 *    })
 * 
 * 2. Comparators:
 *    val comparator = object : Comparator<String> {
 *        override fun compare(a: String, b: String) = a.length - b.length
 *    }
 * 
 * 3. Quick Implementations:
 *    val callback = object : Callback {
 *        override fun onSuccess() { ... }
 *        override fun onError() { ... }
 *    }
 */

