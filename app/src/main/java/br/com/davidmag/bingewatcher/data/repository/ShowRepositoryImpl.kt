package br.com.davidmag.bingewatcher.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchers
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.util.IntRemoteMediator
import br.com.davidmag.bingewatcher.domain.common.orZero
import br.com.davidmag.bingewatcher.domain.exception.EntityNotFoundException
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.HttpURLConnection

class ShowRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val showLocalDatasource: ShowLocalDatasource,
    private val favoredShowLocalDatasource: FavoredShowLocalDatasource,
    private val showRemoteDatasource: ShowRemoteDatasource,
    private val genreLocalDatasource: GenreLocalDatasource
) : ShowRepository {

    override suspend fun favorite(showId: Long, favorite: Boolean) {
        CoroutineScope(appDispatchers.network()).launch {
            val shows = showLocalDatasource.get(showId).firstOrNull().orEmpty()

            if(shows.isNotEmpty()){
                val show = shows.first()
                withContext(appDispatchers.database()) {
                    if (favorite)
                        favoredShowLocalDatasource.upsert(show)
                    else
                        favoredShowLocalDatasource.delete(show)
                }
            } else{
                throw EntityNotFoundException("No Show found for id: $showId")
            }
        }.join()
    }

    @ExperimentalPagingApi
    override fun get(
        query: String,
        favorite : Boolean,
        pageSize : Int
    ): Flow<PagingData<Show>> {
        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = 10,
                initialLoadSize = pageSize
            ),
            1,
            IntRemoteMediator { fetch(it, query).orZero() == 0 },
            if(favorite)
                favoredShowLocalDatasource.get(query).asPagingSourceFactory()
            else
                showLocalDatasource.get(query).asPagingSourceFactory(),
        ).flow.cachedIn(GlobalScope)
    }

    override fun get(showId: Long): Flow<List<Show>> {
        return showLocalDatasource.get(showId)
    }

    override suspend fun lookup(showId: Long) {
        val show = showRemoteDatasource.lookup(showId)

        genreLocalDatasource.append(show.genres)
        showLocalDatasource.append(listOf(show))
    }

    override suspend fun fetch(page: Int, query: String): Int {
        val deferred: Deferred<Int> = CoroutineScope(appDispatchers.network()).async {
            try {
                val shows = when {
                    query.isEmpty() || query == "%%" -> showRemoteDatasource.fetch(page)
                    else -> showRemoteDatasource.search(query.replace("%", ""), page)
                }

                val genres = shows.map { it.genres }.flatten().toHashSet().toList()

                withContext(appDispatchers.database()) {
                    if (page == 1) {
                        genreLocalDatasource.cache(genres)
                        showLocalDatasource.cache(shows)
                    }
                    else {
                        genreLocalDatasource.append(genres)
                        showLocalDatasource.append(shows)
                    }
                }

                shows.size
            } catch (e: HttpException) {
                if (e.code() == HttpURLConnection.HTTP_NOT_FOUND) 0 else throw e
            }
        }

        return deferred.await()
    }
}