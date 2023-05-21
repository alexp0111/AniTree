package com.ikbo0621.anitree.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentForgotPasswordBinding
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment that takes care about sending
 * password-recovery message to user
 * */
@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private val TAG: String = "FORGOT_PASSWORD_FRAGMENT"
    lateinit var binding: FragmentForgotPasswordBinding
    val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnSendRecovery.setOnClickListener {
            if (validation()) {
                viewModel.forgotPassword(binding.etEmail.text.toString())
            }
        }
    }

    /**
     * Set up an observer for livedata
     * to get result of sending recovery message to user
     *
     * If result is successful -> goto LogInFragment
     * */
    private fun observer() {
        viewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnSendRecovery.text = "Loading"
                    binding.btnSendRecovery.disable()
                    binding.pb.show()
                }
                is UiState.Failure -> {
                    binding.btnSendRecovery.text = "Send me recovery code"
                    binding.btnSendRecovery.enabled()
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnSendRecovery.text = "Send me recovery code"
                    binding.btnSendRecovery.enabled()
                    binding.pb.hide()
                    toast(state.data)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, LogInFragment()).commit()
                }
            }
        }
    }

    /**
     * Validation block for email
     * */
    fun validation(): Boolean {
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

        return isValid
    }
}