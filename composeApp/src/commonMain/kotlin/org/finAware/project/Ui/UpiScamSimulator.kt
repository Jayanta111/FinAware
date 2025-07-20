package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class SimulationResult(val score: Int)

@Composable
fun UpiScamSimulator(onSimulationComplete: (SimulationResult) -> Unit) {
    var step by remember { mutableStateOf(0) }
    var correctActions by remember { mutableStateOf(0) }

    val steps = listOf(
        "You receive a message: 'You've won ₹10,000! Click to claim via UPI.'",
        "The link opens an app asking UPI PIN to 'receive' money.",
        "What do you do?"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("UPI Scam Simulation", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = steps[step], fontSize = 16.sp)
        Spacer(modifier = Modifier.height(20.dp))

        when (step) {
            0 -> {
                Button(onClick = { step++ }) {
                    Text("Next")
                }
            }
            1 -> {
                Button(onClick = { step++ }) {
                    Text("Next")
                }
            }
            2 -> {
                Button(
                    onClick = {
                        correctActions++
                        onSimulationComplete(SimulationResult(score = correctActions * 10))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Close the App")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        onSimulationComplete(SimulationResult(score = correctActions * 10))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Enter UPI PIN")
                }
            }
        }
    }
}