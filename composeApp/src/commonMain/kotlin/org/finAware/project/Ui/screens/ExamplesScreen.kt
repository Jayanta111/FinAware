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

@Composable
fun ExamplesScreen(
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
                entry = allEntries.firstOrNull { it.courseId == courseId && it.language == selectedLanguage }
            } catch (e: Exception) {
                entry = getDummyCourses().firstOrNull { it.courseId == courseId && it.language == selectedLanguage }
            }
        }
    }

    if (entry == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = when (selectedLanguage) {
                    "en" -> "Examples (English)"
                    "hi" -> "उदाहरण (हिंदी)"
                    "pn" -> "ਉਦਾਹਰਨਾਂ (ਪੰਜਾਬੀ)"
                    "as" -> "উদাহৰণ (অসমীয়া)"
                    else -> "Examples"
                },
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))

            when (selectedLanguage) {
                "en", "hi", "pn", "as" -> Text(text = entry!!.example, style = MaterialTheme.typography.bodyLarge)
                else -> Text("No examples available for selected language.")
            }
        }
    }
}
