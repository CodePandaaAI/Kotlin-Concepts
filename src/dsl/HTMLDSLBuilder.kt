package dsl

// KOTLIN CONCEPT: HTML DSL
//
// Nested lambdas build structured output. Each `body { }` or `html { }` switches receiver (`this`).
// Closing tags live in builder code — caller cannot forget them.
//
// Prerequisite: DSLBuilderPattern.kt

fun main() {

    val page = html {
        body {
            text("h1", "Welcome!")
            text("p", "This is a Kotlin-generated HTML page.")
            text("p", "Built with a type-safe DSL builder.")
        }
    }

    println(page)
}

fun html(block: HtmlBuilder.() -> Unit): String {
    val builder = HtmlBuilder()
    builder.block()
    return builder.build()
}

class HtmlBuilder {
    private val children = mutableListOf<String>()

    fun body(block: BodyBuilder.() -> Unit) {
        val bodyBuilder = BodyBuilder()
        bodyBuilder.block()
        children.add(bodyBuilder.build())
    }

    fun build(): String {
        val content = children.joinToString("\n")
        return "<html>\n$content\n</html>"
    }
}

class BodyBuilder {
    private val elements = mutableListOf<String>()

    fun text(tag: String, content: String) {
        elements.add("    <$tag>$content</$tag>")
    }

    fun build(): String {
        val content = elements.joinToString("\n")
        return "  <body>\n$content\n  </body>"
    }
}
