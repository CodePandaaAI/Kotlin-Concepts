package functions

// ============================================
//  KOTLIN CONCEPT: HIGHER-ORDER FUNCTIONS
// ============================================
//
//  A "higher-order function" is just a function that does
//  ONE of these things:
//    1. Takes a function as a parameter
//    2. Returns a function
//
//  You've already used them!
//    list.filter { it > 3 }     ← filter takes a lambda
//    list.map { it * 2 }        ← map takes a lambda
//    list.forEach { println(it) } ← forEach takes a lambda
//
//  In this file, we'll BUILD these functions from scratch
//  so you see there's no magic inside. They're simple loops.
//
//  PREREQUISITE: Read Lambdas.kt first!

fun main() {

    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8)

    // ============================================
    //  STEP 1: BUILD OUR OWN filter()
    // ============================================
    //
    //  filter() takes each item and asks:
    //    "Should I keep this?" → lambda returns true/false
    //
    //  That's it. It's a loop with an if-statement inside.

    val evens = numbers.myFilter { it % 2 == 0 }
    println("Evens: $evens")  // [2, 4, 6, 8]

    // How it works inside:
    //   for each item in [1, 2, 3, 4, 5, 6, 7, 8]:
    //     1 % 2 == 0? → false → skip
    //     2 % 2 == 0? → true  → keep ✅
    //     3 % 2 == 0? → false → skip
    //     4 % 2 == 0? → true  → keep ✅
    //     ... and so on



    // ============================================
    //  STEP 2: BUILD OUR OWN map()
    // ============================================
    //
    //  map() takes each item and TRANSFORMS it:
    //    "Turn this into something else" → lambda returns the new value
    //
    //  Input type and output type can be DIFFERENT.

    val doubled = numbers.myMap { it * 2 }
    println("Doubled: $doubled")  // [2, 4, 6, 8, 10, 12, 14, 16]

    // Changing type: Int → String
    val labels = numbers.myMap { "Number $it" }
    println("Labels: $labels")  // [Number 1, Number 2, ...]

    // How it works inside:
    //   for each item in [1, 2, 3]:
    //     1 → "Number 1"
    //     2 → "Number 2"
    //     3 → "Number 3"



    // ============================================
    //  STEP 3: BUILD OUR OWN forEach()
    // ============================================
    //
    //  forEach() runs a block on each item.
    //  Returns nothing. Just side effects (printing, logging, etc.)

    print("Items: ")
    numbers.myForEach { print("$it ") }
    println()  // newline



    // ============================================
    //  STEP 4: REIFIED TYPE PARAMETERS
    // ============================================
    //
    //  This is an advanced concept. Skip if you're starting out.
    //
    //  PROBLEM:
    //    fun <T> isType(item: Any): Boolean {
    //        return item is T  // ❌ ERROR! T is erased at runtime!
    //    }
    //
    //  WHY? Java/Kotlin erase generic types at runtime.
    //  At runtime, List<String> is just List. The String part is GONE.
    //
    //  SOLUTION: 'inline' + 'reified'
    //    inline fun <reified T> isType(item: Any): Boolean {
    //        return item is T  // ✅ WORKS! T is preserved!
    //    }
    //
    //  HOW? 'inline' copies the function code to the call site.
    //  At the call site, the actual type IS known.
    //  So 'reified' replaces T with the real type during copy.

    checkType<String>("hello")  // ✅ Match! 'hello' IS a String
    checkType<Int>("hello")     // ❌ No match. 'hello' is String, not Int
    checkType<Int>(42)          // ✅ Match! 42 IS an Int
}


// ============================================
//  OUR OWN filter()
// ============================================
//
//  Let's break down the signature:
//
//  inline fun <T> List<T>.myFilter(predicate: (T) -> Boolean): List<T>
//  ^^^^^^     ^^^  ^^^^^^^          ^^^^^^^^^^^^^^^^^^^^^^^^^   ^^^^^^^
//  |          |    |                |                           |
//  Performance Generic Extension fn Lambda parameter           Return type
//
//  'inline': Copy this function into the call site.
//            No function call overhead. No lambda object created.
//            Just a direct loop at the call site. Fast!
//
//  '<T>': Works for ANY list type.
//         List<Int>, List<String>, List<User> — all work.
//
//  'List<T>.': Extension function on List.
//              Called as: myList.myFilter { ... }
//
//  'predicate: (T) -> Boolean': A function that takes one item (T)
//                                and returns true (keep) or false (skip).
//
//  Inside, it's just a loop:

inline fun <T> List<T>.myFilter(predicate: (T) -> Boolean): List<T> {
    val result = mutableListOf<T>()
    for (item in this) {         // 'this' = the list
        if (predicate(item)) {   // Ask the lambda: keep this item?
            result.add(item)     // Yes → add to result
        }
    }
    return result
}

// That's it. filter() is a loop with an if inside.
// The "magic" is that YOU provide the if-condition via the lambda.


// ============================================
//  OUR OWN map()
// ============================================
//
//  Two type parameters this time:
//    T = input type (what the list has now)
//    R = result type (what each item becomes)
//
//  List<Int>.myMap { "Number $it" }
//    T = Int, R = String
//    Each Int → String

inline fun <T, R> Iterable<T>.myMap(transform: (T) -> R): List<R> {
    val result = mutableListOf<R>()
    for (item in this) {
        result.add(transform(item))  // Transform each item
    }
    return result
}

// It's a loop where each item gets REPLACED by the lambda's result.


// ============================================
//  OUR OWN forEach()
// ============================================
//
//  Simplest one. Just calls the lambda on each item.
//  Returns Unit (nothing).

inline fun <T> Iterable<T>.myForEach(action: (T) -> Unit) {
    for (item in this) {
        action(item)  // Do something with each item
    }
}


// ============================================
//  REIFIED TYPE CHECK
// ============================================

inline fun <reified T> checkType(item: Any) {
    if (item is T) {
        println("✅ Match! '$item' IS a ${T::class.simpleName}")
    } else {
        println("❌ No match. '$item' is ${item::class.simpleName}, not ${T::class.simpleName}")
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Why 'inline'? What happens without it?"
//  A: Without inline, every lambda creates an OBJECT at runtime.
//       list.filter { it > 3 }
//     This creates a Function1 object, calls it in a loop, then throws it away.
//     With inline, the lambda code is COPIED directly into the calling code.
//     No object creation. No function call overhead. Just a plain loop.
//
//  Q: "When should I use inline?"
//  A: ✅ Use when: function takes lambdas, function is small, need reified
//     ❌ Don't use when: function body is large, function is recursive
//
//  Q: "Why does myMap have TWO type parameters <T, R>?"
//  A: Because the INPUT type and OUTPUT type can differ:
//       listOf(1, 2, 3).myMap { "Num: $it" }
//       T = Int (input), R = String (output)
//     If we only had <T>, the output would have to be the same type.
//
//  Q: "Is filter() REALLY just a loop with an if?"
//  A: Yes. Look at the Kotlin source code — it's almost identical
//     to our myFilter. There's no compiler magic. Just a loop.
//
//  NEXT STEP: See ../oop/AnonymousObjects.kt or ../oop/SealedClasses.kt
