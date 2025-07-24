package org.finAware.project.Ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.*
import kotlinx.coroutines.launch
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.model.LearningEntry

@Composable
fun IntroScreen(
    courseId: String,
    selectedLanguage: String,
    client: HttpClient
) {
    var entry by remember { mutableStateOf<LearningEntry?>(null) }
    val scope = rememberCoroutineScope()
    val courseTitle = entry?.title

    LaunchedEffect(courseId, selectedLanguage) {
        scope.launch {
            try {
                val allEntries = fetchLearningEntries(client)
                entry = allEntries.firstOrNull {
                    it.courseId.equals(courseId, ignoreCase = true) &&
                            it.language.equals(selectedLanguage, ignoreCase = true)
                }
            } catch (e: Exception) {
                // Handle error (optional: show Toast or log)
            }
        }
    }

    if (entry == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            entry?.imageUrl?.let { imageUrl ->
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Course Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = when (selectedLanguage.lowercase()) {
                        "en" -> "Introduction to $courseTitle"
                        "hi" -> "$courseTitle का परिचय"
                        "pn" -> "$courseTitle ਦਾ ਪਰਚਿਆਵ"
                        "as" -> "$courseTitle ৰ পৰিচয়"
                        else -> "Introduction to $courseTitle"
                    },
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = entry!!.intro,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
