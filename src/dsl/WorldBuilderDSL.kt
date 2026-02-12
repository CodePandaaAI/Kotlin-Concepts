package dsl

// ============================================
//  KOTLIN CONCEPT: WORLD BUILDER DSL
// ============================================
//
//  A fun, creative DSL that builds a fantasy world!
//
//  This example shows the "builder → immutable result" pattern:
//    1. World class is MUTABLE (you configure it)
//    2. WorldConfig class is IMMUTABLE (the final result)
//    3. world { } runs your config, then calls build()
//       to create the immutable output
//
//  This pattern is everywhere in Kotlin:
//    - Retrofit (API client configuration)
//    - Room (database configuration)
//    - OkHttp (HTTP client builder)
//    - Ktor (server and client setup)
//
//  In all of them:
//    Builder (mutable) → .build() → Config (immutable)

fun main() {

    // ============================================
    //  BUILD YOUR WORLD!
    // ============================================

    val myWorld = world {
        name = "Middle Earth"
        biome = Biome.ENCHANTED_FOREST

        creatures {
            add("Dragon", CreatureType.HOSTILE)
            add("Fairy", CreatureType.FRIENDLY)
            add("Wolf", CreatureType.NEUTRAL)
        }

        rules {
            maxPopulation = 100
            allowPvP = false
            difficultyLevel = Difficulty.HARD
        }
    }

    println(myWorld)

    // ============================================
    //  STEP-BY-STEP: WHAT HAPPENS WHEN YOU RUN THIS
    // ============================================
    //
    //  world { ... }
    //    1. Creates a World() object (mutable builder)
    //    2. Runs { ... } with World as 'this'
    //    3. name = "Middle Earth"     → this.name = "Middle Earth"
    //    4. biome = Biome.ENCHANTED   → this.biome = Biome.ENCHANTED
    //    5. creatures { ... }         → creates a Creatures builder
    //       5a. add("Dragon", ...)    → adds to Creatures' internal list
    //       5b. add("Fairy", ...)     → adds to Creatures' internal list
    //       5c. After { } ends, copies creatures to World's list
    //    6. rules { ... }             → creates a Rules builder
    //       6a. maxPopulation = 100   → sets on Rules
    //       6b. After { } ends, assigns rules to World
    //    7. Calls world.build() → returns WorldConfig (immutable!)
    //
    //  The mutable World is thrown away.
    //  What you get back (myWorld) is an IMMUTABLE WorldConfig.
    //  Can't change it. Safe to pass around.
}


// ============================================
//  DSL ENTRY POINT
// ============================================

fun world(block: World.() -> Unit): WorldConfig {
    val w = World()     // 1. Create mutable builder
    w.block()           // 2. Run your config on it
    return w.build()    // 3. Convert to immutable result
}


// ============================================
//  MUTABLE BUILDER (Used during configuration)
// ============================================

class World {
    var name: String = "Unnamed World"
    var biome: Biome = Biome.PLAINS

    private val creatureList = mutableListOf<Creature>()
    private var worldRules = Rules()

    // Nested DSL block: creatures { ... }
    fun creatures(block: Creatures.() -> Unit) {
        val builder = Creatures()
        builder.block()
        creatureList.addAll(builder.getAll())
    }

    // Nested DSL block: rules { ... }
    fun rules(block: Rules.() -> Unit) {
        val r = Rules()
        r.block()
        worldRules = r
    }

    // Convert mutable state → immutable config
    fun build(): WorldConfig = WorldConfig(
        name = name,
        biome = biome,
        creatures = creatureList.toList(),  // Immutable copy!
        rules = worldRules
    )
}


// ============================================
//  CREATURES BUILDER
// ============================================

class Creatures {
    private val list = mutableListOf<Creature>()

    fun add(name: String, type: CreatureType) {
        list.add(Creature(name, type))
    }

    fun getAll(): List<Creature> = list
}


// ============================================
//  DATA CLASSES (Immutable — the final result)
// ============================================

data class WorldConfig(
    val name: String,        // val = read-only. Can't change after creation.
    val biome: Biome,
    val creatures: List<Creature>,
    val rules: Rules
) {
    override fun toString(): String {
        val creatureInfo = creatures.joinToString("\n    ") { "• ${it.name} (${it.type})" }
        return """
            |🌍 World: $name
            |🌿 Biome: $biome
            |
            |🐉 Creatures:
            |    $creatureInfo
            |
            |📜 Rules:
            |    Max Population: ${rules.maxPopulation}
            |    PvP: ${if (rules.allowPvP) "Enabled" else "Disabled"}
            |    Difficulty: ${rules.difficultyLevel}
        """.trimMargin()
    }
}

data class Creature(val name: String, val type: CreatureType)

class Rules {
    var maxPopulation: Int = 50
    var allowPvP: Boolean = true
    var difficultyLevel: Difficulty = Difficulty.NORMAL
}


// ============================================
//  ENUMS: Type-safe options
// ============================================

enum class Biome {
    PLAINS, DESERT, ENCHANTED_FOREST, VOLCANIC, UNDERWATER, FROZEN_TUNDRA
}

enum class CreatureType {
    FRIENDLY, HOSTILE, NEUTRAL
}

enum class Difficulty {
    EASY, NORMAL, HARD, NIGHTMARE
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Why have both World (mutable) and WorldConfig (immutable)?"
//
//  A: Separation of concerns.
//     During config: you NEED mutability (setting properties).
//     After config: you WANT immutability (safety, no accidental changes).
//
//     If WorldConfig was mutable, someone could do:
//       myWorld.name = "HACKED"  // Oops!
//
//     With immutable WorldConfig, that line won't compile. Safe!
//
//
//  Q: "creatures { add(...) } — why a separate builder?"
//
//  A: Without a builder, you'd need to expose the mutable list directly:
//       creatureList.add(Creature("Dragon", HOSTILE))
//     That's ugly AND breaks encapsulation.
//
//     With a builder:
//       creatures { add("Dragon", HOSTILE) }
//     Reads like natural language. The builder collects items,
//     then dumps them into the World. Clean!
//
//
//  Q: "Can I add more DSL functions like 'weather { }' or 'terrain { }'?"
//
//  A: Yes! Just add a new builder class and a function in World:
//       fun weather(block: WeatherBuilder.() -> Unit) { ... }
//     That's the beauty of DSLs — they're extensible.
