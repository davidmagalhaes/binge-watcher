package br.com.davidmag.bingewatcher.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.RemoteMediator
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import br.com.davidmag.bingewatcher.data.scheduler.AppSchedulers
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.util.IntRemoteMediator
import br.com.davidmag.bingewatcher.domain.common.onErrorMap
import br.com.davidmag.bingewatcher.domain.common.orZero
import br.com.davidmag.bingewatcher.domain.exception.EntityNotFoundException
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository.Companion.DEFAULT_PAGE_SIZE
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.rx2.await
import retrofit2.HttpException
import java.net.HttpURLConnection

class ShowRepositoryImpl(
    private val appSchedulers: AppSchedulers,
    private val showLocalDatasource: ShowLocalDatasource,
    private val favoredShowLocalDatasource: FavoredShowLocalDatasource,
    private val showRemoteDatasource: ShowRemoteDatasource,
    private val genreLocalDatasource: GenreLocalDatasource
) : ShowRepository {

    override fun favorite(showId: Long, favorite: Boolean): Maybe<Any> {
        return showLocalDatasource.get(showId)
            .subscribeOn(appSchedulers.database())
            .firstElement()
            .flatMap {
                if(it.isNotEmpty()){
                    val show = it.first()
                    if(favorite)
                        favoredShowLocalDatasource.upsert(show)
                            .subscribeOn(appSchedulers.database())
                    else
                        favoredShowLocalDatasource.delete(show)
                            .subscribeOn(appSchedulers.database())
                } else{
                    throw EntityNotFoundException("Show with id: $showId")
                }
            }
            .observeOn(appSchedulers.main())
    }

    @ExperimentalPagingApi
    override fun get(
        query: String,
        favorite : Boolean,
        pageSize : Int
    ): Flowable<PagingData<Show>> {
        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = 10,
                initialLoadSize = pageSize
            ),
            1,
            IntRemoteMediator {
                val count = fetch(it, query)
                    .blockingGet()
                    .orZero()

                RemoteMediator.MediatorResult.Success(
                    endOfPaginationReached = count < DEFAULT_PAGE_SIZE
                )
            },
            if(favorite)
                favoredShowLocalDatasource.get(query).asPagingSourceFactory()
            else
                showLocalDatasource.get(query).asPagingSourceFactory(),
        ).flowable.cachedIn(GlobalScope)
    }

    override fun get(showId: Long): Flowable<List<Show>> {
        return showLocalDatasource.get(showId)
            .subscribeOn(appSchedulers.database())
            .observeOn(appSchedulers.main())
    }

    override fun lookup(showId: Long): Maybe<Any> {
        return showRemoteDatasource.lookup(showId)
            .subscribeOn(appSchedulers.network())
            .flatMap { shows ->
                genreLocalDatasource.append(
                    shows.map { it.genres }.flatten().toHashSet().toList()
                ).subscribeOn(appSchedulers.database())
                    .map { shows }
            }.flatMap {
                showLocalDatasource.append(it)
                    .subscribeOn(appSchedulers.database())
            }
            .observeOn(appSchedulers.main())
    }

    override fun fetch(page: Int, query: String): Maybe<Int> {
        return when {
            query.isEmpty() || query == "%%" -> showRemoteDatasource.fetch(page)
            else -> showRemoteDatasource.search(query, page)
        }.subscribeOn(appSchedulers.network())
            .flatMap { shows ->
                val genres = shows.map { it.genres }.flatten().toHashSet().toList()

                val maybe = if(page == 1) genreLocalDatasource.cache(genres) else
                    genreLocalDatasource.append(genres)

                maybe.subscribeOn(appSchedulers.database()).map { shows }
            }.flatMap { shows ->
                val maybe = if(page == 1) showLocalDatasource.cache(shows) else
                    showLocalDatasource.append(shows)

                maybe.subscribeOn(appSchedulers.database()).map { shows }
            }.onErrorMap { e ->
                if(e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND)
                    emptyList()
                else
                    throw e
            }.map { it.size }.observeOn(appSchedulers.main())
    }
}