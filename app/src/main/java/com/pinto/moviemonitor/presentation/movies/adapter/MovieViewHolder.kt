package com.pinto.moviemonitor.presentation.movies.adapter

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.pinto.moviemonitor.R
import com.pinto.moviemonitor.data.source.local.models.MovieResult
import com.pinto.moviemonitor.utils.Constants
import com.pinto.moviemonitor.utils.GenreIdConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MovieViewHolder(itemView: View, private val listener : (MovieResult) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private val ivMovieImage : ImageView = itemView.findViewById(R.id.ivMovieImage)
    private val tvMovieTitle : TextView = itemView.findViewById(R.id.tvMovieTitle)
    private val tvMovieReleaseDate : TextView = itemView.findViewById(R.id.tvMovieReleaseDate)
    private val tvMovieGenre : TextView = itemView.findViewById(R.id.tvMovieGenre)
    private val ratingBar : RatingBar = itemView.findViewById(R.id.ratingBar)

    fun bind(movie: MovieResult?) {
        if (movie != null){
            tvMovieTitle.text = movie.title ?: ""
            val formatter = DateTimeFormatter.ofPattern(Constants.DATE_TIME_PATTERN)
            val releaseDate = movie.releaseDate
            if (releaseDate != null){
                tvMovieReleaseDate.text = LocalDate.parse(releaseDate).format(formatter)
            }

            ratingBar.rating = (movie.rating ?: 0) as Float
            val movieImageUrl = "${Constants.IMAGE_BASE_URL}${movie.imagePath}"
            Glide.with(itemView.context).load(movieImageUrl).placeholder(R.drawable.placeholder)
                .centerCrop().transform(RoundedCorners(20)).into(ivMovieImage)

            tvMovieGenre.text = GenreIdConverter.convertIdToGenre(movie.genreIds)

            itemView.setOnClickListener {
                listener.invoke(movie)
            }
        }


    }

}