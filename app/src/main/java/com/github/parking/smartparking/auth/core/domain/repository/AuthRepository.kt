package com.github.parking.smartparking.auth.core.domain.repository

import com.github.parking.smartparking.auth.core.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun getUserId(): Flow<String>
    val currentUserId:String?

    suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>>

    fun isCurrentUserExist(): Flow<Boolean>

    suspend fun getCurrentUserEmail(): Flow<String>
    suspend fun signOut()
}