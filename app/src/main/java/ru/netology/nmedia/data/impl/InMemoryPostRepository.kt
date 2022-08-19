package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.dto.Post

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        Post(
            id = 0L,
            author = "Somebody",
            content = "Something",
            published = "15 августа в 21:53"
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value shouldn't be null"
        }
        val likedPost = currentPost.copy(
            likedByUser = !currentPost.likedByUser,
            likes = if (currentPost.likedByUser) currentPost.likes - 1 else currentPost.likes + 1
        )
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value shouldn't be null"
        }
        val sharedPost =currentPost.copy(
            shares = currentPost.shares + 1
        )
        data.value = sharedPost
    }
}