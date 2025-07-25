package org.finAware.project.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizEntry(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

@Serializable
data class QuizPayload(
    val courseId: String,
    val title: String,
    val quiz: List<QuizEntry>
)


@Serializable
data class QuizResponse(
    val userUid: String,
    val email: String,
    val courseId: String,
    val score: Int,
    val xpEarned: Int,
    val totalQuestions: Int,
    val userAnswers: List<Int?>,
    val selectedLanguage: String
)
