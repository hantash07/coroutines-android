package com.techyourchance.coroutines.exercises.exercise8

import com.hantash.coroutines.model.ThreadInfoLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FetchAndCacheUsersUseCase(
        private val getUserEndpoint: GetUserEndpoint,
        private val usersDao: UsersDao
) {

    suspend fun fetchAndCacheUsers(userIds: List<String>) = withContext(Dispatchers.Default) {
        for (userId in userIds) {
            val user = getUserEndpoint.getUser(userId)
            usersDao.upsertUserInfo(user)
            ThreadInfoLogger.logThreadInfo("fetchAndCacheUsers() => ${user.name}")
        }
    }

}