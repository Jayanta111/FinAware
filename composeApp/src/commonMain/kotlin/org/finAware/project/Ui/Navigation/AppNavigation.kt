package org.finAware.project.Ui.Navigation

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.finAware.project.FinAwareHomeScreen
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.FraudTipsScreen
import org.finAware.project.Ui.FraudTypeSelectionScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.api.LearningViewModel
import org.finAware.project.authentication.AuthServiceImpl
import org.finAware.project.authentication.AuthViewModel
import org.finAware.project.authentication.LoginScreen
import org.finAware.project.authentication.SignUpScreen
import org.finAware.project.model.FraudType
import org.finAware.project.model.LearningEntry
import org.finAware.project.ui.DashboardScreen
import org.finaware.project.ui.screens.LearningCenterScreen
import org.finaware.project.ui.screens.LearningCenterScreen


sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Dashboard : Screen("DashboardScreen")
    data object SimulatorSelection : Screen("Simulator") // FraudTypeSelectionScreen
    data object Simulator : Screen("simulate/{type}") // FraudSimulatorScreen
    data object Learning : Screen("learning")
    data object Profile : Screen("ProfileScreen")
    data object Tips : Screen("tips")
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(
    onGoogleSignIn: () -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as ComponentActivity

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.AuthViewModelFactory(AuthServiceImpl(activity))
    )

    // Determine start screen based on auth status
    val isUserLoggedIn = remember { authViewModel.isUserLoggedIn() }
    val startDestination = if (isUserLoggedIn) Screen.Dashboard.route else Screen.Home.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Home.route) {
            FinAwareHomeScreen(
                onGoogleSignInClick = { onGoogleSignIn() },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = { onGoogleSignIn() },
                viewModel = authViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { name, email, phone, password ->
                    authViewModel.signUp(name, email, phone, password) { isSuccess, _ ->
                        if (isSuccess) {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        }
                    }
                },
                onVerifyOtpClick = { /* TODO */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(
            route = Screen.Simulator.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type")
            val fraudType = try {
                FraudType.valueOf(type ?: "")
            } catch (e: IllegalArgumentException) {
                null
            }

            if (fraudType != null) {
                FraudSimulatorScreen(
                    navController = navController,
                    fraudType = fraudType
                )
            } else {
                Text("Invalid simulation type: $type")
            }
        }

        composable(Screen.Learning.route) {
            LearningCenterScreen(navController)
        }

        composable(Screen.SimulatorSelection.route) {
            FraudTypeSelectionScreen(navController)
        }

        composable(Screen.Tips.route) {
            FraudTipsScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        learningGraph(
            navController = navController,
            learningViewModel = LearningViewModel()
        )    }
}

