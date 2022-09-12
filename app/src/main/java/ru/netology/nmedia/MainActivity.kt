package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.get
import androidx.core.view.isVisible
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

        val viewModel: PostViewModel by viewModels()
        val adapter = PostsAdapter(viewModel)

        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.saveBtn.setOnClickListener {
            with(binding.contentEditText) {
                val content = binding.contentEditText.text.toString()
                viewModel.onSaveButtonClicked(content)
            }
            binding.editGroup.visibility = View.INVISIBLE
        }

        viewModel.currentPost.observe(this) { currentPost ->
            val content = currentPost?.content
            with(binding.contentEditText) {
                setText(content)
                if (content != null) {
                    requestFocus()
                    showKeyboard()
                    binding.editGroup.visibility = View.VISIBLE
                } else {
                    clearFocus()
                    hideKeyboard()
                }
            }
            binding.editingMessageText.text = content
        }

        binding.closeEditPostButton.setOnClickListener{
            viewModel.onCloseEditingClicked()
            binding.editingMessageText.text = ""
            binding.contentEditText.setText("")
            binding.editGroup.visibility = View.INVISIBLE
        }
    }
}