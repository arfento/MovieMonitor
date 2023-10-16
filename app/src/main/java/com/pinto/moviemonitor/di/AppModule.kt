package com.pinto.moviemonitor.di

import android.content.Context
import androidx.room.Room
import com.pinto.moviemonitor.data.repository.MovieRepositoryImpl
import com.pinto.moviemonitor.data.source.local.database.MovieDatabase
import com.pinto.moviemonitor.data.source.remote.api.MovieApi
import com.pinto.moviemonitor.utils.Constants
import com.pinto.moviemonitor.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    fun provideMovieRepositoryImpl(movieApi: MovieApi): MovieRepositoryImpl {
        return MovieRepositoryImpl(movieApi)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            DB_NAME
        ).build()
    }
}