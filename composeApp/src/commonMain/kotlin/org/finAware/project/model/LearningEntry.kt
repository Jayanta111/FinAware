package org.finAware.project.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LearningEntry(
    @SerialName("_id")
    val ignoredId: String? = null, // Just here to avoid crash, not used

    val courseId: String,
    val title: String,
    val imageUrl: String?,
    val intro: String,
    val example: String,
    val prevention: String,
    val quiz: String,
    val language: String
)