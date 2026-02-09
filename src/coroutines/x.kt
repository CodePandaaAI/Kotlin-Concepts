package coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun main() {
    val c = CoroutineScope(kotlinx.coroutines.Dispatchers.Default)

    c.launch {
        println(1+1)
    }

    println("I came First or Second")


}