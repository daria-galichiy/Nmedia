package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostListItemBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.functions.likesToText


class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostListItemBinding.inflate(
            inflater, parent, false
        )
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    class ViewHolder(
        private val binding: PostListItemBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }

        init {
            binding.likeBtn.setOnClickListener {
                listener.onLikeClicked(post)
            }
            binding.shareBtn.setOnClickListener {
                listener.onShareClicked(post)
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
                options.setOnClickListener { popupMenu.show() }
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