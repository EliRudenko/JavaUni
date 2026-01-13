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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.myapplication.data.WishlistItemDto
import com.example.myapplication.data.resolveImageUrl
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.components.ErrorState
import com.example.myapplication.ui.components.LoadingState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted
import com.example.myapplication.ui.util.formatPrice

@Composable
fun WishlistScreen(
    authState: AuthState,
    wishlistState: UiState<List<WishlistItemDto>>,
    onNavigateHome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    onRefreshWishlist: () -> Unit,
    onRemoveItem: (Long) -> Unit,
    onMoveToCart: (WishlistItemDto) -> Unit
) {
    var pendingRemoval by remember { mutableStateOf<WishlistItemDto?>(null) }
    val items = (wishlistState as? UiState.Success)?.data.orEmpty()

    if (pendingRemoval != null) {
        AlertDialog(
            onDismissRequest = { pendingRemoval = null },
            title = { Text(text = "Remove item?") },
            text = { Text(text = "Are you sure you want to remove ${pendingRemoval?.comic?.title} from your wishlist?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingRemoval?.let { onRemoveItem(it.comic.id) }
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
                Text(text = "My Wishlist", fontSize = 24.sp, fontWeight = FontWeight.Black)
                TextButton(onClick = onRefreshWishlist) {
                    Text("Refresh")
                }
            }
            if (authState.token.isBlank()) {
                Text(
                    text = "Sign in to save your favorite comics.",
                    color = BazingaTextMuted
                )
                Button(onClick = onNavigateToLibrary, modifier = Modifier.fillMaxWidth()) {
                    Text("Sign in")
                }
            } else {
                when (wishlistState) {
                    UiState.Loading -> LoadingState(message = "Loading wishlist...")
                    is UiState.Error -> ErrorState(message = (wishlistState as UiState.Error).message)
                    is UiState.Success -> {
                        if (items.isEmpty()) {
                            Text(text = "Your wishlist is empty.", color = BazingaTextMuted)
                            Button(onClick = onNavigateHome, modifier = Modifier.fillMaxWidth()) {
                                Text("Browse comics")
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                items(items) { item ->
                                    WishlistItemRow(
                                        item = item,
                                        onRemove = { pendingRemoval = item },
                                        onMoveToCart = { onMoveToCart(item) }
                                    )
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
private fun WishlistItemRow(
    item: WishlistItemDto,
    onRemove: () -> Unit,
    onMoveToCart: () -> Unit
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
                    .height(110.dp)
                    .width(76.dp)
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
                Text(
                    text = item.comic.author ?: "Bazinga Studios",
                    fontSize = 11.sp,
                    color = BazingaTextMuted
                )
                Text(
                    text = formatPrice(item.comic.price ?: 4.99),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Button(
                    onClick = onMoveToCart,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Add to cart")
                    Text(text = "Add to cart", modifier = Modifier.padding(start = 6.dp))
                }
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, contentDescription = "Remove from wishlist")
            }
        }
    }
}
