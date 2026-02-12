package functions

// ============================================
//  KOTLIN CONCEPT: SCOPE FUNCTIONS
// ============================================
//
//  Kotlin has 5 "scope functions".
//  They all do the same basic thing:
//    "Run a block of code on an object."
//
//  The difference is:
//    - How you refer to the object (this vs it)
//    - What gets returned (the object vs the result)
//
//  If this sounds confusing, don't worry.
//  We'll go one by one with examples.
//  By the end, you'll know exactly when to use each.

data class User(var name: String, var age: Int, var email: String = "")

fun main() {

    // ============================================
    //  1. LET — "I have something, do something with it"
    // ============================================
    //
    //  Object: referred to as 'it'
    //  Returns: whatever the lambda returns
    //
    //  BEST FOR: null checks, transforming values

    // Without let (old-school null check):
    val name: String? = "Alice"
    if (name != null) {
        println("Name length: ${name.length}")
    }

    // With let (cleaner):
    name?.let {
        println("Name length: ${it.length}")
    }
    // If name is null, the ENTIRE block is skipped.
    // No if-else needed!

    // Let for transforming:
    val uppercased = "hello".let { it.uppercase() }
    println(uppercased)  // HELLO

    // 'let' returns the RESULT of the lambda, not the object.



    // ============================================
    //  2. APPLY — "Configure this object and give it back"
    // ============================================
    //
    //  Object: referred to as 'this'
    //  Returns: THE OBJECT ITSELF
    //
    //  BEST FOR: setting up / configuring objects

    val user = User("Bob", 25).apply {
        // Inside here, 'this' = the User
        // So you can access properties directly:
        email = "bob@example.com"
        age = 26  // Birthday!
    }
    // 'apply' returns the SAME User object (now configured)
    println(user)  // User(name=Bob, age=26, email=bob@example.com)

    // WHY 'this' AND NOT 'it'?
    //
    // With 'this', you write:
    //   email = "bob@example.com"     ← clean
    //
    // With 'it', you'd write:
    //   it.email = "bob@example.com"  ← more verbose
    //
    // 'apply' is designed for CONFIGURATION, so 'this' is cleaner.



    // ============================================
    //  3. ALSO — "Do something extra, don't change the chain"
    // ============================================
    //
    //  Object: referred to as 'it'
    //  Returns: THE OBJECT ITSELF
    //
    //  BEST FOR: side effects (logging, debugging)
    //
    //  "Also do this thing, but keep passing the object through."

    val numbers = mutableListOf(1, 2, 3)
        .also { println("Before: $it") }  // Log: Before: [1, 2, 3]
        .also { it.add(4) }               // Modify
        .also { println("After: $it") }   // Log: After: [1, 2, 3, 4]

    // Each .also returns the SAME list, so you can chain.
    // It's like putting print statements in the middle of a chain.



    // ============================================
    //  4. RUN — "Be the object, compute something"
    // ============================================
    //
    //  Object: referred to as 'this'
    //  Returns: whatever the lambda returns
    //
    //  BEST FOR: computing a result from an object

    val greeting = "Hello".run {
        // 'this' = "Hello"
        // You can call String methods directly:
        "$this World! Length: $length"
    }
    println(greeting)  // Hello World! Length: 5

    // 'run' is like 'apply' but returns the RESULT, not the object.

    // Also works for null-safe operations:
    val nullableUser: User? = User("Eve", 30)
    val info = nullableUser?.run {
        "User: $name, Age: $age"  // 'this' = the User
    }
    println(info)  // User: Eve, Age: 30



    // ============================================
    //  5. WITH — "Group operations on an object"
    // ============================================
    //
    //  Object: referred to as 'this'
    //  Returns: whatever the lambda returns
    //
    //  BEST FOR: grouping multiple calls on one object
    //
    //  NOTE: 'with' is NOT an extension function!
    //  You write: with(object) { ... }
    //  Not:       object.with { ... }

    val dave = User("Dave", 28, "dave@example.com")

    val description = with(dave) {
        // No need to write dave.name, dave.age, etc.
        // 'this' = dave
        "Name: $name, Age: $age, Email: $email"
    }
    println(description)

    // 'with' is basically the same as 'run' but with different syntax.
    // dave.run { ... } === with(dave) { ... }
}


// ============================================
//  THE CHEAT SHEET
// ============================================
//
//  Ask TWO questions:
//
//  Q1: Do I want the OBJECT back or a COMPUTED RESULT?
//
//      Object back → apply, also
//      Result      → let, run, with
//
//  Q2: Do I want 'this' (I AM the object) or 'it' (I HAVE the object)?
//
//      this → apply, run, with
//      it   → let, also
//
//  DECISION MATRIX:
//
//      ┌────────────┬──────────────┬──────────────┐
//      │            │ Returns      │ Returns      │
//      │            │ OBJECT       │ RESULT       │
//      ├────────────┼──────────────┼──────────────┤
//      │ Uses 'this'│ apply        │ run / with   │
//      ├────────────┼──────────────┼──────────────┤
//      │ Uses 'it'  │ also         │ let          │
//      └────────────┴──────────────┴──────────────┘
//
//
//  SIMPLE RULES:
//
//  • Null check?          → .let { }
//  • Configure an object? → .apply { }
//  • Log / debug?         → .also { }
//  • Compute from object? → .run { } or with(obj) { }


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "But apply and also BOTH return the object. What's the difference?"
//  A: How you refer to the object inside:
//       apply { this.name = "X" }   ← 'this' (you ARE the object)
//       also  { it.name = "X" }     ← 'it' (you HAVE the object)
//     Use 'apply' for configuration (shorter syntax).
//     Use 'also' for side effects like logging (clearer that it's temporary).
//
//  Q: "Run and with seem identical. Why both?"
//  A: Syntax difference only:
//       dave.run { "$name is $age" }     ← extension function, null-safe
//       with(dave) { "$name is $age" }   ← regular function, NOT null-safe
//     Prefer 'run' when dealing with nullable objects.
//
//  Q: "Do I NEED to use scope functions?"
//  A: No! They're convenience functions.
//     Code works fine without them.
//     They make code more readable and idiomatic Kotlin.
//     Don't force them — use them when they make code CLEARER.
//
//  NEXT STEP: See HigherOrderFunctions.kt for how these patterns
//  connect to filter(), map(), and other higher-order functions.
