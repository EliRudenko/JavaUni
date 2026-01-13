package com.example.myapplication.data

data class ComicDto(
    val id: Long,
    val title: String,
    val author: String? = null,
    val description: String? = null,
    val mainCharacter: String? = null,
    val series: String? = null,
    val image: String? = null,
    val price: Double? = null,
    val category: CategoryDto? = null,
    val comicType: String? = null,
    val createdAt: String? = null
)

data class CategoryDto(
    val name: String? = null
)

data class LibraryItemDto(
    val id: Long,
    val comic: ComicDto
)

data class AuthRequest(
    val email: String,
    val password: String,
    val username: String? = null
)

data class AuthResponse(
    val token: String,
    val userId: Long,
    val username: String,
    val email: String,
    val role: String,
    val subscriptionType: String? = null,
    val subscriptionExpiration: String? = null
)

data class LibraryItemRequest(
    val comicId: Long
)

data class CartItemDto(
    val id: Long,
    val comic: ComicDto,
    val quantity: Int,
    val purchaseType: String,
    val unitPrice: Double
)

data class CartItemRequest(
    val cartItemId: Long? = null,
    val comicId: Long? = null,
    val quantity: Int? = null,
    val purchaseType: String? = null
)

data class WishlistItemDto(
    val id: Long,
    val comic: ComicDto
)

data class WishlistItemRequest(
    val comicId: Long
)

data class NewsPostDto(
    val id: Long,
    val title: String,
    val content: String,
    val authorId: Long,
    val authorUsername: String,
    val authorRole: String,
    val createdAt: String,
    val expiresAt: String
)

data class NewsPostRequest(
    val title: String,
    val content: String
)

data class SubscriptionRequest(
    val subscriptionType: String,
    val billingCycle: String
)

data class SubscriptionResponse(
    val subscriptionType: String,
    val subscriptionExpiration: String?
)
