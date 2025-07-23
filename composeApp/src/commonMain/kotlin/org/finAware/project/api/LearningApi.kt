package org.finAware.project.api

import android.os.Build
import androidx.annotation.RequiresApi
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.finAware.project.model.LearningEntry

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
suspend fun fetchLearningEntries(): List<LearningEntry> {
    return httpClient
        .get("https://finaware-backend.onrender.com/content")
        .body()
}
