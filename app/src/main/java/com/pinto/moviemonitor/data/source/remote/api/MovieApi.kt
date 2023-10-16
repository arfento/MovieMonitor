package com.pinto.moviemonitor.data.source.remote.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.pinto.moviemonitor.data.source.local.models.MovieListResult
import com.pinto.moviemonitor.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    suspend fun getMovieResults(
        @Query("sort_by") sortBy: String = Constants.SORT_ORDER,
        @Query("page") page: Int,
        @Query("release_date.lte") releaseDateLTE: String = Constants.RELEASE_DATE_LTE,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): MovieListResult
}