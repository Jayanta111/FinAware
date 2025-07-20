package org.finAware.project.authentication

import com.google.firebase.auth.FirebaseUser

interface AuthService {
    fun login(email: String, password: String, onResult: (Boolean) -> Unit)
    fun signUp(email: String, password: String, onResult: (Boolean) -> Unit)
    fun logout()
    fun getCurrentUserEmail(): String?
    fun getCurrentUser(): FirebaseUser?
}