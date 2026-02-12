package generics

// ============================================
//  KOTLIN CONCEPT: GENERIC VARIANCE
// ============================================
//
//  This is the hardest concept in Kotlin generics.
//  We're going to build it up SLOWLY from the problem.
//
//  No jumping ahead. No shortcuts.
//  By the end, you'll understand out, in, Nothing, and Result<out T>.
//
//  PREREQUISITE: Read basics/BasicGenerics.kt first.
//
//  ROADMAP:
//    Part 1: The Problem (Invariance)
//    Part 2: The Solution for Output (Covariance / out)
//    Part 3: The Solution for Input (Contravariance / in)
//    Part 4: Nothing — the universal subtype
//    Part 5: Everything Together — Result<out T>

fun main() {
    part1_invariance()
    part2_covariance()
    part3_contravariance()
    part4_nothing()
    part5_resultPattern()
}



// ============================================
//  PART 1: THE PROBLEM (Invariance)
// ============================================
//
//  String is a subtype of Any. Everyone knows this:
//    val x: Any = "hello"  ✅ Works! String IS Any.
//
//  So naturally you'd think:
//    "If String is a subtype of Any...
//     then MutableList<String> is a subtype of MutableList<Any>?"
//
//  NOPE. Kotlin says NO. Let's see why.

fun part1_invariance() {
    println("=== Part 1: Invariance ===")

    // This is FINE:
    val s: Any = "hello"  // String → Any ✅

    // But this is BLOCKED:
    val strings: MutableList<String> = mutableListOf("A", "B")
    // val anything: MutableList<Any> = strings  // ❌ COMPILE ERROR!

    // WHY? Because it would break type safety:
    //
    //   val strings: MutableList<String> = mutableListOf("A", "B")
    //   val anything: MutableList<Any> = strings   // ❌ If this were allowed...
    //   anything.add(123)                           // ...we could add an Int!
    //   val s: String = strings[0]                  // Fine: "A"
    //   val s: String = strings[2]                  // 💥 CRASH! It's 123, not String!
    //
    //   Both 'strings' and 'anything' point to the SAME list.
    //   If we add 123 through 'anything', it appears in 'strings' too.
    //   But 'strings' promises everything inside is a String.
    //   123 is NOT a String. BOOM.
    //
    //   So Kotlin blocks it. This is called INVARIANCE.
    //   MutableList<String> has NO relationship with MutableList<Any>.
    //   Even though String IS Any, the containers are NOT related.

    println("MutableList<String> and MutableList<Any> = NO relationship (invariant)")
    println("This is the DEFAULT. Safe but restrictive.\n")
}



// ============================================
//  PART 2: COVARIANCE (out T)
// ============================================
//
//  "Okay, invariance protects us. But what if I have a READ-ONLY container?"
//
//  If I can ONLY read from the container, the danger disappears:
//    - I can't add(123) because there's no add() method.
//    - Reading a String as Any? Always safe. String IS Any.
//
//  So Kotlin lets you opt in:
//    "I promise T only comes OUT (return types, val properties).
//     In exchange, allow Child→Parent subtyping."
//
//  The keyword is 'out'.

fun part2_covariance() {
    println("=== Part 2: Covariance (out) ===")

    // List<T> in Kotlin uses 'out T' — it's read-only!
    val strings: List<String> = listOf("Hello", "World")
    val objects: List<Any> = strings  // ✅ WORKS!

    // This is safe because:
    //   objects[0]  → returns "Hello" as Any. Fine!
    //   objects.add(123)  → DOESN'T EXIST! List is read-only.
    //   No danger.

    println("List<String> → List<Any> = WORKS (List uses 'out T')")

    // Custom covariant class:
    val burgerBox: ReadOnlyBox<Burger> = ReadOnlyBox(Burger())
    val foodBox: ReadOnlyBox<Food> = burgerBox  // ✅ Burger is Food, so this works!
    println("ReadOnlyBox<Burger> → ReadOnlyBox<Food> = WORKS")
    println("Got from foodBox: ${foodBox.get()}")

    // BUT WAIT — what does 'out' actually DO?
    //
    //   class ReadOnlyBox<out T>(private val item: T) {
    //       fun get(): T = item        ← T in OUTPUT position ✅
    //       // fun put(newItem: T) { }  ← T in INPUT position ❌ BLOCKED!
    //   }
    //
    //   'out' is not protection. It's not a wall.
    //   'out' is your PROOF to the compiler:
    //     "I promise I'll never take T as input.
    //      So it's safe to allow ReadOnlyBox<Burger> as ReadOnlyBox<Food>."
    //
    //   The compiler CHECKS your promise.
    //   If you try to add a method with T as input, it REFUSES.

    println()
}



