package org.finAware.project.model

import kotlinx.serialization.Serializable

@Serializable
data class LearningEntry(
    val courseId: String,
    val title: String,
    val imageUrl: String?,
    val intro: String,
    val example: String,
    val prevention: String,
    val quiz: String,
    val language: String
)