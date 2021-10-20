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
import retrofit2.HttpException
import java.net.HttpURLConnection

class ShowRepositoryImpl(
    private val appSchedulers: AppSchedulers,
    private val showLocalDatasource: ShowLocalDatasource,
    private val favoredShowLocalDatasource: FavoredShowLocalDatasource,
    private val showRemoteDatasource: ShowRemoteDatasource
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
                }
                else{
                    throw EntityNotFoundException("Show with id: $showId")
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
                prefetchDistance = 10,
                initialLoadSize = pageSize
            ),
            1,
            IntRemoteMediator {
                val shows = showRemoteDatasource.fetch(it)
                    .subscribeOn(appSchedulers.network())
                    .flatMap { shows ->
                        showLocalDatasource.append(shows).map { shows }
                    }
                    .onErrorReturn { e ->
                        if(e is HttpException && e.code() == HttpURLConnection.HTTP_NOT_FOUND)
                            emptyList()
                        else
                            throw e
                    }
                    .await()

                RemoteMediator.MediatorResult.Success(
                    endOfPaginationReached = shows.orEmpty().isEmpty()
                )
            },
            (if(favorite) favoredShowLocalDatasource.get(query)
                else showLocalDatasource.get(query)).asPagingSourceFactory(),
        ).flowable.cachedIn(GlobalScope)
    }

    override fun get(showId: Long): Flowable<List<Show>> {
        return showLocalDatasource.get(showId)
            .subscribeOn(appSchedulers.database())
    }

    override fun lookup(showId: Long): Maybe<Any> {
        return showRemoteDatasource.lookup(showId)
            .subscribeOn(appSchedulers.network())
            .flatMap {
                showLocalDatasource.append(it)
            }
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