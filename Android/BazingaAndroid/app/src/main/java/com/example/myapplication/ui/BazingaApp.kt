package com.example.myapplication.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.data.BazingaApiClient
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.CartItemDto
import com.example.myapplication.data.WishlistItemDto
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.navigation.Screen
import com.example.myapplication.ui.screens.CartScreen
import com.example.myapplication.ui.screens.CheckoutScreen
import com.example.myapplication.ui.screens.NewsScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.ReaderScreen
import com.example.myapplication.ui.screens.SubscriptionScreen
import com.example.myapplication.ui.screens.WishlistScreen
import com.example.myapplication.ui.screens.home.HomeScreen
import com.example.myapplication.ui.screens.LibraryScreen
import kotlinx.coroutines.launch

@Composable
fun BazingaApp() {
    val navController = rememberNavController()
    val repository = remember { BazingaRepository(BazingaApiClient.api) }
    var authState by remember { mutableStateOf(AuthState()) }
    var cartState by remember { mutableStateOf<UiState<List<CartItemDto>>>(UiState.Success(emptyList())) }
    var wishlistState by remember { mutableStateOf<UiState<List<WishlistItemDto>>>(UiState.Success(emptyList())) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState.token) {
        cartState = if (authState.token.isNotBlank()) {
            repository.fetchCart(authState.token)
        } else {
            UiState.Success(emptyList())
        }
    }

    LaunchedEffect(authState.token) {
        wishlistState = if (authState.token.isNotBlank()) {
            repository.fetchWishlist(authState.token)
        } else {
            UiState.Success(emptyList())
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute != Screen.Reader.route) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text(Screen.Home.label) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Library.route,
                        onClick = { navController.navigate(Screen.Library.route) },
                        icon = { Icon(Icons.Filled.Book, contentDescription = "Library") },
                        label = { Text(Screen.Library.label) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Cart.route,
                        onClick = { navController.navigate(Screen.Cart.route) },
                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart") },
                        label = { Text(Screen.Cart.label) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.News.route,
                        onClick = { navController.navigate(Screen.News.route) },
                        icon = { Icon(Icons.Filled.Article, contentDescription = "News") },
                        label = { Text(Screen.News.label) }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { navController.navigate(Screen.Profile.route) },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                        label = { Text(Screen.Profile.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    authState = authState,
                    onAddToCart = { comic, purchaseType ->
                        if (authState.token.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign in to add items to your cart.")
                            }
                        } else {
                            scope.launch {
                                when (val result = repository.addToCart(authState.token, comic.id, purchaseType)) {
                                    is UiState.Success -> {
                                        cartState = result
                                        snackbarHostState.showSnackbar("Added to cart!")
                                    }
                                    is UiState.Error -> snackbarHostState.showSnackbar(result.message)
                                    UiState.Loading -> Unit
                                }
                            }
                        }
                    },
                    onNavigateToSubscription = {
                        if (authState.token.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign in to subscribe.")
                            }
                            navController.navigate(Screen.Library.route)
                        } else {
                            navController.navigate(Screen.Subscription.route)
                        }
                    },
                    onNavigateToWishlist = { navController.navigate(Screen.Wishlist.route) },
                    wishlistComicIds = (wishlistState as? UiState.Success)
                        ?.data
                        ?.map { it.comic.id }
                        ?.toSet()
                        ?: emptySet(),
                    onAddToWishlist = { comicId ->
                        if (authState.token.isBlank()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Sign in to save to wishlist.")
                            }
                        } else {
                            scope.launch {
                                wishlistState = repository.addToWishlist(authState.token, comicId)
                                if (wishlistState is UiState.Success) {
                                    snackbarHostState.showSnackbar("Added to wishlist!")
                                }
                            }
                        }
                    },
                    onRemoveFromWishlist = { comicId ->
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                wishlistState = repository.removeFromWishlist(authState.token, comicId)
                                if (wishlistState is UiState.Success) {
                                    snackbarHostState.showSnackbar("Removed from wishlist.")
                                }
                            }
                        }
                    }
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen(
                    repository = repository,
                    authState = authState,
                    onAuthStateChange = { authState = it },
                    onReadComic = { comicId -> navController.navigate(Screen.Reader.createRoute(comicId)) }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    authState = authState,
                    onSignOut = {
                        authState = AuthState()
                        cartState = UiState.Success(emptyList())
                        wishlistState = UiState.Success(emptyList())
                        navController.navigate(Screen.Home.route)
                    },
                    onNavigateToLibrary = { navController.navigate(Screen.Library.route) },
                    onProfileUpdate = { updated -> authState = updated }
                )
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    authState = authState,
                    cartState = cartState,
                    onNavigateHome = { navController.navigate(Screen.Home.route) },
                    onNavigateToLibrary = { navController.navigate(Screen.Library.route) },
                    onCheckout = { navController.navigate(Screen.Checkout.route) },
                    onRefreshCart = {
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                cartState = repository.fetchCart(authState.token)
                            }
                        }
                    },
                    onUpdateQuantity = { itemId, quantity ->
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                cartState = repository.updateCartQuantity(authState.token, itemId, quantity)
                            }
                        }
                    },
                    onRemoveItem = { itemId ->
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                cartState = repository.removeCartItem(authState.token, itemId)
                            }
                        }
                    }
                )
            }
            composable(Screen.Wishlist.route) {
                WishlistScreen(
                    authState = authState,
                    wishlistState = wishlistState,
                    onNavigateHome = { navController.navigate(Screen.Home.route) },
                    onNavigateToLibrary = { navController.navigate(Screen.Library.route) },
                    onRefreshWishlist = {
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                wishlistState = repository.fetchWishlist(authState.token)
                            }
                        }
                    },
                    onRemoveItem = { comicId ->
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                wishlistState = repository.removeFromWishlist(authState.token, comicId)
                            }
                        }
                    },
                    onMoveToCart = { item ->
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                when (val result = repository.addToCart(authState.token, item.comic.id, "ORIGINAL")) {
                                    is UiState.Success -> {
                                        cartState = result
                                        wishlistState = repository.removeFromWishlist(authState.token, item.comic.id)
                                        snackbarHostState.showSnackbar("Moved to cart!")
                                    }
                                    is UiState.Error -> snackbarHostState.showSnackbar(result.message)
                                    UiState.Loading -> Unit
                                }
                            }
                        }
                    }
                )
            }
            composable(Screen.News.route) {
                NewsScreen(
                    repository = repository,
                    authState = authState
                )
            }
            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    repository = repository,
                    authState = authState,
                    cartState = cartState,
                    onBackToCart = { navController.navigate(Screen.Cart.route) },
                    onOrderComplete = {
                        scope.launch {
                            cartState = UiState.Success(emptyList())
                            snackbarHostState.showSnackbar("Order placed successfully!")
                            navController.navigate(Screen.Home.route)
                        }
                    },
                    onClearCart = {
                        if (authState.token.isNotBlank()) {
                            scope.launch {
                                cartState = repository.clearCart(authState.token)
                            }
                        }
                    }
                )
            }
            composable(Screen.Subscription.route) {
                SubscriptionScreen(
                    repository = repository,
                    authState = authState,
                    onAuthStateChange = { authState = it },
                    onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable(
                Screen.Reader.route,
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backStackEntry ->
                val comicId = backStackEntry.arguments?.getLong("id") ?: 0L
                ReaderScreen(
                    repository = repository,
                    comicId = comicId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
