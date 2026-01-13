package com.example.myapplication.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

private const val BASE_URL = "http://13.60.79.86:8080"

interface BazingaApi {
    @GET("/api/comics")
    suspend fun getComics(): List<ComicDto>

    @POST("/api/auth/login")
    suspend fun login(@Body request: AuthRequest): AuthResponse

    @POST("/api/auth/register")
    suspend fun register(@Body request: AuthRequest): AuthResponse

    @GET("/api/library")
    suspend fun getLibrary(@Header("Authorization") token: String): List<LibraryItemDto>

    @POST("/api/library")
    suspend fun addToLibrary(
        @Header("Authorization") token: String,
        @Body request: LibraryItemRequest
    ): List<LibraryItemDto>

    @GET("/api/cart")
    suspend fun getCart(@Header("Authorization") token: String): List<CartItemDto>

    @POST("/api/cart")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Body request: CartItemRequest
    ): List<CartItemDto>

    @PUT("/api/cart")
    suspend fun updateCart(
        @Header("Authorization") token: String,
        @Body request: CartItemRequest
    ): List<CartItemDto>

    @DELETE("/api/cart/{cartItemId}")
    suspend fun removeCartItem(
        @Header("Authorization") token: String,
        @Path("cartItemId") cartItemId: Long
    ): List<CartItemDto>

    @DELETE("/api/cart")
    suspend fun clearCart(@Header("Authorization") token: String): List<CartItemDto>

    @GET("/api/wishlist")
    suspend fun getWishlist(@Header("Authorization") token: String): List<WishlistItemDto>

    @POST("/api/wishlist")
    suspend fun addToWishlist(
        @Header("Authorization") token: String,
        @Body request: WishlistItemRequest
    ): List<WishlistItemDto>

    @DELETE("/api/wishlist/{comicId}")
    suspend fun removeFromWishlist(
        @Header("Authorization") token: String,
        @Path("comicId") comicId: Long
    ): List<WishlistItemDto>

    @GET("/api/news")
    suspend fun getNews(): List<NewsPostDto>

    @POST("/api/news")
    suspend fun postNews(
        @Header("Authorization") token: String,
        @Body request: NewsPostRequest
    ): NewsPostDto

    @POST("/api/subscriptions/subscribe")
    suspend fun subscribe(
        @Header("Authorization") token: String,
        @Body request: SubscriptionRequest
    ): SubscriptionResponse
}

object BazingaApiClient {
    val api: BazingaApi by lazy {
        val client = OkHttpClient.Builder().build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(BazingaApi::class.java)
    }
}

fun resolveImageUrl(image: String?): String? {
    if (image.isNullOrBlank()) {
        return null
    }

    val trimmed = image.trim()

    val isAbsolute = trimmed.startsWith("http://") || trimmed.startsWith("https://")
            || trimmed.startsWith("data:") || trimmed.startsWith("blob:")

    if (isAbsolute) {
        return trimmed
    }

    val normalized = if (trimmed.startsWith("/")) trimmed.drop(1) else trimmed
    return "$BASE_URL/$normalized"
}
