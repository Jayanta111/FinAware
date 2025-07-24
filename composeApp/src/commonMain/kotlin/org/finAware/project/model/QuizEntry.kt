package org.finAware.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

@Serializable
data class QuizPayload(
    val courseId: String,
    val title: String,
    val questions: List<QuizQuestion>
)

@Serializable
data class QuizResponse(
    @SerialName("_id")
    val id: String? = null,
    val courseId: String,
    val title: String,
    val questions: List<QuizQuestion>,
    val createdAt: String? = null
)
