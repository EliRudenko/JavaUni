package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.CartItemDto
import com.example.myapplication.data.resolveImageUrl
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.components.ErrorState
import com.example.myapplication.ui.components.LoadingState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted
import com.example.myapplication.ui.util.formatPrice

@Composable
fun CartScreen(
    authState: AuthState,
    cartState: UiState<List<CartItemDto>>,
    onNavigateHome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onCheckout: () -> Unit,
    onRefreshCart: () -> Unit,
    onUpdateQuantity: (Long, Int) -> Unit,
    onRemoveItem: (Long) -> Unit
) {
    var pendingRemoval by remember { mutableStateOf<CartItemDto?>(null) }
    val items = (cartState as? UiState.Success)?.data.orEmpty()
    val totalItems = items.sumOf { it.quantity }
    val totalPrice = items.sumOf { it.unitPrice * it.quantity }

    if (pendingRemoval != null) {
        AlertDialog(
            onDismissRequest = { pendingRemoval = null },
            title = { Text(text = "Remove item?") },
            text = { Text(text = "Are you sure you want to remove ${pendingRemoval?.comic?.title} from your cart?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingRemoval?.let { onRemoveItem(it.id) }
                        pendingRemoval = null
                    }
                ) {
                    Text("Remove")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingRemoval = null }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Your Cart", fontSize = 24.sp, fontWeight = FontWeight.Black)
                TextButton(onClick = onRefreshCart) {
                    Text("Refresh")
                }
            }
            if (authState.token.isBlank()) {
                Text(
                    text = "Sign in to manage your cart and checkout.",
                    color = BazingaTextMuted
                )
                Button(onClick = onNavigateToLibrary, modifier = Modifier.fillMaxWidth()) {
                    Text("Sign in")
                }
            } else {
                when (cartState) {
                    UiState.Loading -> LoadingState(message = "Loading cart...")
                    is UiState.Error -> ErrorState(message = (cartState as UiState.Error).message)
                    is UiState.Success -> {
                        if (items.isEmpty()) {
                            Text(text = "Your cart is empty.", color = BazingaTextMuted)
                            Button(onClick = onNavigateHome, modifier = Modifier.fillMaxWidth()) {
                                Text("Continue shopping")
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(items) { item ->
                                    CartItemRow(
                                        item = item,
                                        onIncrease = { onUpdateQuantity(item.id, item.quantity + 1) },
                                        onDecrease = { onUpdateQuantity(item.id, item.quantity - 1) },
                                        onRemove = { pendingRemoval = item }
                                    )
                                }
                            }
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                color = BazingaSurface
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Items")
                                        Text(text = totalItems.toString())
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "Total")
                                        Text(text = formatPrice(totalPrice), fontWeight = FontWeight.Bold)
                                    }
                                    Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth()) {
                                        Text("Proceed to Checkout")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItemDto,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = BazingaSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val image = resolveImageUrl(item.comic.image)
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(BazingaSurfaceAlt)
            ) {
                if (image != null) {
                    AsyncImage(
                        model = image,
                        contentDescription = item.comic.title,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.comic.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(text = item.comic.author ?: "Bazinga Studios", fontSize = 11.sp, color = BazingaTextMuted)
                Text(
                    text = if (item.purchaseType == "DIGITAL") "Digital Copy" else "Original Copy",
                    fontSize = 11.sp,
                    color = BazingaTextMuted
                )
                Text(
                    text = formatPrice(item.unitPrice * item.quantity),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Filled.Add, contentDescription = "Increase quantity")
                }
                Text(text = item.quantity.toString(), fontWeight = FontWeight.Bold)
                IconButton(onClick = onDecrease) {
                    Icon(Icons.Filled.Remove, contentDescription = "Decrease quantity")
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove item", tint = BazingaRed)
            }
        }
    }
}
