package dsl

/**
 * ============================================
 * KOTLIN CONCEPT: DSL WITH ENUMS
 * ============================================
 * 
 * This demonstrates combining DSLs with enums for
 * type-safe configuration. Enums provide:
 * - Limited, valid options
 * - Type safety
 * - Clear API
 * 
 * USE CASE:
 * ---------
 * Configuring a product (like a phone) with specific
 * models, specifications, and colors. Using enums
 * ensures only valid combinations are possible.
 */

fun main() {
    // ============================================
    // CONFIGURING A PHONE WITH DSL
    // ============================================
    
    val oppoPhone = choosePhoneSpecs {
        model = OppoFindX9Models.FindX9Pro
        ramAndStorageSpecsCombination = RamAndStorageSpecsCombination.HIGH
        color = MobileColor.LunarRed
    }
    
    println("Configured Phone:")
    println(oppoPhone)
    println()
    
    // ============================================
    // TYPE SAFETY BENEFIT
    // ============================================
    // Try uncommenting these to see compile errors:
    // model = "Invalid Model"  // ❌ Compile error: String is not OppoFindX9Models
    // color = "Red"            // ❌ Compile error: String is not MobileColor
    
    // Enums ensure only valid values can be assigned!
}

/**
 * PHONE SPECIFICATION BUILDER FUNCTION
 * -------------------------------------
 * 
 * This is the DSL entry point for configuring a phone.
 * 
 * fun choosePhoneSpecs(specs: Mobile.() -> Unit): Mobile
 *                          ^
 *                          |
 *                  Lambda with receiver
 * 
 * The lambda has Mobile as receiver, so inside { } you're
 * working directly with Mobile properties.
 */
fun choosePhoneSpecs(specs: Mobile.() -> Unit): Mobile {
    val smartPhone = Mobile()
    smartPhone.specs()  // Configure using the lambda
    return smartPhone
}

/**
 * MOBILE PHONE CLASS
 * ------------------
 * 
 * Represents a mobile phone with configurable specifications.
 * Properties use enum types for type safety.
 */
class Mobile {
    val name = "Oppo Find X9 Series"
    
    /**
     * MODEL PROPERTY
     * --------------
     * Uses enum to ensure only valid models can be assigned.
     */
    var model: OppoFindX9Models = OppoFindX9Models.None
    
    /**
     * RAM AND STORAGE COMBINATION
     * ----------------------------
     * Uses enum with associated values (ram and storage amounts).
     */
    var ramAndStorageSpecsCombination: RamAndStorageSpecsCombination = RamAndStorageSpecsCombination.None
    
    /**
     * COLOR PROPERTY
     * --------------
     * Uses enum for available colors.
     */
    var color = MobileColor.None
    
    override fun toString(): String {
        return """
            |$name
            |Model: $model
            |Mobile Specs: $ramAndStorageSpecsCombination
            |Mobile Color: $color
        """.trimMargin()
    }
}

/**
 * PHONE MODEL ENUM
 * ----------------
 * 
 * Defines available phone models.
 * Using enum ensures only these models are valid.
 */
enum class OppoFindX9Models {
    FindX9,
    FindX9Pro,
    None  // Default/unselected state
}

/**
 * RAM AND STORAGE SPECS ENUM
 * ---------------------------
 * 
 * ENUMS WITH VALUES:
 * ------------------
 * This enum demonstrates enums with associated values.
 * Each variant can hold data (ram and storage amounts).
 * 
 * SYNTAX:
 * -------
 * BASE(8, 256) means BASE variant with ram=8GB, storage=256GB
 * 
 * BENEFITS:
 * ---------
 * - Type-safe combinations
 * - Data associated with each option
 * - Can access values: BASE.ram, BASE.storage
 */
enum class RamAndStorageSpecsCombination(
    val ram: Int? = null,
    val storage: Int? = null
) {
    BASE(8, 256),      // 8GB RAM, 256GB Storage
    MEDIUM(12, 256),   // 12GB RAM, 256GB Storage
    HIGH(16, 512),     // 16GB RAM, 512GB Storage
    None;              // Default/unselected state
    
    /**
     * CUSTOM toString()
     * -----------------
     * Provides a readable string representation.
     * Handles the None case specially.
     */
    override fun toString(): String {
        return if (ram != null && storage != null) {
            "${ram}GB RAM, ${storage}GB Storage"
        } else {
            "None"
        }
    }
}

/**
 * MOBILE COLOR ENUM
 * -----------------
 * 
 * Simple enum for available colors.
 */
enum class MobileColor {
    White,
    Black,
    FlowPurple,
    LunarRed,
    None  // Default/unselected state
}

/**
 * ============================================
 * WHY COMBINE DSLs WITH ENUMS?
 * ============================================
 * 
 * 1. TYPE SAFETY:
 *    - Compiler prevents invalid values
 *    - No typos or invalid strings
 *    - IDE autocomplete shows all options
 * 
 * 2. READABILITY:
 *    - Code reads naturally
 *    - Clear what options are available
 *    - Self-documenting
 * 
 * 3. MAINTAINABILITY:
 *    - Add new options by adding enum values
 *    - Compiler ensures all cases are handled
 *    - Refactoring is safer
 * 
 * 4. RUNTIME SAFETY:
 *    - No need to validate strings
 *    - Enum values are guaranteed valid
 *    - Can use exhaustive when expressions
 * 
 * ============================================
 * REAL-WORLD EXAMPLES
 * ============================================
 * 
 * 1. GRADLE BUILD SCRIPTS:
 *    android {
 *        buildTypes {
 *            release { ... }
 *            debug { ... }
 *        }
 *    }
 * 
 * 2. DATABASE SCHEMAS:
 *    table("users") {
 *        column("id", INTEGER)
 *        column("name", VARCHAR(255))
 *    }
 * 
 * 3. API CLIENT CONFIGURATION:
 *    apiClient {
 *        environment = Environment.PRODUCTION
 *        timeout = Timeout.SHORT
 *    }
 */

