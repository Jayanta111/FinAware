package org.finAware.project.firestore

data class RelatedContent(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val lastUpdated: com.google.firebase.Timestamp? = null,
    val relatedLinks: List<String>?= null,

)
