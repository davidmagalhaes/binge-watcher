package br.com.davidmag.bingewatcher.data.source.local.contract

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.domain.model.Show

interface FavoredShowLocalDatasource {
    suspend fun upsert(show: Show)
    suspend fun delete(show: Show)
    fun get(query : String) : DataSource.Factory<Int, Show>
}