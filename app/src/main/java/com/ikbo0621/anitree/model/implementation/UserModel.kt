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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if (it == null) {
                            result.invoke(UiState.Failure("Failed to store local session"))
                        } else {
                            result.invoke(UiState.Success("Login successfully!"))
                        }
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Authentication failed, Check email and password"))
            }
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