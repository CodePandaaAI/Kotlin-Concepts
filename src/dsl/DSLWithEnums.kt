package dsl

// KOTLIN CONCEPT: DSL + Enums
//
// DSL fields use enums instead of String — invalid options fail at compile time, not runtime.
//
// Prerequisite: DSLBuilderPattern.kt

fun main() {

    val myPhone = configurePhone {
        model = PhoneModel.FIND_X9_PRO
        color = PhoneColor.MIDNIGHT_BLACK
        storage = StorageSize.GB_256
        accessories {
            add(Accessory.CASE)
            add(Accessory.SCREEN_PROTECTOR)
        }
    }
    println(myPhone)

    val budgetPhone = configurePhone {
        model = PhoneModel.RENO_12
        color = PhoneColor.OCEAN_BLUE
        storage = StorageSize.GB_128
    }
    println(budgetPhone)
}

enum class PhoneModel(val displayName: String) {
    FIND_X9("Find X9"),
    FIND_X9_PRO("Find X9 Pro"),
    RENO_12("Reno 12"),
    RENO_12_PRO("Reno 12 Pro")
}

enum class PhoneColor(val hex: String) {
    MIDNIGHT_BLACK("#1a1a1a"),
    OCEAN_BLUE("#0066cc"),
    SUNSET_GOLD("#ffcc00"),
    PEARL_WHITE("#f5f5f5")
}

enum class StorageSize(val gb: Int) {
    GB_128(128),
    GB_256(256),
    GB_512(512)
}

enum class Accessory {
    CASE, SCREEN_PROTECTOR, CHARGER, EARBUDS
}

class PhoneConfig {
    var model: PhoneModel = PhoneModel.FIND_X9
    var color: PhoneColor = PhoneColor.MIDNIGHT_BLACK
    var storage: StorageSize = StorageSize.GB_128
    private val accessoryList = mutableListOf<Accessory>()

    fun accessories(block: AccessoryBuilder.() -> Unit) {
        val builder = AccessoryBuilder()
        builder.block()
        accessoryList.addAll(builder.getAll())
    }

    override fun toString(): String {
        val accStr = if (accessoryList.isEmpty()) "none" else accessoryList.joinToString(", ")
        return """
            |📱 Phone Order:
            |   Model:    ${model.displayName}
            |   Color:    ${color.name.replace("_", " ")} (${color.hex})
            |   Storage:  ${storage.gb}GB
            |   Extras:   $accStr
        """.trimMargin()
    }
}

class AccessoryBuilder {
    private val list = mutableListOf<Accessory>()
    fun add(accessory: Accessory) { list.add(accessory) }
    fun getAll(): List<Accessory> = list
}

fun configurePhone(block: PhoneConfig.() -> Unit): PhoneConfig {
    val config = PhoneConfig()
    config.block()
    return config
}
