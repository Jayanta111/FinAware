package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.finAware.project.firestore.FirestoreQuizItem

@Composable
fun QuizScreen(courseId: String) {
    val firestore = FirebaseFirestore.getInstance()
    var quizItems by remember { mutableStateOf<List<FirestoreQuizItem>>(emptyList()) }
    val userAnswers = remember { mutableStateListOf<Int?>(/* dynamically init below */) }
    var submitted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var xp by remember { mutableStateOf(0) }

    // Load quiz from Firestore
    LaunchedEffect(courseId) {
        val snapshot = firestore.collection("quiz")
            .whereEqualTo("courseId", courseId)
            .get()
            .await()

        quizItems = snapshot.documents.mapNotNull { it.toObject(FirestoreQuizItem::class.java) }

        // Init userAnswers based on the fetched size
        userAnswers.clear()
        userAnswers.addAll(List(quizItems.size) { null })
    }
    LaunchedEffect(Unit) {
        // Dummy quiz content
        quizItems = listOf(
            FirestoreQuizItem(
                question = "What is investing?",
                options = listOf("Spending", "Saving", "Growing money", "Donating"),
                answer = "Growing money",
                courseId = courseId
            ),
            FirestoreQuizItem(
                question = "Which of these is a common investment?",
                options = listOf("Shoes", "Stocks", "Fast food", "Bicycles"),
                answer = "Stocks",
                courseId = courseId
            ),
            FirestoreQuizItem(
                question = "What is a risk of investing?",
                options = listOf("Losing money", "Getting bored", "Learning too much", "Spending time"),
                answer = "Losing money",
                courseId = courseId
            )
        )

        // Initialize answers
        userAnswers.clear()
        userAnswers.addAll(List(quizItems.size) { null })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Quiz for $courseId", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        quizItems.forEachIndexed { index, item ->
            Text("${index + 1}. ${item.question}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(4.dp))

            item.options?.forEachIndexed { optIndex, option ->
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
                score = quizItems.countIndexed { i, item ->
                    val correctAnswerIndex = item.options?.indexOf(item.answer)
                    userAnswers[i] == correctAnswerIndex
                }
                xp = score * 10
                submitted = true
            },
            enabled = !submitted && quizItems.isNotEmpty(),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Submit Quiz")
        }

        if (submitted) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("You scored $score out of ${quizItems.size}", style = MaterialTheme.typography.bodyLarge)
            Text("XP Earned: $xp", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// Helper extension to count correct answers
inline fun <T> List<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    forEachIndexed { index, item ->
        if (predicate(index, item)) count++
    }
    return count
}
