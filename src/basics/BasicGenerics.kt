package basics

// ============================================
//  KOTLIN CONCEPT: GENERICS
// ============================================
//
//  Before we start: imagine you're building a box.
//  A box that holds ONE thing.
//
//  You could make a StringBox, an IntBox, a UserBox...
//  Or you could make ONE box that works with ANY type.
//
//  That's generics.

fun main() {

    // ============================================
    //  STEP 1: THE PROBLEM (No Generics)
    // ============================================
    //
    //  Without generics, you'd have to make separate classes:
    //
    //  class StringBox(val item: String)
    //  class IntBox(val item: Int)
    //  class UserBox(val item: User)
    //
    //  That's 3 classes that do the SAME THING.
    //  What if you need 50 types? 50 classes? No way.



    // ============================================
    //  STEP 2: THE SOLUTION (Generics!)
    // ============================================
    //
    //  Instead of hardcoding the type, we use a PLACEHOLDER:
    //
    //  class Box<T>(val item: T)
    //         ^^^
    //         |
    //    "T" = Type parameter
    //    "I don't know the type yet. You tell me when you use me."

    val stringBox = Box("Hello")   // T becomes String
    val intBox = Box(42)           // T becomes Int
    val doubleBox = Box(3.14)      // T becomes Double

    // Kotlin figured out T automatically! (Type inference)
    // You COULD also write it explicitly:
    val explicitBox = Box<String>("World")

    println("String box: ${stringBox.item}")   // Hello
    println("Int box: ${intBox.item}")          // 42
    println("Double box: ${doubleBox.item}")    // 3.14



    // ============================================
    //  STEP 3: TYPE SAFETY (The Real Win)
    // ============================================
    //
    //  "Wait, I could just use Any to hold anything. Why generics?"
    //
    //  Because generics give you TYPE SAFETY:

    val safeBox = Box("Hello")
    val value: String = safeBox.item  // ✅ Compiler KNOWS this is a String

    // With Any, you'd have to cast:
    // val unsafeBox = UnsafeBox("Hello")
    // val value = unsafeBox.item as String  // 😬 What if it's not a String? CRASH!



    // ============================================
    //  STEP 4: MULTIPLE TYPE PARAMETERS
    // ============================================
    //
    //  You can have more than one type parameter:

    val pair1 = Pair2("Alice", 30)         // A=String, B=Int
    val pair2 = Pair2(true, 3.14)          // A=Boolean, B=Double
    val pair3 = Pair2("Key", listOf(1,2))  // A=String, B=List<Int>

    println("\nPair: ${pair1.first}, ${pair1.second}")  // Alice, 30

    // The compiler enforces types:
    val name: String = pair1.first   // ✅ Safe — compiler knows it's String
    val age: Int = pair1.second      // ✅ Safe — compiler knows it's Int
    // val wrong: Int = pair1.first  // ❌ COMPILE ERROR: first is String, not Int



    // ============================================
    //  STEP 5: GENERIC FUNCTIONS
    // ============================================
    //
    //  Functions can be generic too!

    val first = firstItem(listOf("A", "B", "C"))
    println("\nFirst item: $first")  // A

    val firstNum = firstItem(listOf(10, 20, 30))
    println("First number: $firstNum")  // 10

    // Same function, works with ANY list type.
    // No duplication!
}

// ============================================
//  THE GENERIC BOX CLASS
// ============================================
//
//  class Box<T>(val item: T)
//
//  Breakdown:
//
//  Box       → The class name
//  <T>       → "I accept ONE type parameter"
//  val item: T → "item will be whatever type T is"
//
//  When you write Box("Hello"):
//    T = String
//    val item: String = "Hello"
//
//  When you write Box(42):
//    T = Int
//    val item: Int = 42
//
//  ONE class. Works for ALL types. Type-safe.

class Box<T>(val item: T)


// ============================================
//  GENERIC PAIR CLASS (Two type parameters)
// ============================================
//
//  class Pair2<A, B>
//              ^  ^
//              |  |
//    First type   Second type
//
//  A and B are independent — they can be the same or different types.

class Pair2<A, B>(
    val first: A,
    val second: B
)


// ============================================
//  GENERIC FUNCTION
// ============================================
//
//  fun <T> firstItem(list: List<T>): T
//       ^                        ^    ^
//       |                        |    |
//   Declare T              List of T   Returns T
//
//  The <T> before the function name DECLARES the type parameter.
//  Then you can use T in parameters and return type.

fun <T> firstItem(list: List<T>): T {
    return list.first()
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "What's the difference between T and Any?"
//  A: Any = "I accept anything, and I forget what it was."
//     T   = "I accept anything, and I REMEMBER what it was."
//
//     Box<String>.item → String (remembered!)
//     AnyBox.item      → Any    (forgotten! need unsafe cast)
//
//  Q: "Can I name T something else?"
//  A: Yes! T is just convention. You could write <Potato>.
//     Common names:
//       T = Type
//       E = Element (for collections)
//       K = Key, V = Value (for maps)
//       R = Return type
//
//  Q: "Can a generic class have methods?"
//  A: Absolutely:
//     class Box<T>(val item: T) {
//         fun getItemType(): String = item!!::class.simpleName ?: "Unknown"
//     }
//
//  NEXT STEP: See ../generics/Variance.kt for the ADVANCED side —
//  what happens when Box<String> meets Box<Any>.
