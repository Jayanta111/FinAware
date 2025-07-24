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
fun ExamplesScreen(
    courseId: String,
    selectedLanguage: String,
    client: HttpClient
) {
    var entry by remember { mutableStateOf<LearningEntry?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(courseId, selectedLanguage) {
        scope.launch {
            try {
                val allEntries = fetchLearningEntries(client)
                entry = allEntries.firstOrNull {
                    it.courseId.equals(courseId, ignoreCase = true) &&
                            it.language.equals(selectedLanguage, ignoreCase = true)
                }
            } catch (_: Exception) {
                entry = null
            }
        }
    }

    if (entry == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text(
                text = when (selectedLanguage.lowercase()) {
                    "en" -> "Some examples of ${entry!!.title}"
                    "hi" -> "${entry!!.title} के कुछ उदाहरण"
                    "pn" -> "${entry!!.title} ਦੇ ਕੁਝ ਉਦਾਹਰਨ"
                    "as" -> "${entry!!.title} ৰ কিছুমান উদাহৰণ"
                    else -> "Some examples of ${entry!!.title}"
                },
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = entry!!.example,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
