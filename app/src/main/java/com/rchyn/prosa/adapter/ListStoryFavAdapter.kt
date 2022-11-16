package com.rchyn.prosa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rchyn.prosa.R
import com.rchyn.prosa.databinding.ItemRowStoryBinding
import com.rchyn.prosa.model.stories.Story
import com.rchyn.prosa.utils.convertTimeToLocal

class ListStoryFavAdapter(
    private val onClickItem: (
        story: Story
    ) -> Unit,
    private val onClickItemFavorite: (story: Story) -> Unit
) : ListAdapter<Story, ListStoryFavAdapter.ListStoryFavViewHolder>(DiffCallback) {
    private lateinit var ctx: Context

    inner class ListStoryFavViewHolder(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryFavViewHolder {
        ctx = parent.context
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(ctx), parent, false)
        return ListStoryFavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryFavViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
}