package collections

fun main() {
    val studentMarks = mutableMapOf("Romit" to 85, "Aman" to 90, "Priya" to 78)

    println("Original Map:")
    println(studentMarks)

    val newStudent = "Ramesh"
    val newMarks = 66

    if(!studentMarks.containsKey(newStudent)){
        studentMarks[newStudent] = newMarks

        println("After adding new student $newStudent")
        println(studentMarks)
    }
    val updateStudent = "Romit"
    val updatedMarks = 92
    if(studentMarks.containsKey(updateStudent)){
        studentMarks[updateStudent] = updatedMarks
    }

}