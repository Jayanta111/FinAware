package org.finAware.project.firestore

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