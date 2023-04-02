package com.ikbo0621.anitree.model.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ikbo0621.anitree.model.repository.UserRepository
import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.UiState

class UserModel(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
) : UserRepository {
    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun logout(result: () -> Unit) {
        TODO("Not yet implemented")
    }
}