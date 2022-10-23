package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostActionsFragmentBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.functions.likesToText
import ru.netology.nmedia.viewModel.PostViewModel


class PostActionsFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    private val args by navArgs<PostActionsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val direction = PostActionsFragmentDirections.postActionsFragmentToFeedFragment()
            findNavController().navigate(direction)
        }

        val binding = PostActionsFragmentBinding.inflate(layoutInflater, container, false)

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(
                PostContentFragment.RESULT_KEY
            ) ?: return@setFragmentResultListener
            viewModel.onSaveButtonClicked(newPostContent)
            binding.postActionsItem.postText.text = newPostContent
        }

        val postId = args.postId

        fun bind(post: Post) {
            with(binding) {
                postActionsItem.authorName.text = post.author
                postActionsItem.postText.text = post.content
                postActionsItem.date.text = post.published
                postActionsItem.likeBtn.text = likesToText(post.likes)
                postActionsItem.likeBtn.isChecked = post.likedByUser
                postActionsItem.shareBtn.text = likesToText(post.shares)

                if (post.video != "") postActionsItem.videoGroup.visibility = View.VISIBLE
            }
        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == postId } ?: return@observe
            bind(post)

            with(binding) {
                postActionsItem.options.setOnClickListener {
                    PopupMenu(root.context, postActionsItem.options).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { menuItem ->
                            when (menuItem.itemId) {
                                R.id.edit -> {
                                    viewModel.onEditClicked(post)
                                    val direction = PostActionsFragmentDirections.postActionsFragmentToPostContentFragment(post.content)
                                    findNavController().navigate(direction)
                                    true
                                }
                                R.id.remove -> {
                                    viewModel.onRemoveClicked(post)
                                    val direction = PostActionsFragmentDirections.postActionsFragmentToFeedFragment()
                                    findNavController().navigate(direction)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }

                postActionsItem.likeBtn.setOnClickListener {
                    viewModel.onLikeClicked(post)
                }
                postActionsItem.shareBtn.setOnClickListener {
                    viewModel.onShareClicked(post)
                }
                postActionsItem.videoImage.setOnClickListener {
                    viewModel.onPlayVideoClicked(post)
                }
                postActionsItem.playBtn.setOnClickListener {
                    viewModel.onPlayVideoClicked(post)
                }
            }

        }

        viewModel.sharePostContent.observe(viewLifecycleOwner) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        viewModel.playPostVideo.observe(viewLifecycleOwner) { postVideo ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(postVideo)
            }

            val playVideoIntent = Intent.createChooser(
                intent, getString(R.string.play_video)
            )
            startActivity(playVideoIntent)
        }
        return binding.root
    }
}