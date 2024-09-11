package com.hantash.coroutines.view.fragment.exercise.Exercise5

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Existing class which you can't change
 */
class PostBenchmarkResultsEndpoint {
    fun postBenchmarkResults(timeSeconds: Int, iterations: Long) {
        Thread.sleep(500)
    }
}