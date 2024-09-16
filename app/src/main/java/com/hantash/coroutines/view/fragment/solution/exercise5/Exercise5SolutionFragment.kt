package com.hantash.coroutines.view.fragment.solution.exercise5

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hantash.coroutines.databinding.FragmentUiThreadDemoBinding
import com.hantash.coroutines.model.ThreadInfoLogger
import com.hantash.coroutines.view.fragment.exercise.exercise5.PostBenchmarkResultsEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class Exercise5SolutionFragment : Fragment() {
    private lateinit var binding: FragmentUiThreadDemoBinding
    private lateinit var benchmarkUseCase: Exercise6BenchmarkUseCase

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private var hasBenchmarkBeenStartedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        benchmarkUseCase = Exercise6BenchmarkUseCase(PostBenchmarkResultsEndpoint())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUiThreadDemoBinding.inflate(layoutInflater, container, false)

        binding.btnStart.setOnClickListener {
            logThreadInfo("button callback")

            val benchmarkDurationSeconds = 5

            coroutineScope.launch {
                updateRemainingTime(benchmarkDurationSeconds)
            }

            coroutineScope.launch {
                try {
                    binding.btnStart.isEnabled = false
                    val iterationsCount = benchmarkUseCase.executeBenchmark(benchmarkDurationSeconds)
                    Toast.makeText(requireContext(), "$iterationsCount", Toast.LENGTH_SHORT).show()
                    binding.btnStart.isEnabled = true
                } catch (e: CancellationException) {
                    binding.btnStart.isEnabled = true
                    binding.tvRemainingTime.text = "done!"
                    logThreadInfo("coroutine cancelled")
                }
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
        }
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
        fun newInstance() = Exercise5SolutionFragment()
    }
}