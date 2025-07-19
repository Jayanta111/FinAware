package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExamplesScreen(courseId: String) {
    Column(Modifier.padding(16.dp)) {
        Text("Examples for $courseId", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Here are some real-world examples of the topic.")
    }
}