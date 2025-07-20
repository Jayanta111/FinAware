package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

// Dummy questions (You can replace this with data from Firestore later)
fun getQuizForCourse(courseId: String): List<QuizQuestion> {
    return when (courseId) {
        "fraud-awareness" -> listOf(
            QuizQuestion("What should you do if someone asks for your OTP?", listOf("Share it", "Ignore it", "Report it", "Call them back"), 2),
            QuizQuestion("Which is a secure payment method?", listOf("Sharing UPI pin", "Cash on delivery", "Third-party links", "None"), 1)
        )
        else -> listOf(
            QuizQuestion("Default question for unknown course", listOf("Option A", "Option B", "Option C", "Option D"), 0)
        )
    }
}

@Composable
fun QuizScreen(courseId: String) {
    val quizQuestions = remember { getQuizForCourse(courseId) }
    val userAnswers = remember { mutableStateListOf<Int?>(*Array(quizQuestions.size) { null }) }
    var submitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var xp by remember { mutableStateOf(0) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Quiz for $courseId", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        quizQuestions.forEachIndexed { index, question ->
            Text(text = "${index + 1}. ${question.question}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))

            question.options.forEachIndexed { optIndex, option ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(
                        selected = userAnswers[index] == optIndex,
                        onClick = {
                            userAnswers[index] = optIndex
                        },
                        enabled = !submitted
                    )
                    Text(option)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Button(
            onClick = {
                score = quizQuestions.countIndexed { i, q ->
                    userAnswers[i] == q.correctAnswerIndex
                }
                xp = score * 10 // Award 10 XP per correct answer
                submitted = true
            },
            enabled = !submitted,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Submit Quiz")
        }

        if (submitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("You scored $score out of ${quizQuestions.size}", style = MaterialTheme.typography.bodyLarge)
            Text("XP Earned: $xp", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// Helper extension for counting with index
inline fun <T> List<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    forEachIndexed { index, item ->
        if (predicate(index, item)) count++
    }
    return count
}
