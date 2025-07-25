package org.finAware.project.util


import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.finAware.project.model.LearningEntry
import org.finAware.project.model.QuizEntry


object QuizListDeserializer : JsonTransformingSerializer<List<QuizEntry>>(ListSerializer(QuizEntry.serializer())) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when {
            element is JsonPrimitive && element.content == "" -> JsonArray(emptyList())
            element is JsonArray -> element
            else -> JsonArray(emptyList())
        }
    }
}
object JsonUtils {
    private val json = Json { ignoreUnknownKeys = true }

    fun fetchAllContent(jsonString: String): List<LearningEntry> {
        val jsonElements = json.parseToJsonElement(jsonString)

        if (jsonElements !is JsonArray) return emptyList()

        return jsonElements.filter { element ->
            val imageUrl = element.jsonObject["imageUrl"]
            imageUrl == null || (imageUrl is JsonPrimitive && imageUrl.isString)
        }.mapNotNull { element ->
            try {
                json.decodeFromJsonElement(LearningEntry.serializer(), element)
            } catch (e: Exception) {
                println("❌ Skipped invalid entry: ${e.message}")
                null
            }
        }
    }
}

