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

@Composable
fun PreventionScreen(
    courseId: String,
    selectedLanguage: String,
    client: HttpClient
) {
    var entry by remember { mutableStateOf<LearningEntry?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(courseId, selectedLanguage) {
        scope.launch {
            try {
                val data = fetchLearningEntries(client)
                entry = data.firstOrNull {
                    it.courseId == courseId && it.language == selectedLanguage
                }
            } catch (_: Exception) {
                entry = null
            }
        }
    }

    if (entry == null) {
        // Show loading centered
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Show content from top
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = when (selectedLanguage) {
                    "en" -> "Prevention Tips for ${entry!!.title}"
                    "hi" -> "${entry!!.title} के लिए रोकथाम युक्तियाँ"
                    "pn" -> "${entry!!.title} ਲਈ ਰੋਕਥਾਮ ਦੇ ਟਿੱਪਸ"
                    "as" -> "${entry!!.title} ৰ বাবে ৰোধ ব্যৱস্থা"
                    else -> "Prevention Tips for ${entry!!.title}"
                },
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = entry!!.prevention.toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
