package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.finAware.project.model.FraudType

@Composable
fun FraudSimulatorScreen(navController: NavController, fraudType: FraudType?) {
    var fraudType by remember { mutableStateOf(fraudType) }
    val steps = remember(fraudType) { fraudType?.let { getSimulationSteps(it) } }
    var currentStepIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = "learning")
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp)) {

            when {
                fraudType == null -> {
                    // Initial Selection UI
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            "Select a Fraud Simulation Type",
                            style = MaterialTheme.typography.headlineSmall
                        )

                        FraudType.values().forEach { type ->
                            Button(
                                onClick = { fraudType = type },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(type.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() })
                            }
                        }
                    }
                }

                showResult && steps != null -> {
                    ResultEvaluatorScreen(
                        navController = navController,
                        score = score,
                        total = steps.size
                    )
                }

                steps != null -> {
                    val currentStep = steps[currentStepIndex]

                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "Question ${currentStepIndex + 1} of ${steps.size}",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentStep.question,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        currentStep.options.forEachIndexed { index, option ->
                            Button(
                                onClick = {
                                    if (index == currentStep.correctAnswerIndex) score++
                                    if (currentStepIndex < steps.lastIndex) {
                                        currentStepIndex++
                                    } else {
                                        showResult = true
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            ) {
                                Text(option, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}
