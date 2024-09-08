package com.hantash.coroutines.view.fragment.exercise

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hantash.coroutines.R
import com.hantash.coroutines.databinding.FragmentExercise1Binding
import com.hantash.coroutines.model.ThreadInfoLogger
import com.hantash.coroutines.view.fragment.exercise.GetReputationEndpoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Exercise3Fragment : Fragment() {
    private lateinit var binding: FragmentExercise1Binding
    private lateinit var getReputationEndpoint: GetReputationEndpoint

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getReputationEndpoint = GetReputationEndpoint()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExercise1Binding.inflate(layoutInflater, container, false)

        binding.etUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnGetReputation.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnGetReputation.setOnClickListener {
            logThreadInfo("button callback")

            job = coroutineScope.launch {
                binding.btnGetReputation.isEnabled = false

                val reputation = getReputationForUser(binding.etUserId.text.toString())

                Toast.makeText(requireContext(), "reputation: $reputation", Toast.LENGTH_SHORT).show()
                binding.btnGetReputation.isEnabled = true
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        logThreadInfo("onStop()")
        job?.cancel()
        //Added this here because when coroutine is cancelled the code after the suspended function inside coroutineScope.launch{} will not execute
        binding.btnGetReputation.isEnabled = true
    }

    companion object {
        @JvmStatic
        fun newInstance() = Exercise3Fragment()
    }

    private suspend fun getReputationForUser(userId: String): Int {
        logThreadInfo("getReputationForUser()")
        return withContext(Dispatchers.Default) {
            val reputation = getReputationEndpoint.getReputation(userId)
            reputation
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}