// ============================================
//  PART 3: CONTRAVARIANCE (in T)
// ============================================
//
//  'out' handles the case where T comes OUT (reading).
//  'in' handles the OPPOSITE: T goes IN (consuming/writing).
//
//  The subtyping goes the OTHER DIRECTION:
//    If String is a subtype of Any...
//    Then Consumer<Any> is a subtype of Consumer<String>.
//
//  Reverse! Parent container → Child container.
//
//  WHY? A consumer that handles ANY type can definitely handle Strings.
//  It's like hiring: if someone can cook ANY food, they can cook burgers.

fun part3_contravariance() {
    println("=== Part 3: Contravariance (in) ===")

    // A consumer that handles any Number:
    val numberConsumer = object : MyConsumer<Number> {
        override fun consume(item: Number) {
            println("  Consuming number: $item (${item::class.simpleName})")
        }
    }

    // Assign to a more SPECIFIC type:
    val doubleConsumer: MyConsumer<Double> = numberConsumer  // ✅ WORKS!
    doubleConsumer.consume(3.14)

    // WHY IS THIS SAFE?
    //
    //   numberConsumer.consume() accepts any Number.
    //   Double IS a Number.
    //   So doubleConsumer.consume(3.14) calls numberConsumer.consume(3.14).
    //   numberConsumer can handle it because Double IS a Number.
    //
    //   The reverse would NOT be safe:
    //   val numberConsumer: MyConsumer<Number> = doubleConsumer  // ❌
    //   numberConsumer.consume(42)  // Int! But doubleConsumer expects Double!

    // SUMMARY:
    //   'out' = T comes OUT → subtyping goes FORWARD (Child→Parent)
    //   'in'  = T goes IN   → subtyping goes BACKWARD (Parent→Child)

    println("MyConsumer<Number> → MyConsumer<Double> = WORKS (uses 'in T')")
    println()
}



// ============================================
//  PART 4: THE 'Nothing' TYPE
// ============================================
//
//  Nothing is a type in Kotlin that:
//    1. Is a subtype of EVERY type
//    2. Has NO instances — you can never create a Nothing object
//    3. Represents a computation that NEVER completes
//
//  "But why would I want a type with no instances?"
//
//  Because combined with 'out', it creates a UNIVERSAL FIT.

fun part4_nothing() {
    println("=== Part 4: Nothing ===")

    // Nothing is a subtype of everything:
    //
    //            Any
    //           / | \
    //     String  Int  User
    //           \ | /
    //          Nothing
    //
    //   Nothing <: String  ✅
    //   Nothing <: Int     ✅
    //   Nothing <: User    ✅
    //   Nothing <: Any     ✅

    // Example 1: emptyList()
    //   emptyList() returns List<Nothing>.
    //   Because of 'out': List<Nothing> <: List<String> (Nothing <: String)
    //   So it fits into ANY List<T>:

    val strings: List<String> = emptyList()  // ✅ List<Nothing> → List<String>
    val ints: List<Int> = emptyList()         // ✅ List<Nothing> → List<Int>
    println("emptyList() fits into any List<T> because it's List<Nothing>")

    // Example 2: throw returns Nothing
    //   fun fail(): Nothing = throw Exception("boom")
    //   The function NEVER returns a value. It always crashes.
    //   Nothing = "I don't produce any value. Ever."

    println("Nothing = subtype of everything, instance of nothing")
    println()
}



// ============================================
//  PART 5: EVERYTHING TOGETHER — Result<out T>
// ============================================
//
//  Now the big payoff. All 4 concepts combine:
//
//  sealed interface NetworkResult<out T>
//  ^^^^^^           ^^^^^^^^^^^^^^^
//  |                |
//  "Closed set"     "T only comes OUT (covariant)"
//
//  Success<T>(val data: T)  → has data of type T
//  Error(val message: String) : NetworkResult<Nothing>  → has NO data of type T

fun part5_resultPattern() {
    println("=== Part 5: Result<out T> ===")

    val success = fetchUser(1)
    val failure = fetchUser(999)

    when (success) {
        is NetworkResult.Success -> println("Got: ${success.data}")
        is NetworkResult.Error -> println("Error: ${success.message}")
    }

    when (failure) {
        is NetworkResult.Success -> println("Got: ${failure.data}")
        is NetworkResult.Error -> println("Error: ${failure.message}")
    }

    // THE MAGIC OF Nothing + 'out':
    //
    //   val error = NetworkResult.Error("Server down")
    //
    //   error is NetworkResult<Nothing>.
    //   Because of 'out': NetworkResult<Nothing> <: NetworkResult<String>
    //   Because of 'out': NetworkResult<Nothing> <: NetworkResult<Int>
    //   Because of 'out': NetworkResult<Nothing> <: NetworkResult<ANYTHING>
    //
    //   ONE Error type fits into ANY NetworkResult<T>.
    //   You don't need Error<String>, Error<Int>, Error<User>.
    //   Just Error. It fits everywhere.

    val error = NetworkResult.Error("Server down")
    val asString: NetworkResult<String> = error  // ✅
    val asInt: NetworkResult<Int> = error         // ✅
    val asAny: NetworkResult<Any> = error         // ✅

    println("\nOne Error fits into ANY NetworkResult<T>!")
    println("This works because: Nothing <: T, and 'out' allows covariant subtyping")
}

