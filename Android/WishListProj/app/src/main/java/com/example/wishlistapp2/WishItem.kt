package com.example.wishlistapp2

import androidx.annotation.DrawableRes

data class WishItem(
    @DrawableRes val imageRes: Int,
    val name: String,
    val price: Int,
    var checked: Boolean = false
)
