package dsl

// ============================================
//  KOTLIN CONCEPT: KTOR-STYLE DSL
// ============================================
//
//  Ktor is Kotlin's web framework by JetBrains.
//  It uses DSLs for configuration:
//
//    val client = HttpClient {
//        install(JsonPlugin) {
//            prettyPrint = true
//        }
//        install(LoggingPlugin) {
//            level = LogLevel.ALL
//        }
//    }
//
//  This LOOKS like a config file, but it's real Kotlin code.
//  Each 'install' block is a nested DSL.
//
//  Let's build a simplified version of this
//  to understand how frameworks use DSLs internally.

fun main() {

    // ============================================
    //  STEP 1: CONFIGURE AN HTTP CLIENT
    // ============================================

    val client = createClient {
        timeout = 30_000        // 30 seconds
        baseUrl = "https://api.example.com"

        install(JsonPlugin) {
            prettyPrint = true
            strictMode = false
        }

        install(LoggingPlugin) {
            level = LogLevel.HEADERS
            logBody = true
        }
    }

    println(client)

    // This reads like a configuration file!
    // But every line is a real Kotlin expression.
    // The compiler checks everything at compile time.



    // ============================================
    //  STEP 2: HOW install() WORKS
    // ============================================
    //
    //  install(JsonPlugin) { prettyPrint = true }
    //
    //  Broken down:
    //    install       → function on ClientBuilder (via 'this')
    //    JsonPlugin    → a COMPANION OBJECT that has a configure() method
    //    { ... }       → lambda that sets up the plugin's config
    //
    //  Inside install():
    //    1. Get the plugin's config object
    //    2. Run the lambda ON that config (lambda with receiver)
    //    3. Store the configured plugin
}


// ============================================
//  DSL ENTRY POINT
// ============================================

fun createClient(block: ClientBuilder.() -> Unit): HttpClientConfig {
    val builder = ClientBuilder()
    builder.block()
    return builder.build()
}


// ============================================
//  CLIENT BUILDER
// ============================================

class ClientBuilder {
    var timeout: Long = 10_000
    var baseUrl: String = ""
    private val plugins = mutableListOf<PluginInfo>()

    /**
     * install(SomePlugin) { ... }
     *
     * This function accepts:
     *   - A plugin definition (companion object implementing PluginFactory)
     *   - A configuration lambda
     *
     * The CONFIG type is determined by the plugin's generic parameter.
     */
    fun <CONFIG> install(
        plugin: PluginFactory<CONFIG>,
        block: CONFIG.() -> Unit
    ) {
        val config = plugin.createConfig()
        config.block()  // Run your config ON the config object
        plugins.add(PluginInfo(plugin.name, config.toString()))
    }

    fun build(): HttpClientConfig = HttpClientConfig(
        timeout = timeout,
        baseUrl = baseUrl,
        plugins = plugins.toList()
    )
}


// ============================================
//  PLUGIN SYSTEM
// ============================================

//  A PluginFactory knows:
//    - Its name
//    - How to create its configuration object
interface PluginFactory<CONFIG> {
    val name: String
    fun createConfig(): CONFIG
}

//  Each plugin defines its own config class
//  and a companion object as the factory.

// --- JSON Plugin ---
class JsonConfig {
    var prettyPrint: Boolean = false
    var strictMode: Boolean = true
    override fun toString() = "prettyPrint=$prettyPrint, strict=$strictMode"
}

object JsonPlugin : PluginFactory<JsonConfig> {
    override val name = "JSON"
    override fun createConfig() = JsonConfig()
}

// --- Logging Plugin ---
class LoggingConfig {
    var level: LogLevel = LogLevel.NONE
    var logBody: Boolean = false
    override fun toString() = "level=$level, logBody=$logBody"
}

object LoggingPlugin : PluginFactory<LoggingConfig> {
    override val name = "Logging"
    override fun createConfig() = LoggingConfig()
}

enum class LogLevel { NONE, INFO, HEADERS, ALL }


// ============================================
//  RESULT
// ============================================

data class PluginInfo(val name: String, val config: String)

data class HttpClientConfig(
    val timeout: Long,
    val baseUrl: String,
    val plugins: List<PluginInfo>
) {
    override fun toString(): String {
        val pluginStr = plugins.joinToString("\n") { "    📦 ${it.name}: ${it.config}" }
        return """
            |🌐 HTTP Client:
            |   Base URL: $baseUrl
            |   Timeout:  ${timeout}ms
            |   Plugins:
            |$pluginStr
        """.trimMargin()
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "What's the companion object doing? Why is JsonPlugin an 'object'?"
//
//  A: 'object' = singleton. There's only ONE JsonPlugin.
//     You don't create it — it just exists.
//     install(JsonPlugin) passes this singleton as a parameter.
//     JsonPlugin knows how to create a JsonConfig().
//     It's a FACTORY: "I know how to make my own config."
//
//
//  Q: "How does install() know which config type to use?"
//
//  A: Generics! The function signature is:
//       fun <CONFIG> install(plugin: PluginFactory<CONFIG>, block: CONFIG.() -> Unit)
//
//     When you write install(JsonPlugin) { ... }:
//       JsonPlugin is PluginFactory<JsonConfig>
//       So CONFIG = JsonConfig
//       So block: JsonConfig.() -> Unit
//       Inside the block, 'this' is a JsonConfig!
//
//     The compiler figures out CONFIG automatically from the plugin.
//
//
//  Q: "Is this how real Ktor works?"
//
//  A: Very similar! Real Ktor is more complex (uses coroutines,
//     has a pipeline system, etc.), but the DSL pattern is the same:
//     Builder + Lambda with receiver + Plugin factory.
