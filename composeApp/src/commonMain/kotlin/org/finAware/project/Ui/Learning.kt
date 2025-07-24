package org.finAware.project.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.client.*
import org.finAware.project.Ui.BottomNavBar
import org.finAware.project.Ui.screens.CourseCard
import org.finAware.project.api.fetchLearningEntries
import org.finAware.project.model.LearningEntry

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LearningCenterScreen(
    navController: NavController,
    client: HttpClient,
    selectedLanguage: String
) {
    var language by remember { mutableStateOf(selectedLanguage) }
    var searchQuery by remember { mutableStateOf("") }

    val contentList = remember { mutableStateListOf<LearningEntry>() }
    var isLoading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf(false) }

    // 🔄 Fetch content from backend only once
    LaunchedEffect(Unit) {
        try {
            val fetched = fetchLearningEntries(client)
            contentList.clear()
            contentList.addAll(fetched)
            loadError = false
        } catch (e: Exception) {
            println("❌ Error fetching content: ${e.message}")
            loadError = true
        } finally {
            isLoading = false
        }
    }

    val filteredEntries by remember(contentList, language, searchQuery) {
        derivedStateOf {
            contentList.filter {
                it.language.contains(language, ignoreCase = true) &&
                        it.title.contains(searchQuery, ignoreCase = true)
            }
        }
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
                Spacer(modifier = Modifier.height(8.dp))
                Text("Learning Center", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("English", "हिन्दी", "ਪੰਜਾਬੀ", "অসমীয়া").forEach { lang ->
                        FilterChip(
                            selected = language == lang,
                            onClick = { language = lang },
                            label = { Text(lang) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search Courses") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            when {
                isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                loadError -> {
                    item {
                        Text(
                            text = "⚠️ Failed to load content. Please try again later.",
                            color = Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                filteredEntries.isEmpty() -> {
                    item {
                        Text(
                            text = "No matching courses found.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    items(filteredEntries) { entry ->
                        CourseCard(entry = entry) {
                            navController.navigate("course_detail/${entry.courseId}/${language}")
                        }
                    }
                }
            }
        }
    }
}
