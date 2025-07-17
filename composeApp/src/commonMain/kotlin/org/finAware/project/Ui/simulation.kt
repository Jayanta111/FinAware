package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun FraudSimulatorScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Fraud Detection Simulator",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            ScenarioSetup()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Simulation History",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            repeat(3) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = "Phishing Attempt - Blocked",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ScenarioSetup() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Scenario Setup",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Fraud Type (e.g., Phishing)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Success Probability") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {},
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Run Simulation")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FraudSimulatorScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        FraudSimulatorScreen(navController)
    }
}
