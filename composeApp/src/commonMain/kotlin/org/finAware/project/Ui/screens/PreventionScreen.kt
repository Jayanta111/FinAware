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
fun PreventionScreen(
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
            Text("Loading prevention tips...")
        }
    } else {
        Column(Modifier.padding(16.dp)) {
            // Dynamic heading based on selected language
            Text(
                text = when (selectedLanguage) {
                    "en" -> "Prevention Tips for ${entry!!.title}"
                    "hi" -> "${entry!!.title} के लिए रोकथाम युक्तियाँ"
                    "pn" -> "${entry!!.title} ਲਈ ਰੋਕਥਾਮ ਦੇ ਟਿੱਪਸ"
                    "As" -> "${entry!!.title} ৰ বাবে ৰোধ ব্যৱস্থা"
                    else -> "Prevention Tips"
                },
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(8.dp))

            // Display localized prevention content
            when (selectedLanguage) {
                "en", "hi", "pn", "As" -> {
                    Text(text = entry!!.prevention, style = MaterialTheme.typography.bodyLarge)
                }
                else -> {
                    Text("No prevention content available for the selected language.")
                }
            }
        }
    }
}
