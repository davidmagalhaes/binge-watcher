package br.com.davidmag.bingewatcher.data.source.remote.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import br.com.davidmag.bingewatcher.domain.model.Show
import timber.log.Timber

@ExperimentalPagingApi
class IntRemoteMediator(
    private val firstPage : Int = 1,
    private val loadItems : suspend (Int) -> Boolean
) : RemoteMediator<Int, Show>() {

    private var currentPage = firstPage

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Show>,
    ): MediatorResult {
        try{
            val nextPage = when (loadType) {
                LoadType.REFRESH -> currentPage
                LoadType.PREPEND -> {
                    if(currentPage == firstPage) {
                        return MediatorResult.Success(
                            endOfPaginationReached = false
                        )
                    } else{
                        currentPage - 1
                    }
                }
                LoadType.APPEND -> {
                    val itemId = state.lastItemOrNull()?.id?.toInt()
                    if(itemId == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = false
                        )
                    } else {
                        ++currentPage
                    }
                }
            }

            val hasNextPage = loadItems(nextPage)

            return MediatorResult.Success(
                endOfPaginationReached = !hasNextPage
            )
        }
        catch (e : Exception){
            Timber.e(e)
            return MediatorResult.Error(e)
        }
    }
}