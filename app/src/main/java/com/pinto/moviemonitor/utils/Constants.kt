package com.pinto.moviemonitor.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.pinto.moviemonitor.BuildConfig
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Constants {
    const val DB_NAME = "movie_db"
    const val BASE_URL = "https://api.themoviedb.org/4/"
    const val API_KEY = BuildConfig.API_KEY
    const val SORT_ORDER = "release_date.desc"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"
    const val DATE_TIME_PATTERN = "dd-MMMM-yyyy"
    val RELEASE_DATE_LTE = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE).toString()
}