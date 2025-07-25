package org.finAware.project.Ui.Navigation

import CourseContentNavScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.ktor.client.*
import org.finAware.project.Ui.CourseDetailScreen
import org.finAware.project.model.LearningEntry
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.ui.LearningCenterScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.learningGraph(
    navController: NavHostController,
    client: HttpClient,
    selectedLanguage: String,
    displayName: String,
    email: String
) {
    navigation(
        startDestination = "learningHome",
        route = "learning_graph"
    ) {
        // 🔹 Learning Home Screen
        composable("learningHome") {
            LearningCenterScreen(
                navController = navController,
                client = client,
                selectedLanguage = selectedLanguage
            )
        }

        // 🔹 Course Detail Screen
        composable("course_detail/{courseId}/{language}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val language = backStackEntry.arguments?.getString("language") ?: "English"

            var entry by remember { mutableStateOf<LearningEntry?>(null) }
            var isLoading by remember { mutableStateOf(true) }

            LaunchedEffect(courseId, language) {
                isLoading = true
                try {
                    val fetched = fetchLearningEntries(client)
                    entry = fetched.firstOrNull {
                        it.courseId == courseId && it.language.equals(language, ignoreCase = true)
                    }
                } catch (e: Exception) {
                    entry = null
                }
                isLoading = false
            }

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Loading course...")
                    }
                }

                entry != null -> {
                    CourseDetailScreen(
                        navController = navController,
                        entry = entry!!,
                        selectedLanguage = language
                    )
                }

                else -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Course not found.")
                    }
                }
            }
        }

        // 🔹 Course Content Screen
        composable("courseContent/{courseId}/{language}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val selectedLanguage = backStackEntry.arguments?.getString("language") ?: "English"

            CourseContentNavScreen(
                navController = navController,
                courseId = courseId,
                selectedLanguage = selectedLanguage,
                displayName = displayName,
                email = email,
                client = client
            )
        }
    }
}
