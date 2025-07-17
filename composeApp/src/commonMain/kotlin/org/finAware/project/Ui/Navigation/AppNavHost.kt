package org.finAware.project.Ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.LearningCenterScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.ui.DashboardScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen(navController) }
        composable("simulator") { FraudSimulatorScreen(navController) }
        composable("learning") { LearningCenterScreen(navController) }
        composable("profile") { ProfileScreen(navController) }

    }
}


