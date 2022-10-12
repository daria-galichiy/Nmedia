package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.core.content.edit
import androidx.core.view.get
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.util.showKeyboard
import ru.netology.nmedia.viewModel.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        run {
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            preferences.edit {
//                putString("key", "value")
//            }
//        }
//
//        run {
//            val preferences = getPreferences(Context.MODE_PRIVATE)
//            val value = preferences.getString("key", "no value") ?: return@run
//            Snackbar.make(binding.root, value, Snackbar.LENGTH_INDEFINITE).show()
//        }

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }

        viewModel.sharePostContent.observe(this) { postContent ->
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

        viewModel.playPostVideo.observe(this) { postVideo ->
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(postVideo)
            }

            val playVideoIntent = Intent.createChooser(
                intent, getString(R.string.play_video)
            )
            startActivity(playVideoIntent)
        }

        val PostContentActivityLauncher = registerForActivityResult(
            PostContentActivity.ResultContract
        ) { postContent ->
            postContent ?: return@registerForActivityResult
            viewModel.onSaveButtonClicked(postContent)
        }
        viewModel.navigateToPostContentScreenEvent.observe(this) {
            PostContentActivityLauncher.launch(viewModel.currentPost.value?.content)
        }
    }
}