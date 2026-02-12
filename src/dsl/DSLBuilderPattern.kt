package dsl

// ============================================
//  KOTLIN CONCEPT: DSL BUILDER PATTERN
// ============================================
//
//  DSL = Domain Specific Language
//
//  A DSL lets you write code that looks like a configuration file
//  instead of function calls. Compare:
//
//    // Normal function calls:
//    val sandwich = Sandwich()
//    sandwich.bread = "sourdough"
//    sandwich.toasted = true
//    sandwich.filling = "turkey"
//
//    // DSL style:
//    val sandwich = makeSandwich {
//        bread = "sourdough"
//        toasted = true
//        filling = "turkey"
//    }
//
//  Both do the same thing. But the DSL feels like describing
//  WHAT you want instead of HOW to build it.
//
//  HOW DOES THIS WORK?
//  The { } block is a LAMBDA WITH RECEIVER (see Lambdas.kt).
//  Inside the block, 'this' refers to the Sandwich object.
//  So 'bread = "sourdough"' is actually 'this.bread = "sourdough"'.
//
//  That's the ENTIRE secret. Nothing more.

fun main() {

    // ============================================
    //  STEP 1: A SIMPLE BUILDER
    // ============================================

    val mySandwich = makeSandwich {
        bread = "Sourdough"
        filling = "Turkey & Avocado"
        toasted = true
    }

    println(mySandwich)
    // Sandwich(bread=Sourdough, filling=Turkey & Avocado, toasted=true,
    //          sauce=Sauce(name=none, spicy=false))



    // ============================================
    //  STEP 2: NESTED DSL BLOCKS
    // ============================================
    //
    //  DSLs can be nested!
    //  makeSandwich { ... } creates a Sandwich.
    //  sauce { ... } inside it configures the sauce.
    //
    //  Each { } block has its OWN receiver object:
    //    makeSandwich { }  → 'this' = Sandwich
    //    sauce { }         → 'this' = Sauce (changes!)

    val fancySandwich = makeSandwich {
        bread = "Ciabatta"
        filling = "Grilled Chicken"
        toasted = true

        sauce {
            // Inside here, 'this' is now a SAUCE object!
            name = "Chipotle Mayo"
            spicy = true
        }
    }

    println(fancySandwich)



    // ============================================
    //  STEP 3: HOW makeSandwich() WORKS
    // ============================================
    //
    //  fun makeSandwich(block: Sandwich.() -> Unit): Sandwich {
    //                          ^^^^^^^^^^^^^^^^^^^^
    //                          |
    //                   Lambda with receiver!
    //                   Inside the block, 'this' = Sandwich
    //
    //      val sandwich = Sandwich()    // 1. Create empty sandwich
    //      sandwich.block()             // 2. Run YOUR config on it
    //      return sandwich              // 3. Return the configured sandwich
    //  }
    //
    //  That's it. Three lines. Create, configure, return.
    //
    //  When you write:
    //    makeSandwich { bread = "Rye" }
    //
    //  It becomes:
    //    val sandwich = Sandwich()
    //    sandwich.bread = "Rye"     // 'this.bread' inside the lambda
    //    return sandwich
}


// ============================================
//  THE BUILDER FUNCTION
// ============================================

fun makeSandwich(block: Sandwich.() -> Unit): Sandwich {
    val sandwich = Sandwich()
    sandwich.block()  // Run the lambda ON the sandwich
    return sandwich
}


// ============================================
//  DATA CLASSES
// ============================================

data class Sandwich(
    var bread: String = "White",
    var filling: String = "None",
    var toasted: Boolean = false,
    var condiment: Sauce = Sauce()
) {
    // Nested DSL: sauce { ... }
    // Creates a Sauce object, runs your block ON it,
    // then assigns it to this sandwich's condiment.
    fun sauce(block: Sauce.() -> Unit) {
        val s = Sauce()
        s.block()          // Run YOUR config on the Sauce
        condiment = s      // Assign the configured sauce
    }
}

data class Sauce(
    var name: String = "none",
    var spicy: Boolean = false
)


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "What's Sandwich.() -> Unit? That syntax is weird."
//
//  A: Let's break it down:
//       (Sandwich) -> Unit = "Takes a Sandwich as parameter"
//       Sandwich.() -> Unit = "Runs ON a Sandwich as receiver"
//
//     The difference:
//       // Parameter style: you HAVE the sandwich
//       val configure1: (Sandwich) -> Unit = { sandwich ->
//           sandwich.bread = "Rye"
//       }
//
//       // Receiver style: you ARE the sandwich
//       val configure2: Sandwich.() -> Unit = {
//           bread = "Rye"    // 'this' = Sandwich, so 'this.bread = "Rye"'
//       }
//
//     Both do the same thing. Receiver style is just cleaner.
//
//
//  Q: "What if I want the DSL to return an IMMUTABLE object?"
//
//  A: Use a mutable builder → create an immutable result:
//       class SandwichBuilder {
//           var bread = "White"
//           fun build(): ImmutableSandwich = ImmutableSandwich(bread)
//       }
//       fun makeSandwich(block: SandwichBuilder.() -> Unit): ImmutableSandwich {
//           val builder = SandwichBuilder()
//           builder.block()
//           return builder.build()
//       }
//     See WorldBuilderDSL.kt for this pattern in action.
//
//
//  Q: "Can DSLs be type-safe?"
//
//  A: Yes! See DSLWithEnums.kt for enum-based options,
//     and HTMLDSLBuilder.kt for nested type-safe builders.
//
//
//  NEXT STEP: See other DSL files in this folder for more patterns —
//  enums, HTML builders, Ktor-style plugins, and fantasy worlds!
