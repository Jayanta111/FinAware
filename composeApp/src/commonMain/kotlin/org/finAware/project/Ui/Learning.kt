package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningCenterScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Learning Center") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            SectionCard(title = "Featured Courses") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(featuredCourses) { course ->
                        CourseCard(
                            title = course.title,
                            label = course.label,
                            description = course.description,
                            modifier = Modifier.width(220.dp),
                            onStartClick = { navController.navigate("courseDetail/${course.route}") }
                        )
                    }
                }
            }

            SectionCard(title = "More Courses") {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(moreCourses) { course ->
                        CourseCard(
                            title = course.title,
                            label = course.label,
                            description = course.description,
                            modifier = Modifier.width(220.dp),
                            onStartClick = { navController.navigate("courseDetail/${course.route}") }
                        )
                    }
                }
            }

            SectionCard(title = "Continue Learning") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProgressItem("Real Estate Investment", 0.45f)
                    ProgressItem("Stock Market", 0.20f)
                }
            }

            SectionCard(title = "Achievements") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AchievementCard(
                        title = "Investor Pro",
                        subtitle = "Completed 3 courses",
                        modifier = Modifier.weight(1f)
                    )
                    AchievementCard(
                        title = "++200",
                        subtitle = "Earned from learning",
                        isXP = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// --------------------- Reusable Components ---------------------

@Composable
fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun CourseCard(
    title: String,
    label: String,
    description: String,
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = description, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onStartClick, modifier = Modifier.fillMaxWidth()) {
                Text("Start Course")
            }
        }
    }
}

@Composable
fun ProgressItem(title: String, progress: Float) {
    Column {
        Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis)
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
        Text(
            "${(progress * 100).toInt()}% complete",
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun AchievementCard(
    title: String,
    subtitle: String,
    isXP: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = if (isXP) "XP" else "Badge", style = MaterialTheme.typography.labelSmall)
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

// --------------------- Data Models ---------------------

data class Course(
    val title: String,
    val label: String,
    val description: String,
    val route: String
)

val featuredCourses = listOf(
    Course("Investing Basics", "New", "Understand the fundamentals of investing.", "investing_basics"),
    Course("Portfolio Management", "Popular", "Optimize and manage investment portfolios.", "portfolio_management"),
    Course("Ponzi schemes", "Frauds", "A Ponzi scheme is a fraudulent investment operation.", "Ponzi"),
    Course("Lottery/sweepstakes fraud", "Frauds", "They want you to think you've won a government-supervised lottery or sweepstake.", "Lottery")
)

val moreCourses = listOf(
    Course("Ponzi schemes", "Frauds", "A Ponzi scheme is a fraudulent investment operation.", "Ponzi"),
    Course("Lottery/sweepstakes fraud", "Frauds", "They want you to think you've won a government-supervised lottery or sweepstake.", "Lottery")
)
