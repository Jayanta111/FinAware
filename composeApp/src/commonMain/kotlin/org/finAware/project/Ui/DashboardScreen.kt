package org.finAware.project.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.finAware.project.Ui.BottomNavBar

@Composable
fun DashboardScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                Text("FinAware", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.height(20.dp))

                // Row of StatCards
                Column {
                    StatCard("Current XP", "1250", "XP earned this month")
                    StatCard("Fraud Blocked", "8", "Cases blocked this week")
                    StatCard("Leaderboard", "#3", "Your current rank")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Recent Activity", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                RecentActivityList(
                    activities = listOf(
                        "Completed Module: Budgeting Basics",
                        "Blocked Fraud Alert: Phishing SMS",
                        "Earned Badge: Fraud Fighter",
                        "Completed Quiz: Safe Banking"
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
                Text("Progress", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                ProgressChart(progress = 0.75f)

                Spacer(modifier = Modifier.height(24.dp))
                Text("Personalized Tips", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Column {
                    TipCard("Enable 2FA", "Add an extra layer of security to your accounts", "Learn More")
                    TipCard("Review Transactions", "Check your statements regularly", "View Guide")
                }
            }
        }
    }
}


@Composable
fun StatCard(title: String, value: String, subtitle: String) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .heightIn(min = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f)
            )
        }
    }
}



@Composable
fun RecentActivityList(activities: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        activities.forEach { activity ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = activity,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ProgressChart(progress: Float) {
    Column {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "${(progress * 100).toInt()}% complete",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun TipCard(title: String, description: String, actionText: String) {
    Card(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Tip", style = MaterialTheme.typography.labelSmall)
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(description, style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = { /* TODO */ }) {
                Text(actionText)
            }
        }
    }
}

@Preview(
    name = "Dashboard Preview",
    showBackground = true,
    showSystemUi = true,
)

            @Composable
            fun DashboardScreenPreview() {
                val navController = rememberNavController()
                MaterialTheme {
                    DashboardScreen(navController = navController)
                }
    }

