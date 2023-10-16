package com.pinto.moviemonitor.presentation.movies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.pinto.moviemonitor.R
import com.pinto.moviemonitor.data.source.local.models.MovieResult

class MovieListAdapter(private val listener: (MovieResult) -> Unit) : PagingDataAdapter<MovieResult, MovieViewHolder>(MovieDiffCallback) {
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(itemView, listener)
    }
}