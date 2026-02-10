package dsl


fun main() {
    val utopia = createWorld {
        name = "Utopia"

        environment {
            gravity = Gravity.MARS
            atmosphere = Atmosphere.BREATHABLE
        }

        geography {
            biome = Biome.JUNGLE
            oceanLevel = 0.4f
        }

        creatures {
            add(CreatureType.HUMANOID, 100000)
            add(CreatureType.MONSTERS, 25)
            add(CreatureType.BEASTS, 5)
        }
    }

    println(utopia)
}

class WorldConfig(
    val environment: Environment = Environment(),
    val geography: Geography = Geography(),
    val creatures: Creatures = Creatures(),
)

class Environment(
    var gravity: Gravity = Gravity.NONE,
    var atmosphere: Atmosphere = Atmosphere.NONE,
)

enum class Gravity {
    EARTH, MOON, JUPITER, MERCURY, MARS, NONE
}

enum class Atmosphere {
    BREATHABLE, TOXIC, NEUTRAL, NONE
}

class Geography(
    var biome: Biome = Biome.NONE,
    var oceanLevel: Float = 0f,
)

enum class Biome {
    TUNDRA, OCEAN, DESSERT, JUNGLE, NONE
}

class Creatures {
    private var creaturesMap: MutableMap<CreatureType, Int> = mutableMapOf()

    fun add(creatureType: CreatureType, count: Int) {
        creaturesMap[creatureType] = count
    }

    override fun toString(): String {
        return creaturesMap.toString()
    }
}

enum class CreatureType {
    HUMANOID, BEASTS, MONSTERS, NONE
}

class World {
    var name: String? = null

    val myWorld = WorldConfig()

    fun environment(init: Environment.() -> Unit) {
        myWorld.environment.init()
    }

    fun geography(init: Geography.() -> Unit) {
        myWorld.geography.init()
    }

    fun creatures(init: Creatures.() -> Unit) {
        myWorld.creatures.init()
    }

    override fun toString(): String {
        return """
            |Environment: (Gravity: ${myWorld.environment.gravity}, Atmosphere: ${myWorld.environment.atmosphere})
            |Geography: (Biome: ${myWorld.geography.biome}, Ocean Level: ${myWorld.geography.oceanLevel})
            |Creatures: ${myWorld.creatures}
        """.trimIndent()
    }
}

fun createWorld(init: World.() -> Unit): World {
    val world = World()

    world.init()

    return world
}
