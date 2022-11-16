package com.rchyn.prosa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rchyn.prosa.databinding.ItemRowLocationBinding
import com.rchyn.prosa.model.place.Place

class ListLocationAdapter(
    private val onClickItem: (place: Place) -> Unit
) : ListAdapter<Place, ListLocationAdapter.ListLocationViewHolder>(DiffCallback) {

    inner class ListLocationViewHolder(
        private val binding: ItemRowLocationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.apply {
                tvName.text = place.name
                tvFormatted.text = place.formatted

                root.setOnClickListener { onClickItem(place) }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListLocationViewHolder {
        val binding =
            ItemRowLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListLocationViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }

    }
}