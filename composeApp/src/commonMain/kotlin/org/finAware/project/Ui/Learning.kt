package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text

@Composable
fun LearningCenterScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("LEARNING CENTER", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))
        Text("Featured Courses", style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CourseCard(
                title = "Investing Basics",
                label = "New",
                description = "Understand the fundamentals of investing.",
                modifier = Modifier.weight(1f)
            )
            CourseCard(
                title = "Portfolio Management",
                label = "Popular",
                description = "Optimize and manage investment portfolios.",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(24.dp))
        Text("Continue Learning", style = MaterialTheme.typography.titleMedium)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ProgressItem("Real Estate Investment", 0.45f)
            ProgressItem("Stock Market", 0.20f)
        }

        Spacer(Modifier.height(24.dp))
        Text("Achievements", style = MaterialTheme.typography.titleMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
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

        Spacer(Modifier.weight(1f))
        BottomNavBar()
    }
}

@Composable
fun CourseCard(
    title: String,
    label: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
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
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
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
        Text("${(progress * 100).toInt()}% complete", style = MaterialTheme.typography.labelSmall)
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
        elevation = CardDefaults.cardElevation(4.dp)
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



@Preview(showBackground = true)
@Composable
fun PreviewLearningCenterScreen() {
    MaterialTheme {
        LearningCenterScreen()
    }
}
