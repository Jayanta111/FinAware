package org.finAware.project.Ui.Navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.finAware.project.FinAwareHomeScreen
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.LearningCenterScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.authentication.AuthServiceImpl
import org.finAware.project.authentication.AuthViewModel
import org.finAware.project.authentication.LoginScreen
import org.finAware.project.authentication.SignUpScreen
import org.finAware.project.ui.DashboardScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Dashboard : Screen("DashboardScreen")
    data object Simulator : Screen("simulator")
    data object Learning : Screen("learning")
    data object Profile : Screen("ProfileScreen")
}

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

    NavHost(navController = navController, startDestination = Screen.Home.route) {

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
                onVerifyOtpClick = { /* OTP unused */ },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        composable(Screen.Simulator.route) {
            FraudSimulatorScreen(navController)
        }

        composable(Screen.Learning.route) {
            LearningCenterScreen(navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        learningGraph(navController)
    }
}
