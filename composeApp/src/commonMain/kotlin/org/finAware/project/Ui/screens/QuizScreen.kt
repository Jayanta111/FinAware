package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
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
import org.finAware.project.model.QuizQuestion
import org.finAware.project.ui.getDummyQuizzes

@Composable
fun QuizScreen(courseId: String, selectedLanguage: String, client: HttpClient? = null) {
    val scope = rememberCoroutineScope()
    var quizTitle by remember { mutableStateOf("") }
    var quizItems: List<QuizQuestion> by remember { mutableStateOf(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var submitted by remember { mutableStateOf(false) }
    var userAnswers = remember { mutableStateListOf<Int?>() }
    var score by remember { mutableStateOf(0) }
    var xp by remember { mutableStateOf(0) }

    LaunchedEffect(courseId, selectedLanguage) {
        scope.launch {
            try {
                val actualClient = client ?: HttpClient()
                val response: HttpResponse =
                    actualClient.get("https://finaware-backend.onrender.com/quiz/$courseId")

                if (response.status == HttpStatusCode.OK) {
                    val payloadList: List<QuizPayload> = response.body()
                    if (payloadList.isNotEmpty()) {
                        val quiz = payloadList.first()
                        quizTitle = quiz.title
                        quizItems = quiz.questions
                    } else {
                        quizItems = getDummyQuizzes(courseId)
                        quizTitle = "Dummy Quiz"
                    }
                } else {
                    quizItems = getDummyQuizzes(courseId)
                    quizTitle = "Dummy Quiz"
                }
            } catch (e: Exception) {
                println("❌ Quiz fetch error: ${e.message}")
                quizItems = getDummyQuizzes(courseId)
                quizTitle = "Dummy Quiz"
            } finally {
                loading = false
                submitted = false
                userAnswers.clear()
                userAnswers.addAll(List(quizItems.size) { null })
            }
        }
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = when (selectedLanguage) {
                "en" -> "Quiz: $quizTitle"
                "hi" -> "प्रश्नोत्तरी: $quizTitle"
                "pn" -> "ਕੁਇਜ਼: $quizTitle"
                "As" -> "প্ৰশ্নোত্তৰ: $quizTitle"
                else -> "Quiz: $quizTitle"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        quizItems.forEachIndexed { index, item ->
            Text("${index + 1}. ${item.question}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))

            item.options.forEachIndexed { optIndex, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    RadioButton(
                        selected = userAnswers[index] == optIndex,
                        onClick = { if (!submitted) userAnswers[index] = optIndex },
                        enabled = !submitted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(option)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(
            onClick = {
                score = quizItems.countIndexed { i, item ->
                    userAnswers[i] == item.correctAnswerIndex
                }
                xp = score * 10
                submitted = true
            },
            enabled = !submitted && quizItems.isNotEmpty(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = when (selectedLanguage) {
                    "en" -> "Submit Quiz"
                    "hi" -> "प्रश्नोत्तरी सबमिट करें"
                    "pn" -> "ਕੁਇਜ਼ ਜਮ੍ਹਾਂ ਕਰੋ"
                    "As" -> "প্ৰশ্নোত্তৰ দাখিল কৰক"
                    else -> "Submit Quiz"
                }
            )
        }

        if (submitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = when (selectedLanguage) {
                    "en" -> "You scored $score out of ${quizItems.size}"
                    "hi" -> "आपने ${quizItems.size} में से $score अंक प्राप्त किए"
                    "pn" -> "ਤੁਸੀਂ ${quizItems.size} ਵਿੱਚੋਂ $score ਸਕੋਰ ਕੀਤਾ"
                    "As" -> "আপুনি ${quizItems.size} ৰ পৰা $score নম্বৰ লাভ কৰিছে"
                    else -> "You scored $score out of ${quizItems.size}"
                },
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "XP Earned: $xp",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// Extension function
inline fun <T> List<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    forEachIndexed { index, item -> if (predicate(index, item)) count++ }
    return count
}
