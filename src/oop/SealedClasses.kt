package oop

// ============================================
//  KOTLIN CONCEPT: SEALED CLASSES & INTERFACES
// ============================================
//
//  Start with a question:
//
//  You build a function that handles network responses.
//  It could be: Loading, Success, or Error.
//  You use an interface:
//
//    interface ResponseState
//    class Loading : ResponseState
//    class Success(val data: String) : ResponseState
//    class Error(val message: String) : ResponseState
//
//  Problem: ANYONE can create a new class that implements ResponseState.
//  Some random code in another file could add:
//
//    class Timeout : ResponseState  ← You didn't plan for this!
//
//  Your 'when' block doesn't handle Timeout. It compiles fine.
//  But at runtime? 💥 Crash or silent bug.
//
//  'sealed' fixes this:
//    "Only the classes I define HERE can implement me. Nobody else. Ever."
//    Now the compiler knows ALL possible cases and FORCES you to handle them.

fun main() {

    // ============================================
    //  STEP 1: SEALED INTERFACE IN ACTION
    // ============================================

    val success: UiState = UiState.Success(listOf("Item 1", "Item 2"))
    val loading: UiState = UiState.Loading
    val error: UiState = UiState.Error("Network timeout")

    handleState(success)  // ✅ Showing 2 items
    handleState(loading)  // ⏳ Loading...
    handleState(error)    // ❌ Error: Network timeout



    // ============================================
    //  STEP 2: EXHAUSTIVE 'when' (The Big Win)
    // ============================================
    //
    //  With sealed interfaces, 'when' is EXHAUSTIVE:
    //  the compiler checks that you handle ALL cases.
    //
    //  when (state) {
    //      is UiState.Loading -> ...
    //      is UiState.Success -> ...
    //      is UiState.Error -> ...
    //      // No 'else' needed! Compiler knows these are the ONLY options.
    //  }
    //
    //  If you add a new state later (e.g., UiState.Empty),
    //  the compiler will ERROR on every 'when' that doesn't handle it.
    //  You'll never miss a case.



    // ============================================
    //  STEP 3: SEALED CLASS (When You Need Shared State)
    // ============================================
    //
    //  Sealed INTERFACE: no shared properties.
    //  Sealed CLASS: can have shared properties.

    val okResult = fetchData(success = true)
    val failResult = fetchData(success = false)

    when (okResult) {
        is TimestampedResult.Ok -> {
            println("\nGot '${okResult.data}' at time ${okResult.timestamp}")
            //                                          ^^^^^^^^^^^^^^^^^^^^
            //                      This comes from the PARENT (shared state!)
        }
        is TimestampedResult.Failed -> {
            println("Failed: ${failResult.reason} at ${failResult.timestamp}")
        }
    }



    // ============================================
    //  STEP 4: data object vs data class
    // ============================================
    //
    //  'data object Loading' = a SINGLETON.
    //     There's only ever ONE Loading instance.
    //     Makes sense: "Loading" doesn't need data.
    //     You don't need Loading(progress=50%) — just "it's loading."
    //
    //  'data class Success(val items: List<String>)' = holds DATA.
    //     Each Success has different data.
    //     Success(["A"]) is different from Success(["B"]).
    //
    //  Rule of thumb:
    //    No data needed? → data object (singleton)
    //    Data needed?    → data class
}


// ============================================
//  SEALED INTERFACE EXAMPLE
// ============================================
//
//  sealed interface UiState
//  ^^^^^^
//  |
//  "Only the classes defined in THIS FILE can implement me."
//
//  Classes are defined INSIDE the interface — this is just organization.
//  They could also be defined outside (at the top level of the same file).

sealed interface UiState {
    data object Loading : UiState
    data class Success(val items: List<String>) : UiState
    data class Error(val message: String) : UiState
}

// Exhaustive when — try removing one case and see the compiler error!
fun handleState(state: UiState) {
    when (state) {
        is UiState.Loading -> println("⏳ Loading...")
        is UiState.Success -> println("✅ Showing ${state.items.size} items")
        is UiState.Error -> println("❌ Error: ${state.message}")
    }
}


// ============================================
//  SEALED CLASS EXAMPLE (With Shared State)
// ============================================
//
//  sealed class TimestampedResult(val timestamp: Long)
//                                 ^^^^^^^^^^^^^^
//                                 |
//              SHARED property — both Ok and Failed have this!
//
//  Ok gets timestamp from parent. Failed gets timestamp from parent.
//  You don't need to add it to each child manually.

sealed class TimestampedResult(val timestamp: Long = System.currentTimeMillis()) {
    data class Ok(val data: String) : TimestampedResult()
    data class Failed(val reason: String) : TimestampedResult()
}

fun fetchData(success: Boolean): TimestampedResult {
    return if (success) {
        TimestampedResult.Ok("Here is your data!")
    } else {
        TimestampedResult.Failed("Something went wrong")
    }
}


// ============================================
//  "BUT WAIT..." — COMMON QUESTIONS
// ============================================
//
//  Q: "Sealed CLASS vs sealed INTERFACE — when to use which?"
//
//  A: Quick table:
//
//     | Feature                  | sealed class   | sealed interface |
//     |--------------------------|----------------|------------------|
//     | Shared state (val)?      | ✅ Yes         | ❌ No            |
//     | Constructor parameters?  | ✅ Yes         | ❌ No            |
//     | Multiple inheritance?    | ❌ Single only | ✅ Multiple OK   |
//
//     Most of the time: use sealed interface.
//     Only use sealed class when children need shared properties.
//
//
//  Q: "Success is defined INSIDE UiState. If I inherit from UiState,
//      do I also get Success and Error inside me?"
//
//  A: NO! This is a very common misconception.
//
//     Nested classes are NOT inherited. They are NOT members.
//     They are just ORGANIZED under the parent name.
//
//     Think of it like FOLDERS:
//       📁 UiState
//          📄 Loading
//          📄 Success
//          📄 Error
//
//     Loading is a FILE inside the UiState FOLDER.
//     It doesn't contain another UiState folder inside it!
//
//     Only METHODS and PROPERTIES are inherited.
//     Nested classes? No. They just live there for organization.
//
//
//  Q: "Can sealed types be in different files?"
//
//  A: All subclasses must be in the SAME FILE (or same package for
//     sealed interfaces in Kotlin 1.5+). This is what makes them
//     "sealed" — the compiler can see ALL of them at once.
//
//
//  Q: "What's the difference between sealed and enum?"
//
//  A: Enum: each value is a SINGLETON with the same structure.
//       enum class Color { RED, GREEN, BLUE }
//       All have the same fields. No unique data per value.
//
//     Sealed: each type can have DIFFERENT data.
//       Success has 'items', Error has 'message', Loading has nothing.
//       They're related but structurally different.
//
//
//  NEXT STEP: See ../generics/Variance.kt for the Result<out T> pattern —
//  sealed interfaces + generics + Nothing type working together.
