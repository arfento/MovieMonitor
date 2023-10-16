package com.pinto.moviemonitor.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pinto.moviemonitor.data.paging.MoviePagingSource
import com.pinto.moviemonitor.data.source.local.models.MovieResult
import com.pinto.moviemonitor.data.source.remote.api.MovieApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieApi : MovieApi) : MovieRepository{
    override fun getMovieResults(): Flow<PagingData<MovieResult>>
    = Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = {MoviePagingSource(movieApi)}).flow
}