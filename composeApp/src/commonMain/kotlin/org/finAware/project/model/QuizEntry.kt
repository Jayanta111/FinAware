package org.finAware.project.model

import kotlinx.serialization.Serializable

@Serializable
// QuizPayload.kt
data class QuizPayload(
    val title: String,
    val questions: List<QuizQuestion>
)
@Serializable

// QuizQuestion.kt
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)