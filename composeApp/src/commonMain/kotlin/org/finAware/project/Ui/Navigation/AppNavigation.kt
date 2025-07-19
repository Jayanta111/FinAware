package org.finAware.project.Ui.Navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

// Sealed class for routes
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

        // 🚪 Home Screen (Welcome)
        composable(Screen.Home.route) {
            FinAwareHomeScreen(
                onGoogleSignInClick = {
                    onGoogleSignIn()
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        // 🔐 Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
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

        // 🆕 Sign Up Screen
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpClick = { name, email, phone, password ->
                    authViewModel.signUp(email, password) { isSuccess ->
                        if (isSuccess) {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                            }
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

        // 🏠 Dashboard (Main Bottom Nav)
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController)
        }

        // 🧠 Fraud Simulator
        composable(Screen.Simulator.route) {
            FraudSimulatorScreen(navController)
        }

        // 📚 Learning Center
        composable(Screen.Learning.route) {
            LearningCenterScreen(navController)
        }

        // 👤 Profile Screen
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        // 🧭 Inject nested learning navigation (Course Detail & Content)
        learningGraph(navController)
    }
}
