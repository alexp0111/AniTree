package com.ikbo0621.anitree.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikbo0621.anitree.model.repository.UserRepository
import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for User
 * */
@HiltViewModel
class UserViewModel @Inject constructor(
    val repository: UserRepository
) : ViewModel() {
    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    private val _user = MutableLiveData<UiState<String>>()
    val user: LiveData<UiState<String>>
        get() = _user

    fun updateUserInfo(
        user: User
    ) {
        _user.value = UiState.Loading
        repository.updateUserInfoAndStore(
            user = user
        ) { _user.value = it }
    }


    fun register(
        email: String,
        password: String,
        user: User
    ) {
        _register.value = UiState.Loading
        repository.registerUser(
            email = email,
            password = password,
            user = user
        ) { _register.value = it }
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading
        repository.loginUser(
            email,
            password
        ) {
            _login.value = it
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email) {
            _forgotPassword.value = it
        }
    }

    fun logout(result: () -> Unit) {
        repository.logout(result)
    }

    fun getSession(result: (User?) -> Unit){
        repository.getSession(result)
    }

    fun getUserById(id: String, result: (User?) -> Unit){
        repository.getUserById(id, result)
    }
}