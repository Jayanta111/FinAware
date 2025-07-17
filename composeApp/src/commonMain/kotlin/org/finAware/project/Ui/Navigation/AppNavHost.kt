package org.finAware.project.Ui.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.LearningCenterScreen
import org.finAware.project.ui.DashboardScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen() }
        composable("simulator") { FraudSimulatorScreen() }
        composable("learning") { LearningCenterScreen() }
        composable("profile") { ProfileScreen() }
    }
}

@Composable
fun ProfileScreen() {
    TODO("Not yet implemented")
}

