package com.rchyn.prosa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.ItemRowStoryBinding
import com.rchyn.prosa.domain.model.stories.Story
import com.rchyn.prosa.utils.convertTimeToLocal


class ListStoryAdapter(
    private val onClickItem: (story: Story) -> Unit,
    private val onClickItemFavorite: (story: Story) -> Unit
) : PagingDataAdapter<Story, ListStoryAdapter.ListStoryViewHolder>(DiffCallback) {
    private lateinit var context: Context

    inner class ListStoryViewHolder(
        private val binding: ItemRowStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: Story) {
            binding.apply {
                usernameWithDate.apply {
                    tvUserName.text = story.name
                    tvDate.text = story.date.convertTimeToLocal()
                }
                ivPhoto.load(story.photo) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder_image)
                }

                root.setOnClickListener {
                    onClickItem(story)
                }

                btnFavorite.isChecked = story.isFavorite
                btnFavorite.setOnClickListener {
                    onClickItemFavorite(story)
                }
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryViewHolder {
        context = parent.context
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ListStoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryViewHolder, position: Int) {
        val data = getItem(position)
        data?.let { story ->
            holder.bind(story)
        }
    }


    private companion object DiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

    }
}