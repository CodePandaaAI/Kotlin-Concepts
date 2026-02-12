package functions

// ============================================
//  KOTLIN CONCEPT: LAMBDAS
// ============================================
//
//  A lambda is a function with NO NAME.
//
//  Normal function:
//    fun double(x: Int): Int {
//        return x * 2
//    }
//
//  Lambda (same thing, no name):
//    { x: Int -> x * 2 }
//
//  That's it. A lambda is just a nameless function.
//  You can store it, pass it around, or call it directly.

fun main() {

    // ============================================
    //  STEP 1: YOUR FIRST LAMBDA
    // ============================================

    // Normal function:
    fun double(x: Int): Int {
        return x * 2
    }

    // Same thing as a lambda, stored in a variable:
    val doubleLambda = { x: Int -> x * 2 }

    // Both work the same way:
    println(double(5))         // 10
    println(doubleLambda(5))   // 10

    // The lambda has no name.
    // It lives inside 'doubleLambda' variable.
    // You call it like a function.



    // ============================================
    //  STEP 2: LAMBDA SYNTAX BREAKDOWN
    // ============================================
    //
    //  { x: Int -> x * 2 }
    //    ^^^^^^    ^^^^^^^
    //    |         |
    //    Parameter  Body (what it returns)
    //
    //  Read it as: "Given x (an Int), return x * 2"
    //
    //  More examples:
    //    { name: String -> "Hello, $name!" }
    //    { a: Int, b: Int -> a + b }
    //    { -> println("No parameters!") }
    //    { println("Also no parameters!") }  // arrow optional when no params



    // ============================================
    //  STEP 3: LAMBDA TYPES
    // ============================================
    //
    //  Every lambda has a TYPE that describes:
    //    - What goes IN (parameters)
    //    - What comes OUT (return value)

    val greet: (String) -> String = { name -> "Hello, $name!" }
    //         ^^^^^^^^^^^^^^^^^
    //         |
    //    TYPE: "Takes a String, returns a String"

    val add: (Int, Int) -> Int = { a, b -> a + b }
    //       ^^^^^^^^^^^^^^^^^
    //       |
    //  TYPE: "Takes two Ints, returns an Int"

    val doNothing: () -> Unit = { println("No input, no return") }
    //             ^^^^^^^^^^
    //             |
    //        TYPE: "Takes nothing, returns nothing (Unit)"

    println(greet("Alice"))  // Hello, Alice!
    println(add(3, 4))       // 7



    // ============================================
    //  STEP 4: THE 'it' SHORTHAND
    // ============================================
    //
    //  When a lambda has EXACTLY ONE parameter,
    //  Kotlin gives it the default name: 'it'

    val numbers = listOf(1, 2, 3, 4, 5)

    // Explicit parameter name:
    val doubled1 = numbers.map { number -> number * 2 }

    // Using 'it' (same thing, shorter):
    val doubled2 = numbers.map { it * 2 }

    println(doubled2)  // [2, 4, 6, 8, 10]

    // 'it' only works with ONE parameter.
    // Two parameters? You must name them:
    // { a, b -> a + b }  ← 'it' would be ambiguous



    // ============================================
    //  STEP 5: PASSING LAMBDAS TO FUNCTIONS
    // ============================================
    //
    //  This is where lambdas become POWERFUL.
    //  You can pass different lambdas to the same function
    //  to get different behavior.

    fun doMath(a: Int, b: Int, operation: (Int, Int) -> Int): Int {
        //                      ^^^^^^^^^^^^^^^^^^^^^^^^
        //                      |
        //              "Give me a function that takes
        //               two Ints and returns an Int"
        return operation(a, b)
    }

    // Same function, different lambdas = different results:
    println(doMath(10, 3) { a, b -> a + b })  // 13 (addition)
    println(doMath(10, 3) { a, b -> a - b })  // 7  (subtraction)
    println(doMath(10, 3) { a, b -> a * b })  // 30 (multiplication)

    // The function doesn't know HOW to do math.
    // It lets YOU decide by passing a lambda.



    // ============================================
    //  STEP 6: TRAILING LAMBDA SYNTAX
    // ============================================
    //
    //  If the LAST parameter is a lambda,
    //  you can move it OUTSIDE the parentheses:

    // Normal (lambda inside parentheses):
    val evens1 = numbers.filter({ it % 2 == 0 })

    // Trailing lambda (lambda outside — cleaner):
    val evens2 = numbers.filter { it % 2 == 0 }

    // If the lambda is the ONLY parameter, drop () entirely:
    //   run({ println("Hi") })  →  run { println("Hi") }

    // THIS IS WHY KOTLIN CODE USES { } SO MUCH!
    //
    //   list.filter { it > 3 }
    //   list.map { it * 2 }
    //   list.forEach { println(it) }
    //
    //   They're all just function calls with trailing lambdas.



    // ============================================
    //  STEP 7: LAMBDA WITH RECEIVER (The DSL Secret!)
    // ============================================
    //
    //  This is the MOST IMPORTANT part for understanding DSLs.
    //
    //  Normal lambda:
    //    (String) -> String = { name -> "Hello, $name!" }
    //    "I RECEIVE a String as a parameter"
    //
    //  Lambda WITH RECEIVER:
    //    String.() -> String = { "Hello, $this!" }
    //    "I AM the String"
    //
    //  See the difference?

    // Normal: the String comes as a parameter
    val greet1: (String) -> String = { name -> "Hello, $name!" }

    // Receiver: YOU ARE the String, access via 'this'
    val greet2: String.() -> String = { "Hello, $this!" }

    // Calling them differently:
    println(greet1("Bob"))     // Pass "Bob" as argument
    println("Charlie".greet2()) // Call ON "Charlie" — like a method!

    // Inside greet2:
    //   'this' = "Charlie"
    //   You can call any String method directly: length, uppercase(), etc.

    // THIS is how DSLs work:
    //   makeSandwich {
    //       bread = "Sourdough"    ← same as this.bread = "Sourdough"
    //       toasted = true         ← same as this.toasted = true
    //   }
    //
    //   Inside the { }, 'this' is the Sandwich object.
    //   So you can set properties directly.

    // Real Kotlin stdlib example:
    val result = buildString {
        // Inside here, 'this' is a StringBuilder
        append("Hello, ")
        append("World!")
    }
    println(result)  // "Hello, World!"
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Is a lambda an object?"
//  A: Under the hood, yes. Kotlin wraps it in a Function object.
//     { x: Int -> x * 2 } becomes an object of type Function1<Int, Int>.
//     But 'inline' functions avoid this overhead (see HigherOrderFunctions.kt).
//
//  Q: "What's the difference between a lambda and an anonymous function?"
//  A: Almost nothing:
//       Lambda:  { x: Int -> x * 2 }
//       Anon fn: fun(x: Int): Int { return x * 2 }
//     Main difference: 'return' inside a lambda returns from the
//     ENCLOSING function. In an anonymous function, it returns from itself.
//
//  Q: "Why use lambda with receiver instead of just passing a parameter?"
//  A: Readability. Compare:
//       // Parameter style:
//       configure(sandwich) { s -> s.bread = "Rye"; s.toasted = true }
//
//       // Receiver style:
//       configure(sandwich) { bread = "Rye"; toasted = true }
//
//     The receiver style reads like a configuration block.
//     That's the entire foundation of Kotlin DSLs.
//
//  NEXT STEP: See ScopeFunctions.kt for let, run, apply, also, with.
//  They all use lambdas (some with receivers, some without).
