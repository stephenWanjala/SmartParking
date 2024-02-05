package com.github.stephenwanjala.smartparking.auth.core.data.repositoryImpl

import com.github.stephenwanjala.smartparking.auth.core.domain.repository.AuthRepository
import com.github.stephenwanjala.smartparking.auth.core.util.Resource
import com.github.stephenwanjala.smartparking.auth.core.util.UiText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun getUserId(): Flow<String> = flow {
        firebaseAuth.currentUser?.uid?.let {
            emit(it)
        }
    }


    override val currentUserId: String?
        get() =firebaseAuth.currentUser?.uid

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                )
            )
        }.catch {
            emit(Resource.Error(UiText.DynamicString(it.message ?: "Unknown Error")))
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            emit(Resource.Success(firebaseAuth.signInWithEmailAndPassword(email, password).await()))
        }.catch {
            emit(Resource.Error(UiText.DynamicString(it.message ?: "Unknown Error")))
        }

    }

    override fun isCurrentUserExist(): Flow<Boolean> = flow {
        emit(firebaseAuth.currentUser != null)
    }


    override suspend fun getCurrentUserEmail(): Flow<String> {
        return flow {
            firebaseAuth.currentUser?.email?.let {
                emit(it)
            }
        }
    }

    override suspend fun signOut() = firebaseAuth.signOut()
}