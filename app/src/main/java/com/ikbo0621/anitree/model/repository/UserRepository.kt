package com.ikbo0621.anitree.model.repository

import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.UiState

/**
 * Repository, that holding operations connected to account and
 * user info in database
 * */
interface UserRepository {
    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: User, result: (UiState<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun storeSession(id: String, result: (User?) -> Unit)
    fun getSession(result: (User?) -> Unit)
}