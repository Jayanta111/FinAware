package org.finAware.project.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LearningEntry(
    @SerialName("_id")
    val ignoredId: String? = null,

    val courseId: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val intro: String = "",
    val example: String = "",
    val prevention: String = "",
    val quiz: String = "",
    val language: String = ""
)