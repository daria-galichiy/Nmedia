package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.functions.likesToText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Somebody",
            content = "Something",
            published = "15 августа в 21:53"
        )

        binding.render(post)
        binding.postPage.likeBtn.setOnClickListener {
            post.likes = if (post.likedByUser) post.likes - 1 else post.likes + 1
            binding.postPage.likesCount.text = likesToText(post.likes)

            post.likedByUser = !post.likedByUser
            binding.postPage.likeBtn.setImageResource(getLikeIconResId(post.likedByUser))
        }

        binding.postPage.shareBtn.setOnClickListener {
            post.shares++
            binding.postPage.sharesCount.text = likesToText(post.shares)
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        postPage.authorName.text = post.author
        postPage.postText.text = post.content
        postPage.date.text = post.published
        postPage.likeBtn.setImageResource(getLikeIconResId(post.likedByUser))
        postPage.likesCount.text = post.likes.toString()
        postPage.sharesCount.text = post.shares.toString()
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_like_pushed_24dp else R.drawable.ic_like_24dp
}