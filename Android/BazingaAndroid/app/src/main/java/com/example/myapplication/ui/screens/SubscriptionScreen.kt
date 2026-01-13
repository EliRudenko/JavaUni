package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted
import kotlinx.coroutines.launch

private data class SubscriptionPlan(
    val name: String,
    val monthly: Double,
    val yearly: Double,
    val accent: Color,
    val benefits: List<String>
)

@Composable
fun SubscriptionScreen(
    repository: BazingaRepository,
    authState: AuthState,
    onAuthStateChange: (AuthState) -> Unit,
    onNavigateToProfile: () -> Unit,
    onBack: () -> Unit
) {
    val plans = listOf(
        SubscriptionPlan(
            name = "Premium",
            monthly = 4.99,
            yearly = 49.99,
            accent = Color(0xFFF97316),
            benefits = listOf(
                "35% discount for all the physical copies of the comics",
                "50% discount for the digital copies"
            )
        ),
        SubscriptionPlan(
            name = "Unlimited",
            monthly = 14.99,
            yearly = 159.99,
            accent = BazingaRed,
            benefits = listOf(
                "50% discount for all the physical copies of the comics",
                "Unlimited access to the digital copies"
            )
        )
    )

    var selectedPlan by rememberSaveable { mutableStateOf(plans.first().name) }
    var billingCycle by rememberSaveable { mutableStateOf("monthly") }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var orderComplete by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val currentPlan = plans.first { it.name == selectedPlan }
    val price = if (billingCycle == "yearly") currentPlan.yearly else currentPlan.monthly

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onBack) {
                Text(text = "Back", color = BazingaTextMuted)
            }

            if (authState.token.isBlank()) {
                Text(
                    text = "Sign in to start a Bazinga subscription.",
                    fontSize = 16.sp,
                    color = BazingaTextMuted
                )
                return@Column
            }

            if (orderComplete) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Subscription Complete!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Your Bazinga $selectedPlan plan is now active.",
                            fontSize = 14.sp,
                            color = BazingaTextMuted
                        )
                        Button(
                            onClick = onNavigateToProfile,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "View Profile")
                        }
                    }
                }
                return@Column
            }

            Text(
                text = "Choose your subscription",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Unlock exclusive pricing on physical comics and expand your digital reading experience.",
                fontSize = 14.sp,
                color = BazingaTextMuted
            )

            plans.forEach { plan ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "Bazinga ${plan.name}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = plan.accent
                                )
                                Text(
                                    text = "$${"%.2f".format(plan.monthly)} / month or $${"%.2f".format(plan.yearly)} / year",
                                    fontSize = 12.sp,
                                    color = BazingaTextMuted
                                )
                            }
                            if (selectedPlan == plan.name) {
                                Text(
                                    text = "Selected",
                                    fontSize = 12.sp,
                                    color = plan.accent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            plan.benefits.forEach { benefit ->
                                Text(text = "• $benefit", fontSize = 12.sp, color = BazingaTextMuted)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(
                                onClick = {
                                    selectedPlan = plan.name
                                    billingCycle = "monthly"
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = plan.accent)
                            ) {
                                Text(text = "Subscribe Monthly")
                            }
                            OutlinedButton(
                                onClick = {
                                    selectedPlan = plan.name
                                    billingCycle = "yearly"
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = plan.accent)
                            ) {
                                Text(text = "Subscribe Yearly")
                            }
                        }
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BazingaSurface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Subscriber details", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First name") },
                            modifier = Modifier.weight(1f)
                        )
                        TextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last name") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BazingaSurface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = "Payment details", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    TextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = { Text("Card number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        TextField(
                            value = expiry,
                            onValueChange = { expiry = it },
                            label = { Text("Expiry") },
                            modifier = Modifier.weight(1f)
                        )
                        TextField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BazingaSurfaceAlt
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Subscription summary", fontWeight = FontWeight.Bold)
                    Text(text = "Plan: Bazinga $selectedPlan", fontSize = 12.sp, color = BazingaTextMuted)
                    Text(text = "Billing: ${billingCycle.replaceFirstChar { it.uppercase() }}", fontSize = 12.sp, color = BazingaTextMuted)
                    Text(text = "Total: $${"%.2f".format(price)}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Button(
                onClick = {
                    if (firstName.isBlank() || lastName.isBlank() || cardNumber.isBlank() || expiry.isBlank() || cvv.isBlank()) {
                        feedbackMessage = "Please complete all payment details."
                        return@Button
                    }
                    if (isProcessing) return@Button
                    isProcessing = true
                    feedbackMessage = null
                    scope.launch {
                        when (val result = repository.subscribe(authState.token, selectedPlan, billingCycle)) {
                            is UiState.Success -> {
                                onAuthStateChange(
                                    authState.copy(
                                        subscriptionType = result.data.subscriptionType,
                                        subscriptionExpiration = result.data.subscriptionExpiration
                                    )
                                )
                                orderComplete = true
                            }
                            is UiState.Error -> feedbackMessage = result.message
                            UiState.Loading -> Unit
                        }
                        isProcessing = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isProcessing
            ) {
                Text(text = if (isProcessing) "Processing..." else "Pay $${"%.2f".format(price)}")
            }

            feedbackMessage?.let { message ->
                Text(text = message, fontSize = 12.sp, color = BazingaTextMuted)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
