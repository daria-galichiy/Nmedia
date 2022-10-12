package ru.netology.nmedia.dto

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val video: String? = "",
    val published: String,
    val likes: Int = 0,
    val likedByUser: Boolean = false,
    val shares: Int = 0
)