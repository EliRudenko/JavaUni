package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.CartItemDto
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaTextMuted
import com.example.myapplication.ui.util.formatPrice
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.YearMonth

@Composable
fun CheckoutScreen(
    repository: BazingaRepository,
    authState: AuthState,
    cartState: UiState<List<CartItemDto>>,
    onBackToCart: () -> Unit,
    onOrderComplete: () -> Unit,
    onClearCart: () -> Unit
) {
    val items = (cartState as? UiState.Success)?.data.orEmpty()
    val totalPrice = items.sumOf { it.unitPrice * it.quantity }
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf(authState.email) }
    var address by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var zip by rememberSaveable { mutableStateOf("") }
    var cardNumber by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var cvv by rememberSaveable { mutableStateOf("") }
    var isProcessing by rememberSaveable { mutableStateOf(false) }
    var orderComplete by rememberSaveable { mutableStateOf(false) }
    var showErrors by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val expiryPattern = Regex("^(0[1-9]|1[0-2])/\\d{2}$")
    val isExpiryValid = expiryPattern.matchEntire(expiry.trim())?.let { match ->
        val (monthText, yearText) = match.destructured
        val expiryDate = YearMonth.of(2000 + yearText.toInt(), monthText.toInt())
        !expiryDate.isBefore(YearMonth.now())
    } ?: false
    val isCardNumberValid = cardNumber.trim().matches(Regex("\\d{16}"))
    val isCvvValid = cvv.trim().matches(Regex("\\d{3}"))
    val areFieldsFilled = listOf(
        firstName,
        lastName,
        email,
        address,
        city,
        zip,
        cardNumber,
        expiry,
        cvv
    ).all { it.isNotBlank() }
    val isFormValid = areFieldsFilled && isCardNumberValid && isExpiryValid && isCvvValid

    if (authState.token.isBlank()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = "Checkout", fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text(text = "Sign in to complete your purchase.", color = BazingaTextMuted)
                Button(onClick = onBackToCart, modifier = Modifier.fillMaxWidth()) {
                    Text("Back to cart")
                }
            }
        }
        return
    }

    if (items.isEmpty() && !orderComplete) {
        LaunchedEffect(Unit) {
            onBackToCart()
        }
        return
    }

    if (orderComplete) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Order Complete!", fontSize = 24.sp, fontWeight = FontWeight.Black)
                Text(
                    text = "Thank you for your purchase. Your comics will be delivered soon.",
                    color = BazingaTextMuted
                )
                Button(onClick = onOrderComplete, modifier = Modifier.fillMaxWidth()) {
                    Text("Continue shopping")
                }
            }
        }
        return
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(text = "Checkout", fontSize = 24.sp, fontWeight = FontWeight.Black)
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Shipping information", fontWeight = FontWeight.Bold)
                        val firstNameError = showErrors && firstName.isBlank()
                        TextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            label = { Text("First name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = firstNameError,
                            supportingText = if (firstNameError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                        val lastNameError = showErrors && lastName.isBlank()
                        TextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            label = { Text("Last name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = lastNameError,
                            supportingText = if (lastNameError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                        val emailError = showErrors && email.isBlank()
                        TextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = emailError,
                            supportingText = if (emailError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                        val addressError = showErrors && address.isBlank()
                        TextField(
                            value = address,
                            onValueChange = { address = it },
                            label = { Text("Address") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = addressError,
                            supportingText = if (addressError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                        val cityError = showErrors && city.isBlank()
                        TextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = cityError,
                            supportingText = if (cityError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                        val zipError = showErrors && zip.isBlank()
                        TextField(
                            value = zip,
                            onValueChange = { zip = it },
                            label = { Text("ZIP code") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = zipError,
                            supportingText = if (zipError) {
                                { Text("Required") }
                            } else {
                                null
                            }
                        )
                    }
                }
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = "Payment details", fontWeight = FontWeight.Bold)
                        val cardNumberError = showErrors && cardNumber.isBlank()
                        val cardNumberFormatError = showErrors && cardNumber.isNotBlank() && !isCardNumberValid
                        TextField(
                            value = cardNumber,
                            onValueChange = { cardNumber = it },
                            label = { Text("Card number") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = cardNumberError || cardNumberFormatError,
                            supportingText = when {
                                cardNumberError -> {
                                    { Text("Required") }
                                }
                                cardNumberFormatError -> {
                                    { Text("Card number must be 16 digits.") }
                                }
                                else -> null
                            }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val expiryError = showErrors && expiry.isBlank()
                            val expiryFormatError = showErrors && expiry.isNotBlank() && !isExpiryValid
                            TextField(
                                value = expiry,
                                onValueChange = { expiry = it },
                                label = { Text("Expiry") },
                                modifier = Modifier.weight(1f),
                                isError = expiryError || expiryFormatError,
                                supportingText = when {
                                    expiryError -> {
                                        { Text("Required") }
                                    }
                                    expiryFormatError -> {
                                        { Text("Use MM/YY with a valid date.") }
                                    }
                                    else -> null
                                }
                            )
                            val cvvError = showErrors && cvv.isBlank()
                            val cvvFormatError = showErrors && cvv.isNotBlank() && !isCvvValid
                            TextField(
                                value = cvv,
                                onValueChange = { cvv = it },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f),
                                isError = cvvError || cvvFormatError,
                                supportingText = when {
                                    cvvError -> {
                                        { Text("Required") }
                                    }
                                    cvvFormatError -> {
                                        { Text("CVV must be 3 digits.") }
                                    }
                                    else -> null
                                }
                            )
                        }
                    }
                }
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = BazingaSurface
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Order summary", fontWeight = FontWeight.Bold)
                        items.forEach { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = item.comic.title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        text = "${if (item.purchaseType == "DIGITAL") "Digital" else "Original"} · Qty ${item.quantity}",
                                        fontSize = 11.sp,
                                        color = BazingaTextMuted
                                    )
                                }
                                Text(text = formatPrice(item.unitPrice * item.quantity))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Total", fontWeight = FontWeight.Bold)
                            Text(text = formatPrice(totalPrice), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        if (isProcessing) return@Button
                        showErrors = true
                        if (!isFormValid) return@Button
                        isProcessing = true
                        scope.launch {
                            delay(2000)
                            val digitalItems = items.filter { it.purchaseType == "DIGITAL" }
                            digitalItems.forEach { item ->
                                repository.addToLibrary(authState.token, item.comic.id)
                            }
                            onClearCart()
                            isProcessing = false
                            orderComplete = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isProcessing
                ) {
                    Text(text = if (isProcessing) "Processing..." else "Pay ${formatPrice(totalPrice)}")
                }
                TextButton(onClick = onBackToCart, modifier = Modifier.fillMaxWidth()) {
                    Text("Back to cart")
                }
            }
        }
    }
}
