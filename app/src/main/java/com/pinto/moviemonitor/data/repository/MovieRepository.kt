package com.pinto.moviemonitor.data.repository

import androidx.paging.PagingData
import com.pinto.moviemonitor.data.source.local.models.MovieResult
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovieResults(): Flow<PagingData<MovieResult>>
}