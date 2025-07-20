package org.finAware.project.firebase

import com.google.firebase.firestore.FirebaseFirestore
import org.finAware.project.model.User

fun saveUserToFirestore(uid: String, user: User) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users").document(uid).set(user)
}