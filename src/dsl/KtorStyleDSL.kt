package dsl

// KOTLIN CONCEPT: Ktor-Style Plugin DSL
//
// `install(Plugin) { }` — generic `install` picks config type from PluginFactory.
// Each plugin is an `object` factory; block configures that plugin's config class.
//
// Prerequisite: DSLBuilderPattern.kt, basics/BasicGenerics.kt

fun main() {

    val client = createClient {
        timeout = 30_000
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
}

fun createClient(block: ClientBuilder.() -> Unit): HttpClientConfig {
    val builder = ClientBuilder()
    builder.block()
    return builder.build()
}

class ClientBuilder {
    var timeout: Long = 10_000
    var baseUrl: String = ""
    private val plugins = mutableListOf<PluginInfo>()

    fun <CONFIG> install(
        plugin: PluginFactory<CONFIG>,
        block: CONFIG.() -> Unit
    ) {
        val config = plugin.createConfig()
        config.block()
        plugins.add(PluginInfo(plugin.name, config.toString()))
    }

    fun build(): HttpClientConfig = HttpClientConfig(
        timeout = timeout,
        baseUrl = baseUrl,
        plugins = plugins.toList()
    )
}

interface PluginFactory<CONFIG> {
    val name: String
    fun createConfig(): CONFIG
}

class JsonConfig {
    var prettyPrint: Boolean = false
    var strictMode: Boolean = true
    override fun toString() = "prettyPrint=$prettyPrint, strict=$strictMode"
}

object JsonPlugin : PluginFactory<JsonConfig> {
    override val name = "JSON"
    override fun createConfig() = JsonConfig()
}

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
