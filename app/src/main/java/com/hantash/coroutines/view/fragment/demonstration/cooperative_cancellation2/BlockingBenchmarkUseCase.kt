package com.hantash.coroutines.view.fragment.demonstration.cooperative_cancellation2

import com.hantash.coroutines.model.ThreadInfoLogger.logThreadInfo
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class BlockingBenchmarkUseCase {

    suspend fun executeBenchmark(benchmarkDurationSeconds: Int): Long {
        logThreadInfo("benchmark started")

        val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            coroutineContext.ensureActive() //It ensure coroutine is active otherwise throw a cancellable exception
            iterationsCount++
        }

        logThreadInfo("benchmark completed")

        return iterationsCount
    }

}