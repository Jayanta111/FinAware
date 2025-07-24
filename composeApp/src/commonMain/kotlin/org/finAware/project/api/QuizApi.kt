// File: org.finAware.project.api.QuizApi.kt

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
import org.finAware.project.model.QuizResponse

val lenientQuizJson = Json {
    ignoreUnknownKeys = true
}

suspend fun fetchQuizzes(client: HttpClient): List<QuizResponse> {
    return try {
        val response: HttpResponse = client.get("https://finaware-backend.onrender.com/quiz") {
            accept(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            val responseBody = response.bodyAsText()
            println("✅ Quiz Response JSON: $responseBody") // Debug log

            lenientQuizJson.decodeFromString(ListSerializer(QuizResponse.serializer()), responseBody)
        } else {
            println("❌ HTTP ${response.status}")
            println("❌ Response body: ${response.bodyAsText()}")
            emptyList()
        }
    } catch (e: Exception) {
        println("❌ Exception while fetching quizzes: ${e.message}")
        emptyList()
    }
}
