package com.blocksdecoded.coinwave.presentation.posts.recycler

import androidx.recyclerview.widget.DiffUtil
import com.blocksdecoded.coinwave.data.post.model.PublisherPost

class PostsDiffUtil(
    private val old: List<PublisherPost>,
    private val new: List<PublisherPost>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = try {
        old[oldItemPosition].id == new[newItemPosition].id
    } catch (e: Exception) {
        false
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = try {
        val oldItem = old[oldItemPosition]
        val newItem = new[oldItemPosition]

        oldItem.id == newItem.id && oldItem.date == newItem.date &&
                oldItem.title == newItem.title &&
                oldItem.url == newItem.url &&
                oldItem.image?.featured == newItem.image?.featured
    } catch (e: Exception) {
        false
    }
}