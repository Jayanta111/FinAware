// androidMain/kotlin/org/finAware/project/authentication/AuthViewModel.kt
package org.finAware.project.authentication

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import org.finAware.project.firebase.UserRepository
import org.finAware.project.model.User
import java.util.concurrent.TimeUnit

class AuthViewModel(private val authService: AuthService) : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var verificationId: String? = null

    fun sendOtp(
        phone: String,
        activity: Activity,
        onSent: () -> Unit,
        onError: (String) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$phone")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e.message ?: "OTP verification failed.")
                }
                override fun onCodeSent(vid: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = vid
                    onSent()
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtpAndRegister(
        otp: String,
        fullName: String,
        email: String,
        phone: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val vid = verificationId
        if (vid == null) {
            onResult(false, "Verification ID is null. Resend OTP.")
            return
        }

        val credential = PhoneAuthProvider.getCredential(vid, otp)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                signUp(fullName, email, phone, password, onResult)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun signUp(
        fullName: String,
        email: String,
        phone: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        authService.signUp(email, password) { success ->
            if (success) {
                viewModelScope.launch {
                    val user = User(fullName, email, phone)
                    val result = UserRepository.saveUser(user)
                    if (result.isSuccess) {
                        onResult(true, null)
                    } else {
                        onResult(false, result.exceptionOrNull()?.message)
                    }
                }
            } else {
                onResult(false, "Firebase Auth signup failed.")
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {
        authService.login(email, password, onResult)
    }

    fun logout() = authService.logout()
    fun getCurrentUserEmail(): String? = authService.getCurrentUserEmail()

    class AuthViewModelFactory(private val service: AuthService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(service) as T
        }
    }
}
