package com.pinto.moviemonitor.presentation.movies.adapter

import androidx.recyclerview.widget.DiffUtil
import com.pinto.moviemonitor.data.source.local.models.MovieResult

object MovieDiffCallback : DiffUtil.ItemCallback<MovieResult>() {
    override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
        return oldItem == newItem
    }
}