package com.hantash.coroutines.view.fragment.exercise.exercise6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hantash.coroutines.databinding.FragmentExercise6Binding
import com.hantash.coroutines.model.ThreadInfoLogger.logThreadInfo
import com.techyourchance.coroutines.exercises.exercise8.FetchAndCacheUsersUseCase
import com.techyourchance.coroutines.exercises.exercise8.GetUserEndpoint
import com.techyourchance.coroutines.exercises.exercise8.UsersDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class Exercise6Fragment : Fragment() {
    private lateinit var binding: FragmentExercise6Binding
    private lateinit var fetchAndCacheUsersUseCase: FetchAndCacheUsersUseCase

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private val userIds = listOf<String>("bmq81", "gfn12", "gla34")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchAndCacheUsersUseCase = FetchAndCacheUsersUseCase(GetUserEndpoint(), UsersDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExercise6Binding.inflate(layoutInflater, container, false)

        binding.btnFetchUser.setOnClickListener {
            logThreadInfo("button callback")

            val updateElapsedTimeJob = coroutineScope.launch {
                updateElapsedTime()
            }

            coroutineScope.launch {
                try {
                    binding.btnFetchUser.isEnabled = false
                    fetchAndCacheUsersUseCase.fetchAndCacheUsers(userIds)
                    updateElapsedTimeJob.cancel()
                    logThreadInfo("Competed Fetching User")
                } catch (e: CancellationException) {
                    logThreadInfo("CancellationException()")
                    updateElapsedTimeJob.cancelAndJoin()
                    binding.tvElapsedTime.text = ""
                } finally {
                    binding.btnFetchUser.isEnabled = true
                }
            }
        }

        return binding.root
    }

    override fun onStop() {
        logThreadInfo("onStop()")
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    private suspend fun updateElapsedTime() {
        val startTimeNano = System.nanoTime()
        while (true) {
            delay(100)
            val elapsedTimeNano = System.nanoTime() - startTimeNano
            val elapsedTimeMs = elapsedTimeNano / 1000000
            binding.tvElapsedTime.text = "Elapsed time: $elapsedTimeMs ms"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = Exercise6Fragment()
    }
}