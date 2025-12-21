package dsl

/**
 * ============================================
 * KOTLIN CONCEPT: KTOR-STYLE DSL
 * ============================================
 * 
 * This demonstrates how frameworks like Ktor use DSLs
 * for configuration. Ktor is a Kotlin web framework that
 * uses DSLs extensively for setting up HTTP clients,
 * servers, and routing.
 * 
 * REAL KTOR EXAMPLE:
 * ------------------
 * val client = HttpClient {
 *     expectSuccess = false
 *     install(Logging) {
 *         level = LogLevel.INFO
 *     }
 * }
 * 
 * This example simulates that pattern to show how it works.
 */

fun main() {
    // ============================================
    // KTOR-STYLE CLIENT CONFIGURATION
    // ============================================
    
    val result = HttpClient {
        // Configure the client
        expectSuccess = false
        
        // Install plugins (Ktor's plugin system)
        install("Logging") {
            setting = "verbose"
        }
        
        install("Json") {
            setting = "kotlinx.serialization"
        }
    }
    
    println(result)
}

/**
 * HTTP CLIENT CONFIGURATION CLASS
 * --------------------------------
 * 
 * This represents the configuration for an HTTP client.
 * In real Ktor, this would have many more options.
 */
class HttpClientConfig {
    var expectSuccess: Boolean = true
    private val plugins = mutableListOf<String>()
    
    /**
     * INSTALL PLUGIN METHOD
     * ---------------------
     * 
     * This simulates Ktor's plugin installation system.
     * 
     * install("PluginName") {
     *     // Configure plugin
     * }
     * 
     * HOW IT WORKS:
     * 1. Takes plugin name
     * 2. Takes a lambda to configure the plugin
     * 3. Creates PluginConfig
     * 4. Runs configuration lambda
     * 5. Stores plugin configuration
     * 
     * NESTED DSL PATTERN:
     * -------------------
     * This is a nested DSL - the outer DSL (HttpClient { })
     * contains an inner DSL (install { }).
     */
    fun install(name: String, configure: PluginConfig.() -> Unit) {
        val config = PluginConfig()
        config.configure()  // Run the configuration lambda
        plugins.add("$name: ${config.setting}")
        println("Installed $name with setting: ${config.setting}")
    }
    
    /**
     * Get all installed plugins (for display)
     */
    fun getPlugins(): List<String> = plugins
}

/**
 * PLUGIN CONFIGURATION CLASS
 * ---------------------------
 * 
 * This represents configuration for a single plugin.
 * In real Ktor, each plugin would have its own config class.
 */
class PluginConfig {
    var setting: String = "default"
}

/**
 * HTTP CLIENT BUILDER FUNCTION
 * -----------------------------
 * 
 * This is the main entry point, similar to Ktor's HttpClient { }.
 * 
 * fun HttpClient(configure: HttpClientConfig.() -> Unit): String
 *                    ^
 *                    |
 *            Lambda with receiver
 * 
 * USAGE:
 * ------
 * val client = HttpClient {
 *     expectSuccess = false
 *     install("Logging") { ... }
 * }
 * 
 * HOW IT WORKS:
 * -------------
 * 1. Create HttpClientConfig
 * 2. Run the configuration lambda on it
 * 3. Inside the lambda, you can set properties and call methods
 * 4. Return configured client (or in real Ktor, an actual HttpClient instance)
 */
fun HttpClient(configure: HttpClientConfig.() -> Unit): String {
    val config = HttpClientConfig()
    config.configure()  // Configure the client
    
    val pluginsInfo = config.getPlugins().joinToString(", ")
    return "Client created with expectSuccess=${config.expectSuccess}, plugins=[$pluginsInfo]"
}

/**
 * ============================================
 * REAL KTOR EXAMPLES
 * ============================================
 * 
 * 1. HTTP CLIENT:
 *    val client = HttpClient(CIO) {
 *        install(Logging) {
 *            level = LogLevel.INFO
 *        }
 *        install(Json) {
 *            serializer = KotlinxSerializationJson()
 *        }
 *    }
 * 
 * 2. SERVER ROUTING:
 *    embeddedServer(Netty, port = 8080) {
 *        routing {
 *            get("/") {
 *                call.respondText("Hello")
 *            }
 *        }
 *    }
 * 
 * 3. CLIENT REQUEST:
 *    val response = client.get("https://api.example.com/data")
 * 
 * ============================================
 * KEY CONCEPTS
 * ============================================
 * 
 * 1. BUILDER PATTERN: Configure objects using lambdas
 * 2. NESTED DSLS: DSLs within DSLs (install { })
 * 3. PLUGIN SYSTEM: Extensible architecture
 * 4. TYPE SAFETY: Compile-time checking
 * 5. READABILITY: Code reads like configuration
 * 
 * BENEFITS:
 * ---------
 * - Intuitive API
 * - Type-safe configuration
 * - IDE autocomplete support
 * - Extensible (easy to add plugins)
 * - Kotlin-idiomatic code
 */

