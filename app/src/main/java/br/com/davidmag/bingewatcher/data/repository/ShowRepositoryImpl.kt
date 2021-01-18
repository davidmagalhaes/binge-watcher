package br.com.davidmag.bingewatcher.data.repository

import androidx.paging.*
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.flowable
import br.com.davidmag.bingewatcher.data.scheduler.AppSchedulers
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.util.IntRemoteMediator
import br.com.davidmag.bingewatcher.domain.exception.EntityNotFoundException
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import io.reactivex.Flowable
import io.reactivex.Maybe
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.rx2.await

class ShowRepositoryImpl(
    private val appSchedulers: AppSchedulers,
    private val showLocalDatasource: ShowLocalDatasource,
    private val favoredShowLocalDatasource: FavoredShowLocalDatasource,
    private val showRemoteDatasource: ShowRemoteDatasource
) : ShowRepository {
    override fun favorite(showId: Long, favorite: Boolean): Maybe<Any> {
        return showLocalDatasource.lookup(showId)
            .subscribeOn(appSchedulers.database())
            .flatMap {
                if(it.isNotEmpty()){
                    val show = it.first()
                    if(favorite)
                        favoredShowLocalDatasource.upsert(show)
                    else
                        favoredShowLocalDatasource.delete(show)
                }
                else{
                    throw EntityNotFoundException("showId: $showId")
                }
            }
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
                prefetchDistance = 5,
                initialLoadSize = 10
            ),
            1,
            IntRemoteMediator {
                val shows = showRemoteDatasource.fetch(it)
                    .subscribeOn(appSchedulers.network())
                    .flatMap { shows ->
                        showLocalDatasource.append(shows).map { shows }
                    }
                    .await()

                RemoteMediator.MediatorResult.Success(
                    endOfPaginationReached = shows.orEmpty().isEmpty()
                )
            },
            (if(favorite) favoredShowLocalDatasource.get(query) else showLocalDatasource.get(query))
                .asPagingSourceFactory(),
        ).flowable.cachedIn(GlobalScope)
    }

    override fun lookup(showId: Long): Maybe<List<Show>> {
        return showLocalDatasource.lookup(showId)
            .subscribeOn(appSchedulers.database())
    }

    override fun fetch(page: Int): Maybe<Any> {
        return showRemoteDatasource.fetch(page)
            .subscribeOn(appSchedulers.network())
            .flatMap {
                showLocalDatasource.append(it)
                    .subscribeOn(appSchedulers.database())
            }
    }

    override fun search(query: String): Maybe<Any> {
        return showRemoteDatasource.search(query)
            .subscribeOn(appSchedulers.network())
            .flatMap { shows ->
                showLocalDatasource.append(shows)
                    .subscribeOn(appSchedulers.database())
                    .map { Any() }
            }
    }
}