package br.com.davidmag.bingewatcher.data.source.local.contract

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface ShowLocalDatasource {
    fun get(query : String) : DataSource.Factory<Int, Show>
    fun get(showId: Long) : Flow<List<Show>>
    suspend fun append(shows : List<Show>)
    suspend fun cache(shows : List<Show>)
}