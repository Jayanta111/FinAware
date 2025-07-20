package org.finAware.project.Ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FraudTipsScreen(navController: NavController) {
    val tips = listOf(
        "❌ Never enter your UPI PIN to *receive* money.",
        "📞 Always verify caller ID claiming to be from your bank.",
        "🔗 Avoid clicking on unknown or shortened links.",
        "📵 Do not share OTPs, PINs, CVV even with 'official' callers.",
        "📱 Use official apps and avoid side-loaded APKs.",
        "🔍 Enable transaction alerts for all accounts.",
        "🛑 Report frauds at helpline 1930 or cybercrime.gov.in.",
        "✅ RBI mandates banks to reverse unauthorised UPI transactions reported within 3 days.",
        "📃 DPDP Act (India) protects your personal data. Never overshare KYC documents.",
        " Tip: Scammers often create *urgency* or *fear*. Pause before you act."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("🔐 How to Stay Safe", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        tips.forEach { tip ->
            Text(
                "• $tip",
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                navController.navigate("DashboardScreen") // Or "home" or "learning"
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Back to Dashboard")
        }
    }
}
