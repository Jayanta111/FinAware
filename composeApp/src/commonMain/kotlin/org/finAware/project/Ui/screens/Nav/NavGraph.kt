package org.finAware.project.Ui.Navigation

import CourseContentNavScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import org.finAware.project.Ui.CourseDetailScreen

fun NavGraphBuilder.learningGraph(navController: NavHostController) {
    navigation(
        startDestination = "courseDetail/{courseId}",
        route = "learning_graph"
    ) {
        // Course Detail
        composable("courseDetail/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            CourseDetailScreen(navController = navController, courseId = courseId)
        }

        // Main Course Content
        composable("courseContent/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: ""
            CourseContentNavScreen(courseId = courseId, navController = navController)
        }
    }
}
