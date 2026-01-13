package com.example.myapplication.ui.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun parseInstant(dateString: String?): Long {
    if (dateString.isNullOrBlank()) {
        return 0L
    }
    return runCatching {
        LocalDateTime.parse(dateString).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }.getOrDefault(0L)
}

fun formatPrice(amount: Double): String {
    return "$" + "%.2f".format(amount)
}

fun formatNewsDate(dateString: String): String {
    return runCatching {
        LocalDateTime.parse(dateString).format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    }.getOrDefault(dateString)
}
