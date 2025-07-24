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
import kotlinx.coroutines.launch
import org.finAware.project.Ui.CourseDetailScreen
import org.finAware.project.model.LearningEntry
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.ui.LearningCenterScreen
import org.finAware.project.ui.getDummyCourses

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.learningGraph(
    navController: NavHostController,
    client: HttpClient,
    selectedLanguage: String
) {
    navigation(
        startDestination = "learningHome",
        route = "learning_graph"
    ) {
        // 🔹 Learning Home Screen (Main Screen)
        composable("learningHome") {
            LearningCenterScreen(
                navController = navController,
                client = client,
                selectedLanguage = selectedLanguage
            )
        }
        composable("course_detail/{courseId}/{language}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val language = backStackEntry.arguments?.getString("language") ?: "English"

            var entry by remember { mutableStateOf<LearningEntry?>(null) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(courseId, language) {
                scope.launch {
                    try {
                        val fetched = fetchLearningEntries(client)
                        entry = fetched.firstOrNull {
                            it.courseId == courseId && it.language.equals(language, ignoreCase = true)
                        }

                        if (entry == null) {
                            entry = getDummyCourses().firstOrNull {
                                it.courseId == courseId && it.language.equals(language, ignoreCase = true)
                            }
                        }
                    } catch (e: Exception) {
                        entry = getDummyCourses().firstOrNull {
                            it.courseId == courseId && it.language.equals(language, ignoreCase = true)
                        }
                    }
                }
            }

            if (entry != null) {
                CourseDetailScreen(
                    navController = navController,
                    entry = entry!!,
                    selectedLanguage = language // ✅ Fixed here
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Course not found or loading...")
                }
            }
        }





        // 🔹 Course Content Screen
        composable("courseContent/{courseId}/{language}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val selectedLanguage = backStackEntry.arguments?.getString("language") ?: "en" // default
            CourseContentNavScreen(courseId = courseId, selectedLanguage = selectedLanguage, navController = navController)
        }
    }
}
