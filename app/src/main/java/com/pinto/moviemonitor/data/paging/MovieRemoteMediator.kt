package com.pinto.moviemonitor.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.pinto.moviemonitor.data.source.local.database.MovieDao
import com.pinto.moviemonitor.data.source.local.database.MovieDatabase
import com.pinto.moviemonitor.data.source.local.models.MovieRemoteKeys
import com.pinto.moviemonitor.data.source.local.models.MovieResult
import com.pinto.moviemonitor.data.source.remote.api.MovieApi
import java.lang.Exception
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase,
) : RemoteMediator<Int, MovieResult>() {
    private val movieDao = movieDatabase.getMovieDao()
    private val movieRemoteKeyDao = movieDatabase.getMovieRemoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieResult>,
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage
                }
                LoadType.APPEND ->{
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }
            val response = movieApi.getMovieResults(page = currentPage)
            val endOfPaginationReached = response.results.isEmpty()
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH){
                    movieDao.clearAll()
                    movieRemoteKeyDao.deleteAllRemoteKeys()
                }

                val keys = response.results.map { movie ->
                    MovieRemoteKeys(
                        id = movie.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                movieRemoteKeyDao.insertAllRemoteKeys(remoteKeys = keys)
                movieDao.insertAll(movieList = response.results)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieResult>): MovieRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { movie ->
            movieRemoteKeyDao.getAllRemoteKeys(id = movie.id!!)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieResult>
    ): MovieRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { movie ->
            movieRemoteKeyDao.getAllRemoteKeys(id = movie.id!!)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieResult>,
    ):
            MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                movieRemoteKeyDao.getAllRemoteKeys(id = id)
            }
        }
    }
}