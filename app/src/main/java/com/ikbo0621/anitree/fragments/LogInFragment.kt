package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentLogInBinding
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class LogInFragment : Fragment() {

    private val TAG: String = "LOG_IN_FRAGMENT"
    lateinit var binding: FragmentLogInBinding
    val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        observer()
        binding.btnLogIn.setOnClickListener {
            if (validation()) {
                /**
                 * Log in function call
                 * */
                viewModel.login(
                    email = binding.etEmail.text.toString() ,
                    password = binding.etPassword.text.toString()
                )
            }
        }
        binding.btnForgotPassword.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToForgotPasswordFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }
        binding.btnRegister.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToRegistrationFragment()
            Navigation.findNavController(requireView()).navigate(action)
        }

    }

    /**
     * Set up an observer for livedata
     * to get result of log in operation
     * */
    fun observer() {
        viewModel.login.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnLogIn.text = "Loading"
                    binding.btnLogIn.disable()
                    binding.pb.show()
                }
                is UiState.Failure -> {
                    binding.btnLogIn.text = "Log In"
                    binding.btnLogIn.enabled()
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnLogIn.text = "Log In"
                    binding.btnLogIn.enabled()
                    binding.pb.hide()
                    toast(state.data)

                    val action = LogInFragmentDirections.actionLogInFragmentToSearchFragment()
                    Navigation.findNavController(requireView()).navigate(action)

                }
            }
        }
    }

    /**
     * Validation block for input data
     * */
    private fun validation(): Boolean {
        var isValid = true

        if (binding.etEmail.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_email))
        } else {
            if (!binding.etEmail.text.toString().isValidEmail()) {
                isValid = false
                toast(getString(R.string.invalid_email))
            }
        }
        if (binding.etPassword.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_password))
        } else {
            if (binding.etPassword.text.toString().length < RegistrationParams.PASSWORD_LENGTH) {
                isValid = false
                toast(getString(R.string.invalid_password))
            }
        }
        return isValid
    }

    override fun onStart() {
        super.onStart()
        viewModel.getSession { user ->
            if (user != null) {

                val action = LogInFragmentDirections.actionLogInFragmentToSearchFragment()
                Navigation.findNavController(requireView()).navigate(action)
            }
        }
    }
}

