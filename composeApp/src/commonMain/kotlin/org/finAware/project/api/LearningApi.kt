package org.finAware.project.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.finAware.project.model.LearningEntry


suspend fun fetchLearningEntries(client: HttpClient): List<LearningEntry> {
    return try {
        val response: HttpResponse = client.get("https://finaware-backend.onrender.com/content") {
            accept(ContentType.Application.Json)
        }

        if (response.status == HttpStatusCode.OK) {
            response.body()
        } else {
            println("❌ HTTP ${response.status}")
            println("Body: ${response.bodyAsText()}")
            emptyList()
        }
    } catch (e: Exception) {
        println("❌ Exception while fetching content: ${e.message}")
        emptyList()
    }
}