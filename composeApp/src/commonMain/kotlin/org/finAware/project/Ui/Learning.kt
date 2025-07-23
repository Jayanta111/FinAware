package org.finaware.project.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import org.finAware.project.Ui.BottomNavBar
import org.finAware.project.api.LearningViewModel
import org.finAware.project.model.LearningEntry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LearningCenterScreen(
    navController: NavController,
) {
    val viewModel = viewModel<LearningViewModel>()
    val entries by viewModel.entries.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedLanguage by remember { mutableStateOf("English") }

    val filteredEntries = entries.filter { entry ->
        val matchesLanguage = entry.language?.contains(selectedLanguage, ignoreCase = true) ?: true
        val matchesSearch = entry.title.contains(searchQuery, ignoreCase = true)
        matchesLanguage && matchesSearch
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "learning")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 64.dp)
        ) {
            item {
                Text("Learning Center", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("English", "Hindi", "Punjabi").forEach { lang ->
                        FilterChip(
                            selected = selectedLanguage == lang,
                            onClick = { selectedLanguage = lang },
                            label = { Text(lang) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Courses") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Text("Courses", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(filteredEntries) { entry ->
                CourseCard(entry = entry, onStartCourse = {
                    // Dynamic navigation to detailed course page with courseId
                    navController.navigate("course_detail/${entry.courseId}")
                })
            }
        }
    }
}
@Composable
fun CourseCard(entry: LearningEntry, onStartCourse: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (!entry.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = entry.imageUrl,
                    contentDescription = entry.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = entry.intro,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onStartCourse,
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Start Course")
            }
        }
    }
}

