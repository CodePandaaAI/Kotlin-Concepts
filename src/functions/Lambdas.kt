package functions

// KOTLIN CONCEPT: Lambdas
//
// A function without a name. Store it, pass it, call it like a normal function.
// Syntax: `{ param: Type -> body }`  or  `{ it * 2 }` when there is one param (`it`).
//
// Prerequisite: none. See HigherOrderFunctions.kt for passing lambdas into your own functions.

fun main() {

    fun double(x: Int): Int = x * 2
    val doubleLambda = { x: Int -> x * 2 }

    println(double(5))
    println(doubleLambda(5))

    // Lambda type = parameters → return type
    val greet: (String) -> String = { name -> "Hello, $name!" }
    val add: (Int, Int) -> Int = { a, b -> a + b }
    val doNothing: () -> Unit = { println("No input, no return") }

    println(greet("Alice"))
    println(add(3, 4))

    val numbers = listOf(1, 2, 3, 4, 5)
    println(numbers.map { number -> number * 2 })
    println(numbers.map { it * 2 })  // same — single param can use `it`

    fun doMath(a: Int, b: Int, operation: (Int, Int) -> Int): Int =
        operation(a, b)

    println(doMath(10, 3) { a, b -> a + b })
    println(doMath(10, 3) { a, b -> a - b })
    println(doMath(10, 3) { a, b -> a * b })

    // Last lambda param can sit outside parentheses (trailing lambda)
    val evens = numbers.filter { it % 2 == 0 }
    println(evens)

    // Lambda with receiver: inside the block, `this` IS the receiver (DSL building block)
    val greet1: (String) -> String = { name -> "Hello, $name!" }
    val greet2: String.() -> String = { "Hello, $this!" }

    println(greet1("Bob"))
    println("Charlie".greet2())

    val result = buildString {
        append("Hello, ")
        append("World!")
    }
    println(result)
}
