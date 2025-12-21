package dsl

/**
 * ============================================
 * KOTLIN CONCEPT: HTML DSL BUILDER
 * ============================================
 * 
 * This demonstrates building HTML using a type-safe DSL.
 * Instead of concatenating strings, you build HTML structure
 * using Kotlin code that looks like HTML.
 * 
 * COMPARISON:
 * -----------
 * String concatenation:
 *   "<html><body>Hello!</body></html>"
 * 
 * DSL approach:
 *   html {
 *       body {
 *           text("Hello!")
 *       }
 *   }
 * 
 * BENEFITS:
 * ---------
 * 1. Type safety (compiler catches errors)
 * 2. Structure is clear
 * 3. Can use loops, conditionals
 * 4. IDE autocomplete support
 */

fun main() {
    // ============================================
    // BUILDING HTML WITH DSL
    // ============================================
    
    val html1 = html {
        body {
            text("Hello!")
        }
    }
    
    println("Generated HTML:")
    println(html1)
    println()
    
    // ============================================
    // MORE COMPLEX EXAMPLE
    // ============================================
    // You could extend this to support:
    // - Attributes: body { id = "main"; class = "container" }
    // - Nested elements: body { div { text("Nested") } }
    // - Loops: body { for (item in items) { div { text(item) } } }
}

/**
 * BODY BUILDER CLASS
 * ------------------
 * 
 * This class builds the content inside <body> tags.
 * It accumulates content and provides methods to add elements.
 */
class BodyBuilder() {
    val content = StringBuilder()
    
    /**
     * Add text content to the body
     * 
     * @param body The text to add
     */
    fun text(body: String) {
        content.append(body)
    }
    
    /**
     * Build the final HTML string
     * This would be called when the body block completes
     */
    fun build(): String = content.toString()
}

/**
 * HTML BUILDER CLASS
 * ------------------
 * 
 * This is the main builder for HTML structure.
 * It manages the overall HTML document and provides
 * methods to add top-level elements like <body>.
 */
class HtmlBuilder {
    val content = StringBuilder()
    
    /**
     * NESTED DSL: BODY ELEMENT
     * -------------------------
     * 
     * This demonstrates nested DSL structure:
     * html {
     *     body { ... }  ← This is the nested DSL
     * }
     * 
     * HOW IT WORKS:
     * 1. Append opening <body> tag
     * 2. Create BodyBuilder
     * 3. Run the lambda block on BodyBuilder (configure it)
     * 4. Get the built content from BodyBuilder
     * 5. Append closing </body> tag
     * 
     * The lambda has BodyBuilder as receiver, so inside { }
     * you can call BodyBuilder methods like text().
     */
    fun body(init: BodyBuilder.() -> Unit) {
        content.append("<body>")
        
        // Create body builder
        val bodyBuilder = BodyBuilder()
        
        // Configure it by running the lambda
        // Inside the lambda, you can call bodyBuilder.text(), etc.
        bodyBuilder.init()
        
        // Get the built content and append it
        content.append(bodyBuilder.build())
        
        content.append("</body>")
    }
    
    /**
     * Build the final HTML string
     */
    fun build(): String = content.toString()
}

/**
 * TOP-LEVEL HTML BUILDER FUNCTION
 * ---------------------------------
 * 
 * This is the entry point for building HTML.
 * 
 * fun html(init: HtmlBuilder.() -> Unit): String
 *                    ^
 *                    |
 *            Lambda with receiver
 * 
 * USAGE:
 * ------
 * html {
 *     body {
 *         text("Hello")
 *     }
 * }
 * 
 * HOW IT WORKS:
 * -------------
 * 1. Create HtmlBuilder
 * 2. Run the lambda block on it
 * 3. Inside the lambda, you can call htmlBuilder.body { ... }
 * 4. Build and return the HTML string
 */
fun html(init: HtmlBuilder.() -> Unit): String {
    val builder = HtmlBuilder()
    builder.init()  // Configure the builder
    return "<html>${builder.build()}</html>"
}

/**
 * ============================================
 * EXTENDING THE DSL
 * ============================================
 * 
 * You could extend this to support more features:
 * 
 * 1. ATTRIBUTES:
 *    body {
 *        id = "main"
 *        class = "container"
 *    }
 * 
 * 2. MORE ELEMENTS:
 *    html {
 *        head {
 *            title("My Page")
 *        }
 *        body {
 *            div { text("Content") }
 *        }
 *    }
 * 
 * 3. CONDITIONALS:
 *    body {
 *        if (showHeader) {
 *            h1 { text("Header") }
 *        }
 *    }
 * 
 * 4. LOOPS:
 *    body {
 *        for (item in items) {
 *            div { text(item) }
 *        }
 *    }
 * 
 * ============================================
 * REAL-WORLD EXAMPLE: KOTLINX.HTML
 * ============================================
 * 
 * Kotlin has a library called kotlinx.html that provides
 * a full HTML DSL:
 * 
 * import kotlinx.html.*
 * import kotlinx.html.stream.createHTML
 * 
 * val html = createHTML().html {
 *     head {
 *         title("My Page")
 *     }
 *     body {
 *         h1 { +"Hello" }
 *         p { +"World" }
 *     }
 * }
 */

