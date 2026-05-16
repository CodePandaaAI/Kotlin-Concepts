package coroutines

//fun main() {
//    println("1. Starting app")
//
//    val userData = fetchUserFromDatabase()
//
//    println("3. Got user: $userData")
//    println("4. App is done")
//}
//fun fetchUserFromDatabase(): String {
//    println("2. Fetching user... (takes 3 seconds)")
//    Thread.sleep(3000)  // Simulates a slow database read
//    return "Romit"
//}

//fun main() {
//    println("1. Starting app")
//
//    fetchUserWithCallback { userData ->
//        println("3. Got user: $userData")
//    }
//
//    println("2. Main thread is FREE, can do UI work")
//}
//
//fun fetchUserWithCallback(onResult: (String) -> Unit) {
//    // Start a new thread to do the slow work
//    Thread {
//        Thread.sleep(3000)       // Thread-B is blocked, but main thread is free
//        val result = "Romit"
//        onResult(result)         // Call the callback with the result
//    }.start()
//}


class DoEverythingStateMachine {
    var label: Int = 0
    var result: Any? = null

    var user: String? = null
    var posts: List<String>? = null

    fun runNextSection() {
        when (label) {
            0 -> {
                println("Section 0: Starting. About to fetch user.")
                label = 1
                // We DON'T call runNextSection() here.
                // We RETURN. The thread is free.
                // Whoever fetches the user will call us back.
            }
            1 -> {
                user = result as String
                println("Section 1: Got user: $user. Fetching posts.")
                label = 2
                // RETURN. Thread is free.
            }
            2 -> {
                posts = result as List<String>
                println("Section 2: Got ${posts!!.size} posts. Saving.")
                label = 3
                // RETURN. Thread is free.
            }
            3 -> {
                println("Section 3: All done!")
            }
        }
    }
}


fun main() {
    val sm = DoEverythingStateMachine()

    // Section 0 runs and returns. Thread is free.
    sm.runNextSection()

    // Simulate: 3 seconds later, database returns "Romit"
    // In real life, this would be a callback from the database
    sm.result = "Romit"
    sm.runNextSection()   // Section 1 runs

    // Simulate: 2 seconds later, network returns posts
    sm.result = listOf("Post A", "Post B")
    sm.runNextSection()   // Section 2 runs

    // Simulate: save completes
    sm.result = Unit
    sm.runNextSection()   // Section 3 runs → "All done!"
}