package org.finAware.project.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.finAware.project.model.QuizPayload
import org.finAware.project.model.QuizResponse
import org.finAware.project.data.FirebaseService // ✅ import your Firestore service

@Composable
fun QuizScreen(
    client: HttpClient,
    courseId: String,
    selectedLanguage: String,
    displayName: String,
    email: String,
    onBackClick: () -> Unit
) {
    var currentQuiz by remember { mutableStateOf<QuizPayload?>(null) }
    var userAnswers by remember { mutableStateOf<List<Int?>>(emptyList()) }
    var submitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var xp by remember { mutableStateOf(0) }

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    // Fetch quiz
    LaunchedEffect(courseId, selectedLanguage) {
        try {
            val response: HttpResponse = client.get("https://finaware-backend.onrender.com/quiz") {
                parameter("courseId", courseId)
                parameter("language", selectedLanguage)
            }
            if (response.status == HttpStatusCode.OK) {
                val fetchedQuiz: QuizPayload = response.body()
                currentQuiz = fetchedQuiz
                userAnswers = List(fetchedQuiz.quiz.size) { null }
            }
        } catch (e: Exception) {
            println("❌ Error fetching quiz: ${e.message}")
        }
    }

    currentQuiz?.let { quizPayload ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Quiz: ${quizPayload.title}", style = MaterialTheme.typography.headlineMedium)

            quizPayload.quiz.forEachIndexed { index, item ->
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${index + 1}. ${item.question}", style = MaterialTheme.typography.bodyLarge)
                    item.options.forEachIndexed { optionIndex, option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            RadioButton(
                                selected = userAnswers[index] == optionIndex,
                                onClick = {
                                    userAnswers = userAnswers.toMutableList().apply {
                                        this[index] = optionIndex
                                    }
                                },
                                enabled = !submitted
                            )
                            Text(option)
                        }
                    }
                }
            }

            if (!submitted) {
                Button(
                    onClick = {
                        score = quizPayload.quiz.countIndexed { i, item ->
                            userAnswers[i] == item.correctAnswerIndex
                        }
                        xp = score * 10
                        submitted = true

                        scope.launch {
                            // Step 1: Post result to backend
                            try {
                                val result = QuizResponse(
                                    userUid = displayName,
                                    email = email,
                                    courseId = courseId,
                                    score = score,
                                    xpEarned = xp,
                                    totalQuestions = quizPayload.quiz.size,
                                    userAnswers = userAnswers,
                                    selectedLanguage = selectedLanguage
                                )
                                val postResponse: HttpResponse = client.post("https://finaware-backend.onrender.com/quiz-result") {
                                    contentType(ContentType.Application.Json)
                                    setBody(result)
                                }

                                if (postResponse.status == HttpStatusCode.OK) {
                                    println("✅ Quiz result saved successfully.")
                                } else {
                                    println("⚠️ Failed to save result: ${postResponse.status}")
                                }
                            } catch (e: Exception) {
                                println("❌ Error saving quiz result: ${e.message}")
                            }

                            // Step 2: Save XP to Firebase Firestore
                            try {
                                FirebaseService.saveXpToFirestore(email, xp)
                            } catch (e: Exception) {
                                println("❌ Firestore XP Save Failed: ${e.message}")
                            }
                        }
                    }
                ) {
                    Text("Submit")
                }
            } else {
                Text("✅ You scored $score out of ${quizPayload.quiz.size}", style = MaterialTheme.typography.titleLarge)
                Text("⭐ XP Earned: $xp", style = MaterialTheme.typography.titleMedium)
                Button(onClick = onBackClick) {
                    Text("Back to Course")
                }
            }
        }
    } ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

inline fun <T> List<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    forEachIndexed { index, item ->
        if (predicate(index, item)) count++
    }
    return count
}
