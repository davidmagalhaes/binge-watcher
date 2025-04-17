package br.com.davidmag.bingewatcher.presentation.view.common

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import br.com.davidmag.bingewatcher.presentation.common.PresentationResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun <T: Any> PresentationResult<Flow<PagingData<T>>>.collectAsLazyPagingItems(): LazyPagingItems<T> {
    return if (this is PresentationResult.ResultSuccess) {
        data.collectAsLazyPagingItems()
    } else {
        emptyLazyPagingItems()
    }
}

@Composable
fun <T: Any> emptyLazyPagingItems() =
    emptyFlow<PagingData<T>>().collectAsLazyPagingItems()