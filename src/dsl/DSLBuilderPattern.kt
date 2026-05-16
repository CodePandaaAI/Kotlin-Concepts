package dsl

// KOTLIN CONCEPT: DSL Builder Pattern
//
// Configuration block instead of many setter calls.
// `{ }` is lambda with receiver: `Sandwich.() -> Unit` — inside, `this` is the Sandwich.
//
// makeSandwich { bread = "Rye" }  →  create Sandwich, run block on it, return it.
//
// Prerequisite: Lambdas.kt (lambda with receiver)

fun main() {

    val mySandwich = makeSandwich {
        bread = "Sourdough"
        filling = "Turkey & Avocado"
        toasted = true
    }
    println(mySandwich)

    val fancySandwich = makeSandwich {
        bread = "Ciabatta"
        filling = "Grilled Chicken"
        toasted = true
        sauce {
            name = "Chipotle Mayo"
            spicy = true
        }
    }
    println(fancySandwich)
}

fun makeSandwich(block: Sandwich.() -> Unit): Sandwich {
    val sandwich = Sandwich()
    sandwich.block()
    return sandwich
}

data class Sandwich(
    var bread: String = "White",
    var filling: String = "None",
    var toasted: Boolean = false,
    var condiment: Sauce = Sauce()
) {
    fun sauce(block: Sauce.() -> Unit) {
        val s = Sauce()
        s.block()
        condiment = s
    }
}

data class Sauce(
    var name: String = "none",
    var spicy: Boolean = false
)
