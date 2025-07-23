package org.finAware.project.Ui.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.FraudTipsScreen
import org.finAware.project.Ui.FraudTypeSelectionScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.Ui.ResultEvaluatorScreen
import org.finAware.project.api.LearningViewModel
import org.finAware.project.model.FraudType
import org.finAware.project.ui.DashboardScreen
import org.finaware.project.ui.screens.LearningCenterScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dashboard") {

        // Dashboard screen
        composable("dashboard") {
            DashboardScreen(navController)
        }

        // Learning screen
        composable("learning") {
            LearningCenterScreen(navController)
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
            learningViewModel = LearningViewModel()
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
