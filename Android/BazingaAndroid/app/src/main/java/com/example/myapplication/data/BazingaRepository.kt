package com.example.myapplication.data

import com.example.myapplication.ui.UiState

class BazingaRepository(private val api: BazingaApi) {
    suspend fun fetchComics(): UiState<List<ComicDto>> =
        runCatching { api.getComics() }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to load comics") }
            )

    suspend fun login(email: String, password: String): UiState<AuthResponse> =
        runCatching { api.login(AuthRequest(email = email, password = password)) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to sign in") }
            )

    suspend fun register(username: String, email: String, password: String): UiState<AuthResponse> =
        runCatching { api.register(AuthRequest(email = email, password = password, username = username)) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to register") }
            )

    suspend fun fetchLibrary(token: String): UiState<List<LibraryItemDto>> =
        runCatching { api.getLibrary("Bearer $token") }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to load library") }
            )

    suspend fun addToLibrary(token: String, comicId: Long): UiState<List<LibraryItemDto>> =
        runCatching { api.addToLibrary("Bearer $token", LibraryItemRequest(comicId)) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to add to library") }
            )

    suspend fun fetchCart(token: String): UiState<List<CartItemDto>> =
        runCatching { api.getCart("Bearer $token") }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to load cart") }
            )

    suspend fun addToCart(
        token: String,
        comicId: Long,
        purchaseType: String
    ): UiState<List<CartItemDto>> =
        runCatching {
            api.addToCart(
                "Bearer $token",
                CartItemRequest(comicId = comicId, quantity = 1, purchaseType = purchaseType)
            )
        }.fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error(it.message ?: "Unable to add to cart") }
        )

    suspend fun updateCartQuantity(
        token: String,
        cartItemId: Long,
        quantity: Int
    ): UiState<List<CartItemDto>> =
        runCatching {
            api.updateCart(
                "Bearer $token",
                CartItemRequest(cartItemId = cartItemId, quantity = quantity)
            )
        }.fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error(it.message ?: "Unable to update cart") }
        )

    suspend fun removeCartItem(token: String, cartItemId: Long): UiState<List<CartItemDto>> =
        runCatching { api.removeCartItem("Bearer $token", cartItemId) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to remove item") }
            )

    suspend fun clearCart(token: String): UiState<List<CartItemDto>> =
        runCatching { api.clearCart("Bearer $token") }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to clear cart") }
            )

    suspend fun fetchWishlist(token: String): UiState<List<WishlistItemDto>> =
        runCatching { api.getWishlist("Bearer $token") }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to load wishlist") }
            )

    suspend fun addToWishlist(token: String, comicId: Long): UiState<List<WishlistItemDto>> =
        runCatching { api.addToWishlist("Bearer $token", WishlistItemRequest(comicId)) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to add to wishlist") }
            )

    suspend fun removeFromWishlist(token: String, comicId: Long): UiState<List<WishlistItemDto>> =
        runCatching { api.removeFromWishlist("Bearer $token", comicId) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to remove item") }
            )

    suspend fun fetchNews(): UiState<List<NewsPostDto>> =
        runCatching { api.getNews() }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to load news") }
            )

    suspend fun postNews(token: String, title: String, content: String): UiState<NewsPostDto> =
        runCatching { api.postNews("Bearer $token", NewsPostRequest(title, content)) }
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "Unable to post news") }
            )

    suspend fun subscribe(
        token: String,
        subscriptionType: String,
        billingCycle: String
    ): UiState<SubscriptionResponse> =
        runCatching {
            api.subscribe(
                "Bearer $token",
                SubscriptionRequest(subscriptionType = subscriptionType, billingCycle = billingCycle)
            )
        }.fold(
            onSuccess = { UiState.Success(it) },
            onFailure = { UiState.Error(it.message ?: "Unable to activate subscription") }
        )
}
