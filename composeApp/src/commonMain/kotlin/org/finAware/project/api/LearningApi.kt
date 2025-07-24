package org.finAware.project.api

import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.finAware.project.model.LearningEntry

val lenientJson = Json {
    ignoreUnknownKeys = true
}
suspend fun fetchLearningEntries(client: HttpClient): List<LearningEntry> {
    return try {
        val response: HttpResponse = client.get("https://finaware-backend.onrender.com/content") {
            accept(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            val responseBody = response.bodyAsText()
            println("✅ Response JSON: $responseBody") // Debug log

            lenientJson.decodeFromString(ListSerializer(LearningEntry.serializer()), responseBody)
        } else {
            println("❌ HTTP ${response.status}")
            println("❌ Response body: ${response.bodyAsText()}")
            emptyList()
        }
    } catch (e: Exception) {
        println("❌ Exception while fetching content: ${e.message}")
        emptyList()
    }
}
