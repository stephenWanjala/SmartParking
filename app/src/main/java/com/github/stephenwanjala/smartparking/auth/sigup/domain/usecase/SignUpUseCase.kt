package com.github.stephenwanjala.smartparking.auth.sigup.domain.usecase

import android.util.Log
import com.github.stephenwanjala.smartparking.auth.core.domain.repository.AuthRepository
import com.github.stephenwanjala.smartparking.auth.core.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    private val TAG = "SavingUserName"
    private val db = Firebase.firestore
    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {

        return authRepository.signUpWithEmailAndPassword(email.trim(), password.trim())
            .onCompletion {
                createUser(username = userName)
            }

    }

    private fun createUser(username: String) {
        val user = hashMapOf(
            "username" to username
        )

        db.collection("users${authRepository.currentUserId}")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "User document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding user document", e)
            }


    }

}