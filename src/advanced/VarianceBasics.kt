package advanced

/**
 * ==========================================
 * PART 1: THE FOUNDATION (Types & Any)
 * ==========================================
 */

fun main() {
    // 1. WHAT IS 'Any'?
    // 'Any' is the parent of everything in Kotlin.
    // String is-a Any. Int is-a Any. User is-a Any.
    val str: String = "Hello"
    val any: Any = str // ✅ OK! String is a subtype of Any.

    // 2. GENERIC TYPES: List vs List<String>
    // 'List' is a Class.
    // 'List<String>' is a Type.
    // 'List<Int>' is a DIFFERENT Type.
    // They are created from the same Class (List), but they are distinct Types.

    // 3. THE BIG QUESTION: RELATIONSHIPS
    // We know: String is a subtype of Any.
    // Question: Is List<String> a subtype of List<Any>?

    val strings: MutableList<String> = mutableListOf("A", "B")
    
    // uncommenting the line below causes a COMPILER ERROR
    // val objects: MutableList<Any> = strings 
    // ^ Type mismatch: inferred type is MutableList<String> but MutableList<Any> was expected

    // WHY? (The "Invariance" Problem)
    // If Kotlin allowed this:
    // objects.add(123) // Valid! It's a list of Any.
    // val s: String = strings[2] // CRASH! It's actually an Int!
    
    // SO: MutableList<String> is NOT related to MutableList<Any>.
    // This is called "INVARIANCE".
    
    println("Part 1: Invariance protects us from adding wrong types.")

    demonstrateCovariance()
    demonstrateContravariance()
}

/**
 * ==========================================
 * PART 2: COVARIANCE ('out T')
 * ==========================================
 * 
 * Goal: make List<String> a subtype of List<Any> safely.
 * Solution: Promise to only READ items (produce them).
 */
fun demonstrateCovariance() {
    // List<out T> in Kotlin is Covariant.
    // It means: "I promise I only return T, I never take T as input."
    
    val strings: List<String> = listOf("A", "B")
    val objects: List<Any> = strings // ✅ OK! 
    
    // Why is this safe now?
    // objects.add(123) // IMPOSSIBLE! List<out T> does not have .add()
    
    val item: Any = objects[0] // Safe to read as Any.
    println("Part 2: Covariance (out) lets us treat List<String> as List<Any> because it's read-only.")
}

/**
 * ==========================================
 * PART 3: CONTRAVARIANCE ('in T')
 * ==========================================
 * 
 * Goal: The opposite. Treat a generic object as a specific one.
 * Example: A Comparator<Number> can compare Doubles.
 */
// 1. DEFINE A CONTRAVARIANT INTERFACE ('in T')
// It means: "I only CONSUME T. I never return it."
// Equivalent to a "Trash Can" or "Listener".
interface MyConsumer<in T> {
    fun consume(item: T)
}

fun demonstrateContravariance() {
    // 2. CREATE A CONSUMER FOR 'Number'
    val numberConsumer = object : MyConsumer<Number> {
        override fun consume(item: Number) {
            println("Consuming number: $item")
        }
    }

    // 3. ASSIGN IT TO A VARIABLE EXPECTING 'Double'
    // Logic: A consumer that handles ANY Number can definitely handle a specific Double.
    val doubleConsumer: MyConsumer<Double> = numberConsumer // ✅ OK!
    
    // Usage:
    doubleConsumer.consume(3.14) // Works perfectly!
    
    // Why is this safe?
    // We can only pass Doubles into doubleConsumer.
    // The underlying object (numberConsumer) expects Numbers.
    // Is a Double a Number? YES. Safe.
    
    println("Part 3: Contravariance (in) lets us use a general Consumer (for Number) where a specific Consumer (for Double) is expected.")
}
