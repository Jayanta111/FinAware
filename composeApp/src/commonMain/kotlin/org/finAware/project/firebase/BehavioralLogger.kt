package org.finAware.project.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object BehavioralLogger {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun logTypingSpeed(speed: Double) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf("typingSpeed" to speed)
        firestore.collection("behavioral_logs").document(uid)
            .set(data, SetOptions.merge())
    }

    fun logSwipeGesture(gesture: String) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf("lastSwipe" to gesture)
        firestore.collection("behavioral_logs").document(uid)
            .set(data, SetOptions.merge()) // Use set with merge for safety
    }

    fun logUsageTime(minutes: Int) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf("dailyUsage" to minutes)
        firestore.collection("behavioral_logs").document(uid)
            .set(data, SetOptions.merge())
    }
}