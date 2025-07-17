package org.finAware.project.Ui.Navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.finAware.project.FinAwareHomeScreen
import org.finAware.project.authentication.AuthServiceImpl
import org.finAware.project.authentication.AuthViewModel
import org.finAware.project.authentication.SignUpScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import org.finAware.project.Ui.FraudSimulatorScreen
import org.finAware.project.Ui.LearningCenterScreen
import org.finAware.project.Ui.ProfileScreen
import org.finAware.project.authentication.LoginScreen
import org.finAware.project.ui.DashboardScreen

// Sealed class for safe and clear navigation
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object DashboardScreen : Screen("DashboardScreen") // Main app screen
    data object Simulator : Screen("simulator")
    data object Learning : Screen("learning")
    data object Profile : Screen("ProfileScreen")
}
@Composable
fun AppNavigation(
    onGoogleSignIn: () -> Unit // Accept callback from MainActivity
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
                onGoogleSignInClick = {
                    onGoogleSignIn() // Launch Google Sign-In
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onGoogleSignInClick = {
                    onGoogleSignIn()
                },
                viewModel = authViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { name, email, phone, password ->
                    authViewModel.signUp(email, password) { isSuccess ->
                        if (isSuccess) {
                            navController.navigate(Screen.DashboardScreen.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
                        } else {
                            // Handle sign-up error
                        }
                    }
                },
                onVerifyOtpClick = { otp ->
                    // Future OTP logic
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.DashboardScreen.route) {
            DashboardScreen(navController)
        }
        composable(Screen.Simulator.route) { FraudSimulatorScreen(navController) }
        composable(Screen.Learning.route) { LearningCenterScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }


    }
}
