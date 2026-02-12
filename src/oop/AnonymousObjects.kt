package oop

// ============================================
//  KOTLIN CONCEPT: ANONYMOUS OBJECTS
// ============================================
//
//  Sometimes you need a class that:
//    - You'll only use ONCE
//    - Has a very simple implementation
//    - Isn't worth naming and putting in its own file
//
//  That's an anonymous object.
//
//  Think of it like a sticky note vs a document:
//    Named class  = wrote a whole document, filed it, named it
//    Anonymous    = scribbled on a sticky note, used it, done

fun main() {

    // ============================================
    //  STEP 1: THE PROBLEM
    // ============================================
    //
    //  Let's say you have an interface:

    //  interface Greeter {
    //      fun greet(name: String): String
    //  }

    //  And a function that needs a Greeter:

    //  fun welcomeUser(greeter: Greeter, name: String) {
    //      println(greeter.greet(name))
    //  }

    //  Normal approach: create a class that implements Greeter.

    class EnglishGreeter : Greeter {
        override fun greet(name: String): String = "Hello, $name!"
    }

    val english = EnglishGreeter()
    welcomeUser(english, "Alice")  // Hello, Alice!

    // This works, but...
    // We created a whole CLASS just for one greeting style.
    // What if we need 5 different greeting styles?
    // 5 classes just for one-line functions? Overkill.



    // ============================================
    //  STEP 2: THE SOLUTION (Anonymous Object)
    // ============================================
    //
    //  Instead of creating a named class, use 'object : Interface':

    val french = object : Greeter {
        override fun greet(name: String): String = "Bonjour, $name!"
    }
    welcomeUser(french, "Bob")  // Bonjour, Bob!

    // No class name. No file. Just an inline implementation.
    // "I need a Greeter RIGHT NOW. Here's the implementation. Done."

    // You don't even need to store it in a variable:
    welcomeUser(
        object : Greeter {
            override fun greet(name: String): String = "Hola, $name!"
        },
        "Charlie"
    )
    // Hola, Charlie!



    // ============================================
    //  STEP 3: ANONYMOUS OBJECTS WITH STATE
    // ============================================
    //
    //  Unlike lambdas, anonymous objects can have PROPERTIES:

    val counter = object {
        var count = 0
        fun increment() {
            count++
            println("Count: $count")
        }
    }

    counter.increment()  // Count: 1
    counter.increment()  // Count: 2
    counter.increment()  // Count: 3

    // This object has no type name. It just... exists.
    // Has state (count) and behavior (increment).



    // ============================================
    //  STEP 4: COMPARING APPROACHES
    // ============================================

    // APPROACH 1: Named class (best for reuse)
    class FormalGreeter : Greeter {
        override fun greet(name: String) = "Good day, $name."
    }
    val formal = FormalGreeter()

    // APPROACH 2: Anonymous object (best for one-time use)
    val casual = object : Greeter {
        override fun greet(name: String) = "Hey $name!"
    }

    // APPROACH 3: Lambda (if the interface has ONE method)
    // val quick: Greeter = Greeter { name -> "Yo, $name!" }
    // ↑ Only works with SAM interfaces (Single Abstract Method)
    // Greeter is our custom interface, so this syntax requires
    // it to be declared as a 'fun interface'.

    welcomeUser(formal, "Dave")   // Good day, Dave.
    welcomeUser(casual, "Eve")    // Hey Eve!



    // ============================================
    //  STEP 5: REAL-WORLD EXAMPLE — COMPARATOR
    // ============================================

    data class Student(val name: String, val grade: Int)

    val students = listOf(
        Student("Charlie", 85),
        Student("Alice", 95),
        Student("Bob", 72)
    )

    // Sort using anonymous Comparator:
    val sorted = students.sortedWith(object : Comparator<Student> {
        override fun compare(a: Student, b: Student): Int {
            return a.grade - b.grade  // Ascending by grade
        }
    })
    println("\nSorted by grade:")
    sorted.forEach { println("  ${it.name}: ${it.grade}") }

    // But in practice, you'd use a lambda (Comparator is a SAM interface):
    val sortedSimpler = students.sortedWith(Comparator { a, b -> a.grade - b.grade })

    // Or even simpler:
    val sortedSimplest = students.sortedBy { it.grade }
}


// ============================================
//  SUPPORTING CODE
// ============================================

interface Greeter {
    fun greet(name: String): String
}

fun welcomeUser(greeter: Greeter, name: String) {
    println(greeter.greet(name))
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "When should I use an anonymous object vs a lambda?"
//  A: Anonymous object: multiple methods, or you need state (properties)
//     Lambda: single method, simple logic
//
//     // Lambda (simpler):
//     button.setOnClickListener { doSomething() }
//
//     // Anonymous object (when you need multiple methods):
//     textField.addTextWatcher(object : TextWatcher {
//         override fun beforeTextChanged(...) { }
//         override fun onTextChanged(...) { }
//         override fun afterTextChanged(...) { }
//     })
//
//  Q: "Can anonymous objects implement multiple interfaces?"
//  A: Yes!
//     val combo = object : InterfaceA, InterfaceB {
//         override fun methodFromA() { ... }
//         override fun methodFromB() { ... }
//     }
//
//  Q: "Can I use anonymous objects in return statements?"
//  A: Be careful. If the function's return type is the interface,
//     only interface methods are visible. The anonymous object's
//     extra properties are hidden.
//
//  NEXT STEP: See SealedClasses.kt for sealed interfaces —
//  a much more powerful way to define families of types.
