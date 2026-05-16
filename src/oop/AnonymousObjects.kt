package oop

// KOTLIN CONCEPT: Anonymous Objects
//
// Implement an interface (or extend a class) inline, without a named class file.
// Syntax: `object : SomeInterface { override fun ... }`
//
// Use when: one-off implementation, multiple methods, or you need properties on the object.
// If interface has one abstract method, a lambda may be enough (SAM) — see Comparator example.

fun main() {

    class EnglishGreeter : Greeter {
        override fun greet(name: String): String = "Hello, $name!"
    }
    welcomeUser(EnglishGreeter(), "Alice")

    val french = object : Greeter {
        override fun greet(name: String): String = "Bonjour, $name!"
    }
    welcomeUser(french, "Bob")

    welcomeUser(object : Greeter {
        override fun greet(name: String): String = "Hola, $name!"
    }, "Charlie")

    // Anonymous object can hold state (lambdas can't do this the same way)
    val counter = object {
        var count = 0
        fun increment() {
            count++
            println("Count: $count")
        }
    }
    counter.increment()
    counter.increment()

    data class Student(val name: String, val grade: Int)
    val students = listOf(
        Student("Charlie", 85),
        Student("Alice", 95),
        Student("Bob", 72)
    )

    val sorted = students.sortedWith(object : Comparator<Student> {
        override fun compare(a: Student, b: Student): Int = a.grade - b.grade
    })
    println("\nSorted by grade:")
    sorted.forEach { println("  ${it.name}: ${it.grade}") }

    // Comparator is SAM — lambda works too
    students.sortedBy { it.grade }
}

interface Greeter {
    fun greet(name: String): String
}

fun welcomeUser(greeter: Greeter, name: String) {
    println(greeter.greet(name))
}
