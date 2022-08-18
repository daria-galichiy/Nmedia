package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.functions.likesToText
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this){ post ->
            binding.render(post)
        }

        binding.postPage.likeBtn.setOnClickListener {
            viewModel.onLikeClicked()
        }

        binding.postPage.shareBtn.setOnClickListener {
            viewModel.onShareClicked()
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        postPage.authorName.text = post.author
        postPage.postText.text = post.content
        postPage.date.text = post.published
        postPage.likeBtn.setImageResource(getLikeIconResId(post.likedByUser))
        postPage.likesCount.text = likesToText(post.likes)
        postPage.sharesCount.text = likesToText(post.shares)
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_like_pushed_24dp else R.drawable.ic_like_24dp
}