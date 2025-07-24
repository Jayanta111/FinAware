package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import io.ktor.client.*
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.model.LearningEntry
import org.finAware.project.ui.getDummyCourses

// You can pass HttpClient or replace with dummy data if offline
@Composable
fun IntroScreen(
    courseId: String,
    selectedLanguage: String,
    client: HttpClient? = null
) {
    var entry by remember { mutableStateOf<LearningEntry?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(courseId, selectedLanguage) {
        scope.launch {
            try {
                val allEntries = client?.let { fetchLearningEntries(it) } ?: getDummyCourses()
                entry = allEntries.firstOrNull {
                    it.courseId.equals(courseId, ignoreCase = true) &&
                            it.language.equals(selectedLanguage, ignoreCase = true)
                }
            } catch (e: Exception) {
                entry = getDummyCourses().firstOrNull {
                    it.courseId.equals(courseId, ignoreCase = true) &&
                            it.language.equals(selectedLanguage, ignoreCase = true)
                }
            }
        }
    }

    if (entry == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = when (selectedLanguage.lowercase()) {
                    "en" -> "Introduction (English)"
                    "hi" -> "परिचय (हिंदी)"
                    "pn" -> "ਭੂਮਿਕਾ (ਪੰਜਾਬੀ)"
                    "as" -> "পৰিচয় (অসমীয়া)"
                    else -> "Introduction"
                },
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))

            Text(text = entry!!.intro, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
