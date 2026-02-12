package dsl

// ============================================
//  KOTLIN CONCEPT: HTML DSL BUILDER
// ============================================
//
//  Building HTML with strings is ugly and error-prone:
//
//    val html = "<html><body><h1>Title</h1><p>Text</p></body></html>"
//
//  Problems:
//    - No structure (everything on one line)
//    - Easy to forget closing tags (</p>)
//    - No IDE help (it's just a String)
//    - Hard to read and maintain
//
//  A DSL fixes all of this:
//
//    html {
//        body {
//            text("h1", "Title")
//            text("p", "This is a paragraph")
//        }
//    }
//
//  Same HTML, but:
//    ✅ Clear structure (nesting shows hierarchy)
//    ✅ Can't forget closing tags (they're automatic)
//    ✅ IDE autocompletion (it's real Kotlin code!)
//    ✅ Easy to read (looks like HTML itself)

fun main() {

    // ============================================
    //  STEP 1: BUILD HTML WITH OUR DSL
    // ============================================

    val page = html {
        body {
            text("h1", "Welcome!")
            text("p", "This is a Kotlin-generated HTML page.")
            text("p", "Built with a type-safe DSL builder.")
        }
    }

    println(page)
    // Output:
    // <html>
    //   <body>
    //     <h1>Welcome!</h1>
    //     <p>This is a Kotlin-generated HTML page.</p>
    //     <p>Built with a type-safe DSL builder.</p>
    //   </body>
    // </html>



    // ============================================
    //  STEP 2: HOW IT WORKS
    // ============================================
    //
    //  html { ... }
    //    → Creates an HtmlBuilder
    //    → Runs { ... } with HtmlBuilder as receiver
    //    → 'this' = HtmlBuilder inside the block
    //
    //  body { ... }
    //    → Called on HtmlBuilder (because 'this' is HtmlBuilder)
    //    → Creates a BodyBuilder
    //    → Runs { ... } with BodyBuilder as receiver
    //    → Now 'this' = BodyBuilder
    //
    //  text("h1", "Welcome!")
    //    → Called on BodyBuilder
    //    → Adds a <h1>Welcome!</h1> element
    //
    //  Each { } block shifts 'this' to a NEW builder object.
    //  That's how nesting works in DSLs.
}


// ============================================
//  DSL ENTRY POINT
// ============================================

fun html(block: HtmlBuilder.() -> Unit): String {
    val builder = HtmlBuilder()
    builder.block()
    return builder.build()
}


// ============================================
//  HTML BUILDER
// ============================================

class HtmlBuilder {
    private val children = mutableListOf<String>()

    //  body { ... }
    //  Creates a BodyBuilder, runs your config,
    //  collects the output as a child of <html>.
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


// ============================================
//  BODY BUILDER
// ============================================

class BodyBuilder {
    private val elements = mutableListOf<String>()

    //  text("h1", "Welcome!")
    //  Adds: <h1>Welcome!</h1>
    fun text(tag: String, content: String) {
        elements.add("    <$tag>$content</$tag>")
    }

    fun build(): String {
        val content = elements.joinToString("\n")
        return "  <body>\n$content\n  </body>"
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "This is simplified. Does real HTML DSL work like this?"
//
//  A: Yes! The Kotlin library 'kotlinx.html' works exactly the same way,
//     but with ALL HTML tags. You can write fully valid HTML like:
//
//       html {
//           head { title { +"My Page" } }
//           body {
//               div(classes = "container") {
//                   h1 { +"Hello" }
//                   p { +"World" }
//               }
//           }
//       }
//
//     Every tag is a function call with a lambda receiver. Same pattern!
//
//
//  Q: "What does the '+' mean in: +\"Hello\"?"
//
//  A: It's an operator overload. kotlinx.html overloads the unary +
//     operator on String to mean "add this text content".
//     We used text("h1", "Hello") instead for clarity.
//
//
//  Q: "Can I add attributes like class, id, etc.?"
//
//  A: Yes! You'd extend the text() function:
//       fun text(tag: String, content: String, id: String? = null) {
//           val attrs = if (id != null) " id=\"$id\"" else ""
//           elements.add("<$tag$attrs>$content</$tag>")
//       }
//
//     Or use a builder for attributes too (DSL within DSL!).
