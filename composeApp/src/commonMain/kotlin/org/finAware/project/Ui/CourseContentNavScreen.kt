import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import io.ktor.client.*
import org.finAware.project.Ui.screens.*

@Composable
fun CourseContentNavScreen(
    courseId: String,
    selectedLanguage: String = "en",  // Add default language
    navController: NavHostController,
    client: HttpClient? = null
) {
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

    val navBackStackEntry by localNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
            composable("intro") {
                IntroScreen(courseId = courseId, selectedLanguage = selectedLanguage, client = client)
            }
            composable("examples") {
                ExamplesScreen(courseId = courseId, selectedLanguage = selectedLanguage, client = client)
            }
            composable("prevention") {
                PreventionScreen(courseId = courseId, selectedLanguage = selectedLanguage, client = client)
            }
            composable("quiz") {
                QuizScreen(courseId = courseId, selectedLanguage = selectedLanguage, client = client)
            }
        }
    }
}
