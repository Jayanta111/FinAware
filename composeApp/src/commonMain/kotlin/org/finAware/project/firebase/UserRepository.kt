package org.finAware.project.firebase

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import org.finAware.project.model.User

object UserRepository {
    private val db = Firebase.firestore

    suspend fun saveUser(user: User): Result<Unit> {
        return try {
            db.collection("users").add(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}