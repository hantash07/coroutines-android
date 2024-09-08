package com.hantash.coroutines.view.fragment.demonstration.concurrency_cancellation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hantash.coroutines.databinding.FragmentUiThreadDemoBinding
import com.hantash.coroutines.model.ThreadInfoLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ConcurrencyCancellationDemoFragment : Fragment() {
    private lateinit var binding: FragmentUiThreadDemoBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private var jobCounter: Job? = null
    private var job: Job? = null

    private var hasBenchmarkBeenStartedOnce = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUiThreadDemoBinding.inflate(layoutInflater, container, false)

        binding.btnStart.setOnClickListener {
            logThreadInfo("button callback")

            val benchmarkDurationSeconds = 5

            jobCounter = coroutineScope.launch {
                updateRemainingTime(benchmarkDurationSeconds)
            }

            job = coroutineScope.launch {
                binding.btnStart.isEnabled = false
                val iterationsCount = executeBenchmark(benchmarkDurationSeconds) //Waiting for suspended function to execute
                Toast.makeText(requireContext(), "$iterationsCount", Toast.LENGTH_SHORT).show()
                binding.btnStart.isEnabled = true
            }

            hasBenchmarkBeenStartedOnce = true
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        logThreadInfo("onStop()")
        coroutineScope.coroutineContext.cancelChildren()
        if (hasBenchmarkBeenStartedOnce) {
            binding.btnStart.isEnabled = true
            binding.tvRemainingTime.text = "done!"
        }
    }

    private suspend fun executeBenchmark(benchmarkDurationSeconds: Int) = withContext(Dispatchers.Default) {
        logThreadInfo("benchmark started")
        val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            iterationsCount++
        }

        logThreadInfo("benchmark completed")

        iterationsCount
    }

    private suspend fun updateRemainingTime(remainingTimeSeconds: Int) {
        for (time in remainingTimeSeconds downTo 0) {
            if (time > 0) {
                logThreadInfo("updateRemainingTime: $time seconds")
                binding.tvRemainingTime.text = "$time seconds remaining"
                delay(1000)
            } else {
                binding.tvRemainingTime.text = "done!"
            }
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ConcurrencyCancellationDemoFragment()
    }
}