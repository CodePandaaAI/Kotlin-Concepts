class Car {
    companion object { // Declares the companion object
        var totalCarsMade = 0 // A property that belongs to the Car class itself

        fun createCar(model: String): Car { // A function to create new Car instances
            totalCarsMade++ // Increment the counter
            return Car(model)
        }
    }

    constructor(model: String) { // Primary constructor for the Car class
        println("Creating a new $model car")
    }
}

fun main() {
    val car1 = Car.createCar("Sedan") // Access the companion object function directly via the class name
    val car2 = Car.createCar("SUV")
    println(car1)
    val car3 = Car.createCar("Tata")
    println("Total cars made: ${Car.totalCarsMade}") // Access the companion object property directly
}
