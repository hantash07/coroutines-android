package com.hantash.coroutines.view.fragment.solution

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hantash.coroutines.databinding.FragmentLoginBinding
import com.hantash.coroutines.view.fragment.demonstration.uncaught_exception.LoggedInUser
import com.hantash.coroutines.view.fragment.demonstration.uncaught_exception.LoginEndpointUncaughtException
import com.hantash.coroutines.view.fragment.demonstration.uncaught_exception.LoginUseCaseUncaughtException
import com.hantash.coroutines.view.fragment.demonstration.uncaught_exception.LoginUseCaseUncaughtException.*
import com.hantash.coroutines.view.fragment.demonstration.uncaught_exception.UserStateManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

class Exercise7SolutionFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var loginUseCase: LoginUseCaseUncaughtException

    //Created CoroutineExceptionHandler to catch uncaught Exceptions so that app will not crash
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Toast.makeText(requireContext(), "Exception: $throwable", Toast.LENGTH_SHORT).show()
    }

    //Added SupervisorJob() so that if any of Coroutine child fails or other child will not cancelled
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate + coroutineExceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginUseCase = LoginUseCaseUncaughtException(LoginEndpointUncaughtException(), UserStateManager())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        refreshUiState()

        binding.etUsername.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshUiState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshUiState()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnLogin.setOnClickListener {
            coroutineScope.launch {
                try {
                    binding.btnLogin.isEnabled = false
                    val result = loginUseCase.logIn(getUsername(), getPassword())
                    when (result) {
                        is Result.Success -> onUserLoggedIn(result.user)
                        is Result.InvalidCredentials -> onInvalidCredentials()
                        is Result.GeneralError -> onGeneralError()
                    }
                } finally {
                    refreshUiState()
                }
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }

    private fun refreshUiState() {
        val username = getUsername()
        val password = getPassword()
        binding.btnLogin.isEnabled = username.isNotEmpty() && password.isNotEmpty()
    }

    private fun getUsername(): String {
        return binding.etUsername.text.toString()
    }

    private fun getPassword(): String {
        return binding.etPassword.text.toString()
    }

    private fun onUserLoggedIn(user: LoggedInUser) {
        Toast.makeText(requireContext(), "successful login", Toast.LENGTH_SHORT).show()
    }

    private fun onInvalidCredentials() {
        Toast.makeText(requireContext(), "invalid credentials", Toast.LENGTH_SHORT).show()
    }

    private fun onGeneralError() {
        Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = Exercise7SolutionFragment()
    }
}