package com.hantash.coroutines.view.fragment.solution.exercise5

import com.hantash.coroutines.model.ThreadInfoLogger.logThreadInfo
import com.hantash.coroutines.view.fragment.exercise.exercise5.PostBenchmarkResultsEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class Exercise6BenchmarkUseCase(private val postBenchmarkResultsEndpoint: PostBenchmarkResultsEndpoint) {

    suspend fun executeBenchmark(benchmarkDurationSeconds: Int) = withContext(Dispatchers.Default) {
        logThreadInfo("benchmark started")

        val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            ensureActive() //It ensure coroutine is active otherwise throw a cancellable exception
            iterationsCount++
        }

        logThreadInfo("benchmark completed")

        postBenchmarkResultsEndpoint.postBenchmarkResults(benchmarkDurationSeconds, iterationsCount)

        logThreadInfo("benchmark results posted to the server")

        iterationsCount
    }

}