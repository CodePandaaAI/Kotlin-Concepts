package basics

// KOTLIN CONCEPT: Generics
//
// One class or function works for many types. `T` is a placeholder filled in at use site.
// Compiler remembers the real type — safer than storing everything as `Any` and casting.

fun main() {

    // Without generics you'd duplicate Box for String, Int, User, ...
    val stringBox = Box("Hello")
    val intBox = Box(42)
    val doubleBox = Box(3.14)
    val explicitBox = Box<String>("World")  // type can be written explicitly

    println("String box: ${stringBox.item}")
    println("Int box: ${intBox.item}")
    println("Double box: ${doubleBox.item}")

    // Compiler knows item type — no cast
    val value: String = stringBox.item

    val pair1 = Pair2("Alice", 30)
    val pair2 = Pair2(true, 3.14)
    println("\nPair: ${pair1.first}, ${pair1.second}")

    val name: String = pair1.first
    val age: Int = pair1.second
    // val wrong: Int = pair1.first  // compile error — first is String

    println("\nFirst item: ${firstItem(listOf("A", "B", "C"))}")
    println("First number: ${firstItem(listOf(10, 20, 30))}")
}

// T is chosen when you construct: Box("Hi") → T = String
class Box<T>(val item: T)

class Pair2<A, B>(val first: A, val second: B)

// <T> before fun name declares the type parameter for this function
fun <T> firstItem(list: List<T>): T = list.first()
