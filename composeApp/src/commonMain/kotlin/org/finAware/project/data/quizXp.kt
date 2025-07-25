package org.finAware.project.data

import com.google.firebase.firestore.FirebaseFirestore

object FirebaseService {
    private val db = FirebaseFirestore.getInstance()

    fun saveXpToFirestore(userEmail: String, xpEarned: Int) {
        val userRef = db.collection("userXp").document(userEmail)

        userRef.get().addOnSuccessListener { document ->
            val previousXp = document.getLong("xp") ?: 0
            val updatedXp = previousXp + xpEarned

            userRef.set(mapOf("xp" to updatedXp))
                .addOnSuccessListener {
                    println("✅ XP saved to Firestore")
                }
                .addOnFailureListener {
                    println("❌ Failed to save XP to Firestore: ${it.message}")
                }
        }.addOnFailureListener {
            println("❌ Failed to fetch XP from Firestore: ${it.message}")
        }
    }
}
