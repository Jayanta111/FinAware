package org.finAware.project.firestore

data class CourseModule(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val lastUpdated: com.google.firebase.Timestamp? = null,
    val content: String = ""
)
