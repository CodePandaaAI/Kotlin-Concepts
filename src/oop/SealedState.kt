package oop

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

sealed class SealedState() {
    object Loading : SealedState()
    data class HomeScreen(val name: String = "Home Screen") : SealedState()
    object Error : SealedState()
}

suspend fun screen(state: SealedState) {
    when (state) {
        is SealedState.Loading -> println("Its Loading Please Wait")
        is SealedState.HomeScreen -> {
            println("We Are At ${state.name}")
            countTen()
        }
        is SealedState.Error -> println("Error Occurred")
    }
}

suspend fun countTen() {
    print("Loading Profile")
    for (i in 1..10) {
        delay(300)
        print(".")
    }
    println()
    println("Profile loaded successfully!")
    delay(800)
    println("Name: John Doe")
    println("Age: 30")
    println("Email: john.doe@example.com")
    delay(300)
    println("Location: New York")
    println("Bio: Software Developer passionate about Kotlin")
    delay(300)
}



fun main() = runBlocking {
    screen(SealedState.HomeScreen(name = "Hallo Loite Guten Tag"))
}