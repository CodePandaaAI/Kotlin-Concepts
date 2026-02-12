package dsl

// ============================================
//  KOTLIN CONCEPT: DSL WITH ENUMS
// ============================================
//
//  DSLs let you write configuration-like code.
//  But what if someone writes:
//
//    choosePhone {
//        model = "iPhone 47 Ultra Pro Max Plus"  // ← Not a real model!
//        color = "rainbow sparkle"               // ← Not a real color!
//    }
//
//  With Strings, ANYTHING goes. No compiler safety.
//  With ENUMS, only VALID options compile:
//
//    choosePhone {
//        model = PhoneModel.FIND_X9_PRO         // ✅ Must pick from the enum
//        color = PhoneColor.MIDNIGHT_BLACK       // ✅ Must pick from the enum
//    }
//
//  Enums + DSLs = type-safe configuration.

fun main() {

    // ============================================
    //  STEP 1: TYPE-SAFE PHONE CONFIGURATION
    // ============================================

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

    // Try changing PhoneModel to something that doesn't exist:
    //   model = PhoneModel.GALAXY_S99  // ❌ COMPILE ERROR!
    //   model = "My Dream Phone"        // ❌ COMPILE ERROR! (wrong type)
    //
    // Only the options in the enum are allowed. Safe!



    // ============================================
    //  STEP 2: ANOTHER CONFIGURATION
    // ============================================

    val budgetPhone = configurePhone {
        model = PhoneModel.RENO_12
        color = PhoneColor.OCEAN_BLUE
        storage = StorageSize.GB_128
        // No accessories — keep it simple
    }

    println(budgetPhone)
}


// ============================================
//  ENUMS: THE VALID OPTIONS
// ============================================
//
//  Enums are a FIXED SET of options:
//    "These are the ONLY valid values. Nothing else."
//
//  Like a dropdown menu vs a text field:
//    Dropdown: [Option 1] [Option 2] [Option 3]  ← can't type random stuff
//    Text field: [______________]                 ← can type anything

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


// ============================================
//  BUILDER CLASS
// ============================================

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


// ============================================
//  DSL ENTRY POINT
// ============================================
//
//  Same pattern as makeSandwich:
//    1. Create empty config
//    2. Run the lambda ON it (lambda with receiver)
//    3. Return the configured object

fun configurePhone(block: PhoneConfig.() -> Unit): PhoneConfig {
    val config = PhoneConfig()
    config.block()
    return config
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Why not just use Strings with validation?"
//
//  A: With Strings, errors happen at RUNTIME (crashes in production).
//     With Enums, errors happen at COMPILE TIME (red squiggles in your IDE).
//     Compile-time > runtime, always.
//
//
//  Q: "Can enums have methods and properties?"
//
//  A: Yes! Each enum value can have data:
//       enum class PhoneModel(val displayName: String) {
//           FIND_X9_PRO("Find X9 Pro")  ← displayName = "Find X9 Pro"
//       }
//     You can also add methods to the enum class itself.
//
//
//  Q: "What if I need to add new options later?"
//
//  A: Just add them to the enum. Existing code keeps working.
//     If you use 'when' on the enum, the compiler will warn you
//     about unhandled new values (if it's exhaustive).
