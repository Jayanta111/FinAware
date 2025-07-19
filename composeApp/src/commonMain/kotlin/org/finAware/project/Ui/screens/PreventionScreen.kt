package org.finAware.project.Ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PreventionScreen(courseId: String) {
    Column(Modifier.padding(16.dp)) {
        Text("Prevention Tips for $courseId", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text("Tips to avoid common mistakes or fraud.")
    }
}