package collections

fun main() {
    val studentMarks = mutableMapOf("Romit" to 85, "Aman" to 90, "Priya" to 78)

    println("Original Map: $studentMarks")

    // Add or update directly
    studentMarks["Karan"] = 88  // Adds new
    studentMarks["Romit"] = 92  // Updates existing

    // Remove directly
    studentMarks.remove("Priya")
    studentMarks.putIfAbsent("Aman", 66)

    println("Updated Map: $studentMarks")

}
