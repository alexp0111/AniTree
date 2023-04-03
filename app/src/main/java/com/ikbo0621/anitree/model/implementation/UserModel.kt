package com.ikbo0621.anitree.model.implementation

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ikbo0621.anitree.model.repository.UserRepository
import com.ikbo0621.anitree.structure.User
import com.ikbo0621.anitree.util.UiState

class UserModel(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    val appPreferences: SharedPreferences,
    val gson: Gson
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

    override fun storeSession(id: String, result: (User?) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun getSession(result: (User?) -> Unit) {
        TODO("Not yet implemented")
    }
}