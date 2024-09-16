package com.hantash.coroutines.view.fragment.solution.exercise6

import com.hantash.coroutines.model.ThreadInfoLogger
import com.techyourchance.coroutines.exercises.exercise8.GetUserEndpoint
import com.techyourchance.coroutines.exercises.exercise8.UsersDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Exercise6SolutionFetchAndCacheUsersUseCase(
    private val getUserEndpoint: GetUserEndpoint,
    private val usersDao: UsersDao
) {

    suspend fun fetchAndCacheUsers(userIds: List<String>) = withContext(Dispatchers.Default) {
        for (userId in userIds) {
            //Parallel Running Coroutines
            //Structured Concurrency
            launch {
                val user = getUserEndpoint.getUser(userId)
                usersDao.upsertUserInfo(user)
                ThreadInfoLogger.logThreadInfo("fetchAndCacheUsers() => ${user.name}")
            }
        }
    }

}