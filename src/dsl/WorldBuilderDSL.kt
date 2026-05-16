package dsl

// KOTLIN CONCEPT: World Builder DSL (mutable builder → immutable result)
//
// `world { }` configures mutable `World`, then `build()` returns immutable `WorldConfig`.
// Same pattern as OkHttp, Room, Retrofit builders.
//
// Prerequisite: DSLBuilderPattern.kt

fun main() {

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
}

fun world(block: World.() -> Unit): WorldConfig {
    val w = World()
    w.block()
    return w.build()
}

class World {
    var name: String = "Unnamed World"
    var biome: Biome = Biome.PLAINS

    private val creatureList = mutableListOf<Creature>()
    private var worldRules = Rules()

    fun creatures(block: Creatures.() -> Unit) {
        val builder = Creatures()
        builder.block()
        creatureList.addAll(builder.getAll())
    }

    fun rules(block: Rules.() -> Unit) {
        val r = Rules()
        r.block()
        worldRules = r
    }

    fun build(): WorldConfig = WorldConfig(
        name = name,
        biome = biome,
        creatures = creatureList.toList(),
        rules = worldRules
    )
}

class Creatures {
    private val list = mutableListOf<Creature>()
    fun add(name: String, type: CreatureType) {
        list.add(Creature(name, type))
    }
    fun getAll(): List<Creature> = list
}

data class WorldConfig(
    val name: String,
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

enum class Biome {
    PLAINS, DESERT, ENCHANTED_FOREST, VOLCANIC, UNDERWATER, FROZEN_TUNDRA
}

enum class CreatureType {
    FRIENDLY, HOSTILE, NEUTRAL
}

enum class Difficulty {
    EASY, NORMAL, HARD, NIGHTMARE
}
