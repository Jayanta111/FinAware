import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.finAware.project.Ui.screens.ExamplesScreen
import org.finAware.project.Ui.screens.IntroScreen
import org.finAware.project.Ui.screens.PreventionScreen
import org.finAware.project.Ui.screens.QuizScreen

@Composable
fun CourseContentNavScreen(courseId: String, navController: NavHostController) {
    val localNavController = rememberNavController()

    data class BottomNavItem(
        val title: String,
        val route: String,
        val icon: ImageVector
    )

    val items = listOf(
        BottomNavItem("Intro", "intro", Icons.Default.Info),
        BottomNavItem("Examples", "examples", Icons.Default.Star),
        BottomNavItem("Prevention", "prevention", Icons.Default.Shield),
        BottomNavItem("Quiz", "quiz", Icons.Default.CheckCircle)
    )

    val currentBackStackEntry = localNavController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.arguments?.getString("android-support-nav:controller:route")

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            localNavController.navigate(item.route) {
                                popUpTo(localNavController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = localNavController,
            startDestination = "intro",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("intro") { IntroScreen(courseId) }
            composable("examples") { ExamplesScreen(courseId) }
            composable("prevention") { PreventionScreen(courseId) }
            composable("quiz") { QuizScreen(courseId) }
        }
    }
}
