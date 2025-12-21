package dsl

/**
 * ============================================
 * KOTLIN CONCEPT: DSL BUILDER PATTERN
 * ============================================
 * 
 * WHAT IS A DSL?
 * --------------
 * DSL (Domain Specific Language) is a way to write code that reads
 * like a natural language, specific to a particular domain.
 * 
 * BUILDER PATTERN WITH LAMBDAS:
 * -----------------------------
 * Instead of:
 *   val sandwich = Sandwich()
 *   sandwich.bread = "Sourdough"
 *   sandwich.toasted = true
 *   sandwich.addFilling("Avocado")
 * 
 * You write:
 *   val sandwich = makeSandwich {
 *       bread = "Sourdough"
 *       toasted = true
 *       addFilling("Avocado")
 *   }
 * 
 * KEY CONCEPTS:
 * ------------
 * 1. Lambda with receiver (Sandwich.() -> Unit)
 * 2. Builder function that creates and configures objects
 * 3. Nested DSLs for complex structures
 */

fun main() {
    // ============================================
    // DSL IN ACTION: Building a Sandwich
    // ============================================
    
    val myLunch = makeSandwich {
        // We are "inside" the Sandwich object now!
        // 'this' refers to the new Sandwich we just created.
        // This is called a "lambda with receiver"
        
        bread = "Sourdough"  // Same as this.bread = "Sourdough"
        toasted = true
        
        // Nested DSL: Configuring sauce
        sauce {
            type = "Mayo"
            spicy = false
        }
        
        // Calling methods directly
        addFilling("Avocado")
        addFilling("Egg")
    }
    
    println("My lunch: $myLunch")
}

/**
 * THE BLUEPRINT: SANDWICH CLASS
 * ------------------------------
 * 
 * This is the class we want to build using DSL syntax.
 * Notice how properties and methods are designed to work
 * well with the builder pattern.
 */
class Sandwich {
    var bread: String = "White"  // Default value
    var toasted: Boolean = false
    
    var mySauce: Sauce? = null
    private val fillings = mutableListOf<String>()
    
    /**
     * NESTED DSL FUNCTION
     * -------------------
     * This creates a nested DSL for configuring Sauce.
     * 
     * Syntax: sauce { ... }
     * 
     * HOW IT WORKS:
     * 1. Create a Sauce object
     * 2. Run the lambda block on it (configure it)
     * 3. Save it to mySauce
     * 
     * The lambda has Sauce as receiver, so inside { } you're
     * working with a Sauce object.
     */
    fun sauce(block: Sauce.() -> Unit) {
        val s = Sauce()  // Create
        s.block()        // Configure (run the lambda)
        mySauce = s      // Save
    }
    
    /**
     * Helper function to add fillings
     */
    fun addFilling(item: String) {
        fillings.add(item)
    }
    
    override fun toString(): String {
        val sauceInfo = mySauce?.toString() ?: "No sauce"
        return "Sandwich(bread='$bread', toasted=$toasted, sauce=$sauceInfo, fillings=$fillings)"
    }
}

/**
 * SAUCE CLASS
 * -----------
 * Simple class for nested DSL demonstration
 */
class Sauce {
    var type: String = "Mayo"
    var spicy: Boolean = false
    
    override fun toString() = "$type (Spicy: $spicy)"
}

/**
 * THE BUILDER FUNCTION
 * --------------------
 * 
 * fun makeSandwich(block: Sandwich.() -> Unit): Sandwich
 *                      ^
 *                      |
 *              Lambda with receiver
 * 
 * WHAT IS "LAMBDA WITH RECEIVER"?
 * --------------------------------
 * - block: Sandwich.() -> Unit
 * - This means: a function that takes a Sandwich as receiver
 * - Inside the lambda, 'this' refers to the Sandwich
 * - You can access Sandwich's members directly
 * 
 * HOW IT WORKS:
 * -------------
 * 1. Create a blank Sandwich object
 * 2. Call block() on it (this runs the code inside { })
 * 3. The code inside { } runs AS IF it's inside the Sandwich class
 * 4. Return the configured Sandwich
 * 
 * STEP-BY-STEP:
 * -------------
 * When you write:
 *   makeSandwich {
 *       bread = "Sourdough"
 *   }
 * 
 * Kotlin does:
 *   1. Creates Sandwich()
 *   2. Calls sandwich.block() where block is { bread = "Sourdough" }
 *   3. Inside block, 'this' = sandwich, so this.bread = "Sourdough"
 *   4. Returns configured sandwich
 */
fun makeSandwich(block: Sandwich.() -> Unit): Sandwich {
    // Step A: Create the blank object
    val sandwich = Sandwich()
    
    // Step B: Let the user configure it
    // 'block' is the code inside the { } curly braces
    // By calling sandwich.block(), we run that code AS IF we are inside the 'sandwich' object
    sandwich.block()
    
    // Step C: Return the finished product
    return sandwich
}

/**
 * ============================================
 * REAL-WORLD EXAMPLES
 * ============================================
 * 
 * 1. HTML BUILDERS (like in your DSLs.kt):
 *    html {
 *        body {
 *            div { +"Hello" }
 *        }
 *    }
 * 
 * 2. GRADLE BUILD SCRIPTS:
 *    android {
 *        compileSdkVersion 33
 *        defaultConfig {
 *            applicationId "com.example"
 *        }
 *    }
 * 
 * 3. ANKO LAYOUTS (Android):
 *    verticalLayout {
 *        textView("Hello")
 *        button("Click me")
 *    }
 * 
 * 4. KTOR ROUTING:
 *    routing {
 *        get("/") {
 *            call.respondText("Hello")
 *        }
 *    }
 * 
 * ============================================
 * KEY TAKEAWAYS
 * ============================================
 * 
 * 1. Lambda with receiver: Type.() -> Unit
 * 2. Builder function creates and configures objects
 * 3. Nested DSLs for complex structures
 * 4. Makes code more readable and domain-specific
 * 5. Compile-time safety (unlike string-based DSLs)
 */

