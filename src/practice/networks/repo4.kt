package practice.networks
//
//fun main() {
//    val api: UserApi = UserApiImpl()
//    val repository = UserRepository(api)
//
//    // First fetch - goes to API
//    println("--- First fetch ---")
//    val result1 = repository.getUser(1)
//    printResult(result1)
//
//    // Second fetch - should use cache!
//    println("--- Second fetch (cached) ---")
//    val result2 = repository.getUser(1)
//    printResult(result2)
//
//    // Fetch different user
//    println("--- Fetch user 2 ---")
//    val result3 = repository.getUser(2)
//    printResult(result3)
//
//    // Fetch different user
//    println("--- Second fetch (cached) ---")
//    val result4 = repository.getUser(2)
//    printResult(result4)
//
//    // Fetch different user
//    println("--- Fetch user 9 ---")
//    val result5 = repository.getUser(9)
//    printResult(result5)
//
//    // Check cache status
//    println("--- Cache status ---")
//    println("Cached users: ${repository.getCachedUserCount()}")
//}
//
//fun printResult(result: Result<User>) {
//    when (result) {
//        is Result.Success -> println("Got: ${result.data}")
//        is Result.Error -> println("Error: ${result.message}")
//    }
//}
//
//interface UserApi {
//    fun getUser(id: Int): Result<User>
//}
//
//class UserApiImpl() : UserApi {
//    val listOfUsers = listOf<User>(
//        User(id = 1, name = "Rishi", email = "rishi.com"),
//        User(id = 2, name = "Gar", email = "gar.com"),
//        User(id = 3, name = "vikas", email = "vikas.com"),
//        User(id = 4, name = "Romit", email = "romit.com"),
//        User(id = 5, name = "Shubham", email = "brazers.com")
//    )
//
//    override fun getUser(id: Int): Result<User> {
//        for (user in listOfUsers) {
//            if (user.id == id) {
//                return Result.Success(user)
//            }
//        }
//
//        return Result.Error("User Not Found")
//    }
//}
//
//class UserRepository(private val api: UserApi) {
//    private val cachedUsersInStorage = mutableMapOf<Int, User>()
//    fun getUser(id: Int): Result<User> {
//
//        cachedUsersInStorage[id]?.let { cachedUser ->
//            println("Using Cached User")
//            return Result.Success(cachedUser)
//        }
//
//        val result = api.getUser(id)
//
//        if (result is Result.Success) {
//            cachedUsersInStorage[id] = result.data
//            return result
//        }
//
//        return result
//    }
//
//    fun getCachedUserCount(): Int = cachedUsersInStorage.size
//}