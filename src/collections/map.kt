package collections

fun main() {
    val studentMarks = mutableMapOf("Romit" to 85, "Aman" to 90, "Priya" to 78) // Key-Value Pairs List

    println("Original Map:")
    println(studentMarks)

    val newStudent = "Ramesh"
    val newMarks = 66

    // Checks if newStudent doesn't exist, adds a newStudent "Ramesh"
    if(!studentMarks.containsKey(newStudent)){
        studentMarks[newStudent] = newMarks

        println("After adding new student $newStudent")
        println(studentMarks) // list after updating list
    }
    val updateStudent = "Romit"
    val updatedMarks = 92

    // updating existing student "Romit" Marks if the student exists

    if(studentMarks.containsKey(updateStudent)){
        studentMarks[updateStudent] = updatedMarks
    }

    val deleteStudent = "Priya"
    // deleting a student from the map if it exists

    if(studentMarks.containsKey(deleteStudent)){
        studentMarks.remove(deleteStudent)
        println("After Deleting Student $deleteStudent")
        // printed the list after deleting student
        println(studentMarks)
    }

}