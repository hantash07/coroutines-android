package com.hantash.coroutines.view.fragment.demonstration.basic_coroutine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hantash.coroutines.databinding.FragmentUiThreadDemoBinding
import com.hantash.coroutines.model.ThreadInfoLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class BasicCoroutineDemoFragment : Fragment() {
    private lateinit var binding: FragmentUiThreadDemoBinding
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUiThreadDemoBinding.inflate(layoutInflater, container, false)

        binding.btnStart.setOnClickListener {
            logThreadInfo("button callback")
            coroutineScope.launch {
                binding.btnStart.isEnabled = false
                val iterationsCount = executeBenchmark() //Waiting for suspended function to execute
                Toast.makeText(requireContext(), "$iterationsCount", Toast.LENGTH_SHORT).show()
                binding.btnStart.isEnabled = true
            }
        }

        return binding.root
    }

    private suspend fun executeBenchmark(): Long {
        val benchmarkDurationSeconds = 5

        updateRemainingTime(benchmarkDurationSeconds)

        return withContext(Dispatchers.Default) {
            logThreadInfo("benchmark started")
            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

            var iterationsCount: Long = 0
            while (System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("benchmark completed")

            iterationsCount
        }
    }

    private fun updateRemainingTime(remainingTimeSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingTimeSeconds seconds")

        if (remainingTimeSeconds > 0) {
            binding.tvRemainingTime.text = "$remainingTimeSeconds seconds remaining"
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            binding.tvRemainingTime.text = "done!"
        }

    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    companion object {
        @JvmStatic
        fun newInstance() = BasicCoroutineDemoFragment()
    }
}