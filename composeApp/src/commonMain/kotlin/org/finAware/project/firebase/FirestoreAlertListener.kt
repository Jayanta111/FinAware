package org.finAware.project.firebase

import androidx.wear.compose.material.dialog.Alert
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.serialization.Serializable

object FirestoreAlertListener {
    fun listenForUserAlerts(userId: String, onAlert: (Alert) -> Unit) {
        val firestore = Firebase.firestore
        firestore.collection("firewall_alerts")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error == null && snapshot != null && !snapshot.isEmpty) {
                    snapshot.documentChanges.forEach {
                        val alert = it.document.toObject(Alert::class.java)
                        onAlert(alert)
                    }
                }
            }
    }
}
@Serializable
data class Alert(
    val userId: String? = null,
    val type: String,
    val severity: String,
    val details: String,
    val timestamp: Long? = null
)
