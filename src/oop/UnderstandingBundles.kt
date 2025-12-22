package oop

/**
 * ============================================
 * KOTLIN CONCEPT: UNDERSTANDING BUNDLES (Android Context)
 * ============================================
 * 
 * WHAT IS A BUNDLE?
 * -----------------
 * In Android development, a Bundle is essentially a Map<String, Any> that's used
 * to pass data between components (Activities, Fragments, etc.). This example
 * demonstrates the concept using Kotlin's Map to help you understand how Bundles work.
 * 
 * WHY LEARN THIS?
 * --------------
 * Understanding Bundles helps you:
 * 1. Pass data between Android components
 * 2. Save/restore state
 * 3. Understand ContentResolver queries
 * 4. Work with Intent extras
 * 
 * KEY CONCEPT:
 * ------------
 * A Bundle is just a key-value store (like a Map) where:
 * - Keys are standardized strings (like "android:query-arg-sql-selection")
 * - Values can be various types (String, Int, Boolean, etc.)
 */

fun main() {
    // ============================================
    // SIMULATING ANDROID BUNDLE BEHAVIOR
    // ============================================
    // In Android, you'd use Bundle.putString(), Bundle.putInt(), etc.
    // Under the hood, it's just storing values in a Map-like structure
    
    // --- YOUR SIDE (THE APP) ---
    // You're building a query request to get files from the system
    
    // 1. The "Keys" (Standardized instruction labels)
    // These are constants that Android defines for ContentResolver queries
    // In real Android code, these would be like:
    //   ContentResolver.QUERY_ARG_SQL_SELECTION
    //   ContentResolver.QUERY_ARG_SQL_SORT_ORDER
    //   ContentResolver.QUERY_ARG_LIMIT
    val selectionKey = "android:query-arg-sql-selection"
    val sortOrderKey = "android:query-arg-sql-sort-order"
    val queryArgLimitKey = "android:query-arg-limit"
    
    // 2. The "Bundle" (It's just a Map<String, Any>!)
    // In Android: val bundle = Bundle()
    // Here we use a Map to demonstrate the concept
    val queryArgs = mutableMapOf<String, Any>()
    
    // 3. "putString" and "putInt" are just putting things in the map
    // In Android: bundle.putString(selectionKey, "mime_type IS NOT NULL")
    // Here: queryArgs[selectionKey] = "mime_type IS NOT NULL"
    queryArgs[selectionKey] = "mime_type IS NOT NULL"  // SQL WHERE clause
    queryArgs[sortOrderKey] = "date_modified DESC"     // SQL ORDER BY clause
    queryArgs[queryArgLimitKey] = 20                    // Limit results to 20
    
    // You have now built the "order form" (the Bundle with your query parameters)
    println("USER SENDING ORDER: $queryArgs")
    
    // --- THE OTHER SIDE (THE CONTENT RESOLVER) ---
    // The system component receives the Bundle and reads it using the SAME standard keys
    interceptData(queryArgs)
}

/**
 * SIMULATING CONTENT RESOLVER BEHAVIOR
 * -------------------------------------
 * This function simulates what Android's ContentResolver does internally:
 * 1. Receives a Bundle (Map) with query parameters
 * 2. Reads values using standardized keys
 * 3. Builds a SQL query from those parameters
 * 
 * HOW BUNDLES WORK IN ANDROID:
 * -----------------------------
 * 
 * 1. PUTTING DATA INTO BUNDLE:
 *    val bundle = Bundle()
 *    bundle.putString("key", "value")
 *    bundle.putInt("number", 42)
 * 
 * 2. GETTING DATA FROM BUNDLE:
 *    val value = bundle.getString("key")
 *    val number = bundle.getInt("number")
 * 
 * 3. PASSING BETWEEN COMPONENTS:
 *    intent.putExtra("myBundle", bundle)
 *    // In another Activity:
 *    val receivedBundle = intent.getBundleExtra("myBundle")
 * 
 * WHY USE STANDARDIZED KEYS?
 * ---------------------------
 * - Consistency: Everyone uses the same keys
 * - Type Safety: You know what type to expect
 * - Documentation: Keys are documented by Android
 * - Interoperability: Works across different Android versions
 */
fun interceptData(bundle: Map<String, Any>) {
    println("\n--- PIZZA SHOP RECEIVED ORDER (ContentResolver) ---")
    
    // The ContentResolver looks for the specific standardized keys it knows about
    // In real Android code, this would be:
    //   val selection = bundle.getString(ContentResolver.QUERY_ARG_SQL_SELECTION)
    val selectionKey = ""
    val specialInstructions = bundle[selectionKey] as String?
    val queryArgLimitKey = ""
    val howMany = bundle[queryArgLimitKey] as Int?
    
    println("Customer wants exactly: $howMany items")
    println("With special instructions: $specialInstructions")
    
    // Then it builds the real SQL query from these pieces
    // This is what Android does internally when you query ContentResolver
    val finalSQL = "SELECT * FROM files WHERE $specialInstructions ORDER BY date_modified DESC LIMIT $howMany"
    println("FINAL DATABASE QUERY: $finalSQL")
}

/**
 * REAL-WORLD ANDROID EXAMPLE:
 * ---------------------------
 * 
 * // Creating a Bundle in Android:
 * val bundle = Bundle().apply {
 *     putString("username", "john")
 *     putInt("age", 30)
 *     putBoolean("isActive", true)
 * }
 * 
 * // Passing to another Activity:
 * val intent = Intent(this, SecondActivity::class.java)
 * intent.putExtras(bundle)
 * startActivity(intent)
 * 
 * // Receiving in SecondActivity:
 * val username = intent.getStringExtra("username")
 * val age = intent.getIntExtra("age", 0)
 * 
 * KEY TAKEAWAYS:
 * --------------
 * 1. Bundle = Map<String, Any> with convenience methods
 * 2. Keys should be standardized constants
 * 3. Used extensively in Android for data passing
 * 4. Type safety is important (use the right getter method)
 */

