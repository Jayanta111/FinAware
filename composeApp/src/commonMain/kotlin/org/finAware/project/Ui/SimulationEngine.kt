package org.finAware.project.Ui


import org.finAware.project.model.FraudType

data class SimulationStep(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

fun getSimulationSteps(type: FraudType): List<SimulationStep> = when (type) {

    FraudType.UPI_SCAM -> listOf(
        SimulationStep(
            question = "You receive a call asking to scan a QR code to receive ₹500.",
            options = listOf("Scan", "Ignore", "Ask for proof"),
            correctAnswerIndex = 1
        ),
        SimulationStep(
            question = "Caller says it's from your bank and asks for an OTP.",
            options = listOf("Give OTP", "Hang up", "Share last 4 digits"),
            correctAnswerIndex = 1
        )
    )

    FraudType.PHISHING -> listOf(
        SimulationStep(
            question = "You receive an email claiming your account will be blocked unless you click a link.",
            options = listOf("Click the link", "Verify sender and ignore", "Forward it to a friend"),
            correctAnswerIndex = 1
        ),
        SimulationStep(
            question = "The link asks for your login credentials and ATM PIN.",
            options = listOf("Enter details", "Exit and report", "Enter only login ID"),
            correctAnswerIndex = 1
        )
    )

    FraudType.OTP_FRAUD -> listOf(
        SimulationStep(
            question = "Someone asks for OTP to verify a KYC update.",
            options = listOf("Share OTP", "Ignore the request", "Ask who sent it"),
            correctAnswerIndex = 1
        ),
        SimulationStep(
            question = "You get an SMS asking to confirm a transaction using OTP.",
            options = listOf("Enter OTP", "Call bank", "Delete the message"),
            correctAnswerIndex = 1
        )
    )

    FraudType.INVESTMENT_SCAM -> listOf(
        SimulationStep(
            question = "An agent guarantees 3x returns in 6 months if you invest today.",
            options = listOf("Invest quickly", "Verify with SEBI", "Ask for a brochure"),
            correctAnswerIndex = 1
        ),
        SimulationStep(
            question = "You see a celebrity endorsement for a crypto app on WhatsApp.",
            options = listOf("Download and invest", "Ignore it", "Message for more info"),
            correctAnswerIndex = 1
        )
    )

    else -> listOf(
        SimulationStep(
            question = "Simulation not available for this fraud type.",
            options = listOf("OK"),
            correctAnswerIndex = 0
        )
    )
}
