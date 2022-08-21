package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.functions.likesToText

typealias OnLikeListener = (Post) -> Unit

class PostsAdapter(
    private val onLikeClicked: OnLikeListener,
    private val onShareClicked: OnLikeListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, onLikeClicked, onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        private val onLikeClicked: OnLikeListener,
        private val onShareClicked: OnLikeListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likeBtn.setOnClickListener {
                onLikeClicked(post)
            }
            binding.shareBtn.setOnClickListener {
                onShareClicked(post)
            }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                postText.text = post.content
                date.text = post.published
                likeBtn.setImageResource(if (post.likedByUser) R.drawable.ic_like_pushed_24dp else R.drawable.ic_like_24dp)
                likesCount.text = likesToText(post.likes)
                sharesCount.text = likesToText(post.shares)

            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
            oldItem == newItem
    }
}