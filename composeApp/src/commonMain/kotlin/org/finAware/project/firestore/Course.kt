package org.finAware.project.firestore

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/*
courses (collection)
 └── courseId (document)
     ├── name
     ├── description
     ├── createdAt
     ├── updatedAt
     └── Modules (subcollection)
         └── moduleId (document)
             ├── title
             ├── description
             ├── lastUpdated
             ├── content
             └── relatedContent (subcollection)
                 └── contentId (document)
                     ├── content
                     ├── title
                     ├── lastUpdated
                     └── relatedLink
             └── Quiz (subcollection)
                 └── quizId (document)
                     ├── question
                     ├── options
                     └── answer
                     └── type

*/

data class Course(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val createdAt: com.google.firebase.Timestamp? = null,
    val updatedAt: com.google.firebase.Timestamp? = null
)

class CourseMethods {

    suspend fun fetchQuiz(
        courseId: String,
        moduleId: String
    ): List<QuizItem> {
        val firestore = Firebase.firestore
        val snapshot = firestore
            .collection("courses")
            .document(courseId)
            .collection("Modules")
            .document(moduleId)
            .collection("Quiz")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(QuizItem::class.java) }
    }

    suspend fun fetchRelatedContent(
        courseId: String,
        moduleId: String
    ): List<RelatedContent> {
        val firestore = Firebase.firestore
        val snapshot = firestore
            .collection("courses")
            .document(courseId)
            .collection("Modules")
            .document(moduleId)
            .collection("relatedContent")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(RelatedContent::class.java) }
    }
}

// example
// val quizItems = courseMethods.fetchQuiz("course123", "module456")
// val relatedItems = courseMethods.fetchRelatedContent("course123", "module456")