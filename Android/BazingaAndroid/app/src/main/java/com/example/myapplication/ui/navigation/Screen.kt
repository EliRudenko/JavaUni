package com.example.myapplication.ui.navigation

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "Main")
    object Library : Screen("library", "Library")
    object Profile : Screen("profile", "Profile")
    object Cart : Screen("cart", "Cart")
    object News : Screen("news", "News")
    object Wishlist : Screen("wishlist", "Wishlist")
    object Checkout : Screen("checkout", "Checkout")
    object Subscription : Screen("subscription", "Subscription")
    object Reader : Screen("reader/{id}", "Reader") {
        fun createRoute(id: Long) = "reader/$id"
    }
}
