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

class Exercise1Fragment : Fragment() {
    private lateinit var binding: FragmentExercise1Binding
    private lateinit var getReputationEndpoint: GetReputationEndpoint

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
            binding.btnGetReputation.isEnabled = false
            getReputationForUser(binding.etUserId.text.toString())
            binding.btnGetReputation.isEnabled = true
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = Exercise1Fragment()
    }

    private fun getReputationForUser(userId: String) {
        logThreadInfo("getReputationForUser()")

        val reputation = getReputationEndpoint.getReputation(userId)

        Toast.makeText(requireContext(), "reputation: $reputation", Toast.LENGTH_SHORT).show()
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}