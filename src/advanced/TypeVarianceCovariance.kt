package advanced

/**
 * ============================================
 * KOTLIN CONCEPT: TYPE VARIANCE - COVARIANCE
 * ============================================
 * 
 * WHAT IS VARIANCE?
 * -----------------
 * Variance describes how subtyping relationships between generic types
 * relate to subtyping relationships between their type arguments.
 * 
 * TYPES OF VARIANCE:
 * ------------------
 * 1. INVARIANT: No subtyping relationship
 *    - Container<Dog> is NOT a subtype of Container<Animal>
 * 
 * 2. COVARIANT (out): Preserves subtyping relationship
 *    - ReadOnlyContainer<Dog> IS a subtype of ReadOnlyContainer<Animal>
 *    - Use 'out' keyword
 *    - Type parameter can only appear in OUTPUT positions (return types)
 * 
 * 3. CONTRAVARIANT (in): Reverses subtyping relationship
 *    - WriteOnlyContainer<Animal> IS a subtype of WriteOnlyContainer<Dog>
 *    - Use 'in' keyword
 *    - Type parameter can only appear in INPUT positions (parameters)
 * 
 * THIS FILE FOCUSES ON: COVARIANCE (out)
 */

fun main() {
    // ============================================
    // EXAMPLE: Covariance with 'out' keyword
    // ============================================
    
    // Create a container that holds a Burger
    val burgerContainer: ReadOnlyContainer<Burger> = ReadOnlyContainer(
        mutableListOf(Burger())
    )
    
    // ============================================
    // THE MAGIC: Covariance allows this assignment!
    // ============================================
    // Because Burger is a subtype of Food, and we used 'out T',
    // ReadOnlyContainer<Burger> is also a subtype of ReadOnlyContainer<Food>
    
    // This works! Because of 'out' keyword (covariance)
    val foodContainer: ReadOnlyContainer<Food> = burgerContainer
    
    // Now we can safely get a Food item from the container
    // We know it's safe because Burger IS-A Food
    val item: Food = foodContainer.getItem()
    println("Got item: $item")
    
    // ============================================
    // WHY THIS IS SAFE
    // ============================================
    // 1. Burger extends Food (Burger IS-A Food)
    // 2. Container uses 'out T' (covariant)
    // 3. Therefore: Container<Burger> IS-A Container<Food>
    // 4. We can only READ from the container (no writing)
    // 5. Reading a Burger as Food is always safe
}

/**
 * COVARIANT GENERIC CLASS
 * -----------------------
 * 
 * class ReadOnlyContainer<out T>
 *                    ^
 *                    |
 *              'out' keyword makes it covariant
 * 
 * WHAT DOES 'out' MEAN?
 * ---------------------
 * - 'out T' means T can only appear in OUTPUT positions
 * - OUTPUT positions: return types, property getters
 * - INPUT positions: function parameters, property setters
 * 
 * WHY THE RESTRICTION?
 * --------------------
 * If we allowed T in input positions, we could do unsafe things:
 * 
 *   val burgerContainer = ReadOnlyContainer<Burger>(...)
 *   val foodContainer: ReadOnlyContainer<Food> = burgerContainer
 *   foodContainer.addItem(Salad())  // ❌ ERROR! Can't add Salad to Burger container
 * 
 * By restricting T to output positions only, we ensure type safety.
 * 
 * REAL-WORLD ANALOGY:
 * -------------------
 * Think of a "read-only box":
 * - You can only take things OUT (read)
 * - You cannot put things IN (write)
 * - If the box contains Burgers, you can safely treat it as containing Food
 *   (because Burger IS-A Food)
 */
class ReadOnlyContainer<out T>(
    private val items: List<T>  // T appears in input position, but it's OK because
                                // it's a constructor parameter and the list is private
) {
    /**
     * T appears in OUTPUT position (return type)
     * This is allowed with 'out' keyword
     */
    fun getItem(): T {
        return items.first()
    }
    
    // If we tried to add this, it would be an ERROR:
    // fun addItem(item: T) { ... }  // ❌ T in input position not allowed with 'out'
}

/**
 * CLASS HIERARCHY
 * ---------------
 * 
 * Food (parent class)
 *   └── Burger (child class)
 * 
 * Burger IS-A Food (inheritance relationship)
 * 
 * With covariance (out):
 * ReadOnlyContainer<Burger> IS-A ReadOnlyContainer<Food>
 */
open class Food {
    override fun toString() = "Food"
}

class Burger : Food() {
    override fun toString() = "Burger"
}

/**
 * ============================================
 * KEY TAKEAWAYS
 * ============================================
 * 
 * 1. COVARIANCE (out):
 *    - Preserves subtyping: Container<Child> IS-A Container<Parent>
 *    - Type parameter only in OUTPUT positions
 *    - Use for read-only containers
 * 
 * 2. WHEN TO USE:
 *    - When you only read from the container
 *    - When you want to treat specialized containers as general ones
 *    - Examples: List<out T>, Kotlin's List is covariant
 * 
 * 3. REAL-WORLD EXAMPLES:
 *    - List<String> can be assigned to List<Any?>
 *    - Read-only collections
 *    - Result types (Success<Int> can be Result<Number>)
 * 
 * 4. MEMORY AID:
 *    - 'out' = OUTPUT = return types = reading = safe to treat as parent type
 */

