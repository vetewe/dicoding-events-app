package com.dicoding.dicodingevent.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import com.dicoding.dicodingevent.databinding.ItemFavoriteEventBinding

class FavoriteAdapter(
    private val onItemClick: (FavoriteEvent) -> Unit,
    private val onFavoriteClick: (FavoriteEvent) -> Unit
) : ListAdapter<FavoriteEvent, FavoriteAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemFavoriteEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener { onItemClick(event) }
        holder.binding.ivFavoriteIcon.setOnClickListener { onFavoriteClick(event) }
    }

    class MyViewHolder(val binding: ItemFavoriteEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: FavoriteEvent) {
            binding.tvName.text = event.name
            Glide.with(itemView.context)
                .load(event.imgLogo)
                .into(binding.ivImageLogo)
            binding.ivFavoriteIcon.setImageResource(R.drawable.favorite_full)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteEvent>() {
            override fun areItemsTheSame(
                oldItem: FavoriteEvent,
                newItem: FavoriteEvent
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: FavoriteEvent,
                newItem: FavoriteEvent
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
