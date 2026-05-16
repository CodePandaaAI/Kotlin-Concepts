package oop

// KOTLIN CONCEPT: Sealed Classes & Interfaces
//
// Closed family of types — only subclasses you define (same file) can exist.
// `when` on a sealed type is exhaustive: compiler errors if you miss a case.
//
// sealed interface — no shared fields; children can differ completely.
// sealed class — shared constructor/properties on the parent (e.g. timestamp).
//
// No data? use `data object`. Has data? use `data class`.

fun main() {

    handleState(UiState.Success(listOf("Item 1", "Item 2")))
    handleState(UiState.Loading)
    handleState(UiState.Error("Network timeout"))

    val okResult = fetchData(success = true)
    val failResult = fetchData(success = false)

    when (okResult) {
        is TimestampedResult.Ok -> {
            println("\nGot '${okResult.data}' at time ${okResult.timestamp}")
        }
        is TimestampedResult.Failed -> {
            println("Failed: $failResult at ${failResult.timestamp}")
        }
    }
}

sealed interface UiState {
    data object Loading : UiState
    data class Success(val items: List<String>) : UiState
    data class Error(val message: String) : UiState
}

fun handleState(state: UiState) {
    when (state) {
        is UiState.Loading -> println("⏳ Loading...")
        is UiState.Success -> println("✅ Showing ${state.items.size} items")
        is UiState.Error -> println("❌ Error: ${state.message}")
    }
}

sealed class TimestampedResult(val timestamp: Long = System.currentTimeMillis()) {
    data class Ok(val data: String) : TimestampedResult()
    data class Failed(val reason: String) : TimestampedResult()
}

fun fetchData(success: Boolean): TimestampedResult =
    if (success) TimestampedResult.Ok("Here is your data!")
    else TimestampedResult.Failed("Something went wrong")
