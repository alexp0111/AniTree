package com.ikbo0621.anitree.testUI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ikbo0621.anitree.R
import com.ikbo0621.anitree.databinding.FragmentRegistrationBinding
import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.*
import com.ikbo0621.anitree.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Registration fragment
 * that only holds registration functionality
 * */
@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val TAG: String = "REGISTRATION_FRAGMENT"
    lateinit var binding: FragmentRegistrationBinding
    val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        binding.btnRegister.setOnClickListener {
            if (validation()) {
                viewModel.register(
                    email = binding.etEmail.text.toString(),
                    password = binding.etPassword.text.toString(),
                    user = getUserObj()
                )
            }
        }
    }

    /**
     * Set up an observer for livedata
     * to get result of registration operation
     * */
    fun observer() {
        viewModel.register.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.btnRegister.text = "Loading"
                    binding.btnRegister.disable()
                    binding.pb.show()
                }
                is UiState.Failure -> {
                    binding.btnRegister.text = "Register"
                    binding.btnRegister.enabled()
                    binding.pb.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnRegister.text = "Register"
                    binding.btnRegister.enabled()
                    binding.pb.hide()
                    toast(state.data)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SearchFragment()).commit()
                }
            }
        }
    }

    /**
     * Set up new User object
     * */
    private fun getUserObj(): User {
        return User(
            id = "",
            iconId = "",
            name = binding.etName.text.toString(),
            favoriteTrees = arrayListOf(),
            createdTrees = arrayListOf()
        )
    }

    /**
     * Validation block for input data
     * */
    fun validation(): Boolean {
        var isValid = true

        if (binding.etName.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_first_name))
        }

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

}