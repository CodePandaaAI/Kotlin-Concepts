package basics

// ============================================
//  KOTLIN CONCEPT: EXTENSION FUNCTIONS
// ============================================
//
//  Imagine you're using the String class.
//  You wish it had a method called .shout() that returns "HELLO!!!"
//
//  But String is built into Kotlin. You can't edit it.
//  You don't own the source code.
//
//  Extension functions let you ADD methods to any class
//  without modifying the class itself.
//
//  It's like giving a tool to someone without opening their toolbox.

fun main() {

    // ============================================
    //  STEP 1: THE PROBLEM (Normal Functions)
    // ============================================

    val text = "hello"

    // Normal function approach:
    val result1 = shout(text)
    println(result1)  // HELLO!!!

    // This works, but it reads backwards:
    // "shout the text" instead of "text, shout"

    // What if we could write: text.shout()?
    // That reads naturally: "text, shout yourself"



    // ============================================
    //  STEP 2: THE SOLUTION (Extension Function)
    // ============================================

    val result2 = text.shout()
    println(result2)  // HELLO!!!

    // Same result! But notice:
    //   shout(text)    → function-first (less natural)
    //   text.shout()   → object-first (reads like English!)

    // "text, shout yourself" — much cleaner.



    // ============================================
    //  STEP 3: HOW IT WORKS INSIDE
    // ============================================
    //
    //  When you write: text.shout()
    //  Kotlin secretly converts it to: shout(text)
    //
    //  It's SYNTACTIC SUGAR. Same thing, prettier syntax.
    //
    //  Inside the extension function, 'this' refers to the object:
    //    "hello".shout() → this = "hello"
    //    "world".shout() → this = "world"



    // ============================================
    //  STEP 4: MORE EXAMPLES
    // ============================================

    // Add a method to Int:
    println(5.isEven())    // true
    println(7.isEven())    // false

    // Add a method to List:
    val numbers = listOf(1, 2, 3, 4, 5)
    println(numbers.secondOrNull())  // 2

    val short = listOf("only one")
    println(short.secondOrNull())    // null



    // ============================================
    //  STEP 5: NULLABLE EXTENSION FUNCTIONS
    // ============================================
    //
    //  You can even extend nullable types!

    val name: String? = null
    println(name.orDefault("Unknown"))  // Unknown

    val realName: String? = "Alice"
    println(realName.orDefault("Unknown"))  // Alice
}


// ============================================
//  NORMAL FUNCTION (for comparison)
// ============================================
//
//  fun shout(s: String): String
//
//  Takes a String. Returns a String.
//  Called as: shout(text)

fun shout(s: String): String {
    return s.uppercase() + "!!!"
}


// ============================================
//  EXTENSION FUNCTION
// ============================================
//
//  fun String.shout(): String
//      ^^^^^^
//      |
//   "Receiver type" — the class you're extending
//
//  Breakdown:
//    String     → We're adding this to the String class
//    .shout()   → The new method name
//    : String   → It returns a String
//
//  Inside the function:
//    'this' = the String object that called the function
//    "hello".shout() → this = "hello"

fun String.shout(): String {
    // 'this' is the string that called .shout()
    return this.uppercase() + "!!!"
    // Or just: uppercase() + "!!!"
    // (you can omit 'this', just like inside a class)
}


// ============================================
//  MORE EXTENSION FUNCTION EXAMPLES
// ============================================

// Extend Int:
fun Int.isEven(): Boolean {
    return this % 2 == 0
    // 'this' = the Int that called .isEven()
    // 5.isEven() → this = 5 → 5 % 2 == 0 → false
}

// Extend List:
fun <T> List<T>.secondOrNull(): T? {
    return if (this.size >= 2) this[1] else null
    // Generic! Works for List<String>, List<Int>, any List<T>
}

// Extend NULLABLE type:
fun String?.orDefault(default: String): String {
    return this ?: default
    // If 'this' (the string) is null, return the default.
    // If not null, return 'this'.
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Does this actually modify the String class?"
//  A: NO! String's source code is untouched.
//     Extension functions are resolved at COMPILE TIME.
//     The compiler just converts text.shout() → shout(text).
//     It's a trick, not a modification.
//
//  Q: "Can I access private members of the class?"
//  A: NO! Extension functions don't have special access.
//     They can only use public/internal members.
//     They're "outside" the class, just with nicer syntax.
//
//  Q: "What if String already has a method with the same name?"
//  A: The REAL method wins. Always. Extensions can't override.
//
//  Q: "Why not just use normal functions?"
//  A: Readability! Compare:
//       shout(capitalize(trim(text)))           ← nested, hard to read
//       text.trim().capitalize().shout()        ← chained, easy to read
//
//     Extension functions enable FLUENT CHAINS.
//
//  NEXT STEP: See ../functions/HigherOrderFunctions.kt for
//  generic extension functions like filter(), map(), etc.
