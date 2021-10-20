package br.com.davidmag.bingewatcher.data.source.remote.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import br.com.davidmag.bingewatcher.domain.model.Show
import timber.log.Timber

@ExperimentalPagingApi
class IntRemoteMediator(
    private val loadItems : suspend (Int) -> MediatorResult
) : RemoteMediator<Int, Show>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Show>,
    ): MediatorResult {
        try{
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND ->
                    state.lastItemOrNull()?.id?.toInt()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
            }

            return loadItems(loadKey + 1)
        }
        catch (e : Exception){
            Timber.e(e)
            return MediatorResult.Error(e)
        }
    }
}