fun fetchUser(id: Int): NetworkResult<String> {
    return if (id == 1) {
        NetworkResult.Success("User: Alice")
    } else {
        NetworkResult.Error("User not found")
        // Error is NetworkResult<Nothing>
        // Function wants NetworkResult<String>
        // Nothing <: String → NetworkResult<Nothing> <: NetworkResult<String> ✅
    }
}



// ============================================
//  SUPPORTING TYPES
// ============================================

// -- Part 2: Covariance --
open class Food { override fun toString() = "Food" }
class Burger : Food() { override fun toString() = "🍔 Burger" }

class ReadOnlyBox<out T>(private val item: T) {
    fun get(): T = item
    // fun put(newItem: T) { }  // ❌ 'out' forbids T in input position
}

// -- Part 3: Contravariance --
interface MyConsumer<in T> {
    fun consume(item: T)
    // fun produce(): T  // ❌ 'in' forbids T in output position
}

// -- Part 5: Result pattern --
sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val message: String) : NetworkResult<Nothing>
}



// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//
//  Q: "Success(val data: T) — T goes IN via the constructor.
//      Doesn't 'out' mean T can ONLY come OUT?"
//
//  A: Constructors are a SPECIAL EXCEPTION.
//     The constructor creates the object. It's a one-time setup.
//     After creation, 'data' is a 'val' (read-only).
//     So T only comes OUT after the object exists.
//     Kotlin gives constructors a pass because it's safe.
//
//
//  Q: "Why doesn't Error have <T>? Why use Nothing?"
//
//  A: Two approaches:
//
//     Approach 1: Error<T>(val message: String) : Result<T>
//       Then: Error<String>("err") is Result<String>
//             Error<Int>("err") is Result<Int>
//       These are DIFFERENT types! But they're identical in every way.
//       Redundant, confusing, and you'd need to specify T every time.
//
//     Approach 2: Error(val message: String) : Result<Nothing>
//       Then: Error("err") is Result<Nothing>
//       And: Result<Nothing> fits into Result<String>, Result<Int>, anything!
//       ONE type. Works everywhere. Clean.
//
//     Nothing is not a hack. It's honest:
//       "Error has NO data of type T. I have Nothing."
//
//
//  Q: "'out' means only read/output. But what about mutable collections?
//      I need to add AND read. What do I do?"
//
//  A: Don't use 'out' or 'in'. Stay invariant.
//     That's the default, and it's safe for mutable containers.
//     MutableList uses invariant T for exactly this reason.
//
//     'out' and 'in' are for specialized cases:
//       out = read-only producers (List, Result, Flow, LiveData)
//       in  = write-only consumers (Comparator, event handlers)
//
//
//  Q: "I understand Box<out T> prevents adding. But in a sealed interface,
//      there's no add() method anyway. So what's the point of 'out'?"
//
//  A: Great question. In a sealed interface, 'out' isn't about blocking methods.
//     It's about ENABLING subtyping.
//
//     Without 'out':
//       NetworkResult<Nothing> has NO relationship to NetworkResult<String>.
//       Error can't fit into a function returning Result<String>. ❌
//
//     With 'out':
//       NetworkResult<Nothing> IS-A NetworkResult<String>.
//       Error fits into ANY Result<T>. ✅
//
//     'out' is NOT a security guard. It's a PERMISSION SLIP.
//     You're telling the compiler: "I promise T only comes out.
//     In exchange, give me covariant subtyping."
//
//
//  SUMMARY TABLE:
//
//  | Keyword | Name          | T position | Subtyping          | Example         |
//  |---------|---------------|------------|--------------------| --------------- |
//  | (none)  | Invariant     | In + Out   | No relationship    | MutableList<T>  |
//  | out     | Covariant     | Out only   | Child → Parent     | List<out T>     |
//  | in      | Contravariant | In only    | Parent → Child     | Comparator<in T>|
//
//
//  NEXT STEP: See ../patterns/ResultPattern.kt for the full Result pattern
//  with map(), chaining, and real use cases.
