package com.techyourchance.coroutines.exercises.exercise8

import com.hantash.coroutines.model.ThreadInfoLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class GetUserEndpoint {

    suspend fun getUser(userId: String): User = withContext(Dispatchers.IO) {
        ThreadInfoLogger.logThreadInfo("Fetching User...")
        delay(1000)
        return@withContext User(userId, "user ${userId}")
    }
}