package org.finAware.project.Ui.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.ktor.client.HttpClient
import org.finAware.project.Ui.CourseDetailScreen
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.FraudTipsScreen
import org.finAware.project.Ui.FraudTypeSelectionScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.Ui.ResultEvaluatorScreen
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.model.FraudType
import org.finAware.project.model.LearningEntry
import org.finAware.project.ui.DashboardScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController,
               client: HttpClient,
               selectedLanguage: String,
               displayName: String,
               email: String
) {
    NavHost(navController = navController, startDestination = "dashboard") {

        // Dashboard screen
        composable("dashboard") {
            DashboardScreen(navController)
        }

        // Learning screen
        composable(
            "learning/{entryId}/{language}",
            arguments = listOf(
                navArgument("entryId") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId") ?: ""
            val language = backStackEntry.arguments?.getString("language") ?: selectedLanguage

            var entry by remember { mutableStateOf<LearningEntry?>(null) }

            LaunchedEffect(Unit) {
                val allEntries = fetchLearningEntries(client)
                entry = allEntries.find { it.courseId == entryId }
            }

            entry?.let {
                CourseDetailScreen(
                    navController = navController,
                    entry = it,
                    selectedLanguage = language
                )
            }
        }


        // Profile screen
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("simulator") {
            FraudTypeSelectionScreen(navController)
        }

        // Nested learning graph
        learningGraph(
            navController = navController,
            client = client,
            selectedLanguage = selectedLanguage,
            displayName = displayName,
            email = email
        )
        // Fraud Simulation screen with dynamic type
        composable("simulate/{type}") { backStack ->
            val typeArg = backStack.arguments?.getString("type") ?: "UPI_SCAM"
            val type = runCatching { FraudType.valueOf(typeArg) }.getOrElse { FraudType.UPI_SCAM }
            FraudSimulatorScreen(navController, type)
        }

        // Result evaluation screen
        composable("result/{score}/{total}") { backStack ->
            val score = backStack.arguments?.getString("score")?.toIntOrNull() ?: 0
            val total = backStack.arguments?.getString("total")?.toIntOrNull() ?: 0
            ResultEvaluatorScreen(navController, score, total)
        }

        // Tips screen
        composable("tips") {
            FraudTipsScreen(navController)
        }
    }
}
