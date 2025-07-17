package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val userName = user?.displayName ?: "No Name"
    val userEmail = user?.email ?: "No Email"
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProfileContent(
                name = userName,
                email = userEmail,
                onLogoutClick = {
                    auth.signOut()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar("Logged out successfully.")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileContent(name: String, email: String, onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("User Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Name: $name", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Text("Email: $email", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLogoutClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Logout", color = MaterialTheme.colorScheme.onError)
        }
    }
}

@Preview()
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileContent(
            name = "Preview User",
            email = "preview@example.com",
            onLogoutClick = {}
        )
    }
}
