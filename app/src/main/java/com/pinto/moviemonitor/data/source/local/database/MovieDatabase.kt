package com.pinto.moviemonitor.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pinto.moviemonitor.data.source.local.models.MovieRemoteKeys
import com.pinto.moviemonitor.data.source.local.models.MovieResult

@Database(entities = [MovieResult::class, MovieRemoteKeys::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    abstract fun getMovieRemoteKeysDao(): MovieRemoteKeysDao

    inline fun <T> withTransaction(block: () -> T): T {
        beginTransaction()
        return try {
            val result = block()
            setTransactionSuccessful()
            result
        } finally {
            endTransaction()
        }
    }
}