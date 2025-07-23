package org.finAware.project.Ui.Navigation

import CourseContentNavScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.finAware.project.Ui.CourseDetailScreen
import org.finAware.project.api.LearningViewModel
import org.finAware.project.model.LearningEntry
import java.util.Map.entry

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.learningGraph(navController: NavHostController,
                                  learningViewModel: LearningViewModel
) {
    navigation(
        startDestination = "courseDetail/{courseId}",
        route = "learning_graph"
    ) {
        // Course Detail
        composable("courseDetail/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            val entriesState = learningViewModel.entries.collectAsState()
            val entry = entriesState.value.firstOrNull { it.courseId == courseId }

            if (entry != null) {
                CourseDetailScreen(navController, entry)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Course not found")
                }
            }
        }
        // Main Course Content
        composable("courseContent/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            CourseContentNavScreen(courseId = courseId, navController = navController)
        }
    }
}
