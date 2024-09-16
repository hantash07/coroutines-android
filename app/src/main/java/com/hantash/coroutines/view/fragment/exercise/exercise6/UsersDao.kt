package com.techyourchance.coroutines.exercises.exercise8

import com.hantash.coroutines.model.ThreadInfoLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class UsersDao {

    suspend fun upsertUserInfo(user: User) = withContext(Dispatchers.IO) {
        ThreadInfoLogger.logThreadInfo("Adding or Updating User...")
        delay(1000)
    }
}