package com.hantash.coroutines.view.fragment.solution.exercise4

import com.hantash.coroutines.model.ThreadInfoLogger.logThreadInfo
import com.hantash.coroutines.view.fragment.exercise.GetReputationEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetReputationUseCase(
    private val getReputationEndpoint: GetReputationEndpoint
) {

    suspend fun getReputationForUser(userId: String): Int {
        return withContext(Dispatchers.Default) {
            logThreadInfo("getReputationForUser()")
            val reputation = getReputationEndpoint.getReputation(userId)
            reputation
        }
    }

}