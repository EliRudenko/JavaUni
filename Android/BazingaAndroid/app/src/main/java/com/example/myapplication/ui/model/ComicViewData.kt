package com.example.myapplication.ui.model

data class ComicViewData(
    val id: Long,
    val title: String,
    val creators: String,
    val series: String,
    val character: String,
    val image: String?,
    val comicType: String,
    val createdAt: String?
)
