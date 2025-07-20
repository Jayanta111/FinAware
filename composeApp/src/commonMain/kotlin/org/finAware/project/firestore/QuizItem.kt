package org.finAware.project.firestore

data class QuizItem(
    val id: String = "",
    val type: String = "", // "MCQ" or "SortAns"
    val question: String = "",
    val options: List<String>? = null, // Only for MCQ
    val answer: String = ""     // For both MCQ and SortAns(hints)
)