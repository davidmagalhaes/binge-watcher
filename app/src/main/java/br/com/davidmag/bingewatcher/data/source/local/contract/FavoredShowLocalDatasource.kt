package br.com.davidmag.bingewatcher.data.source.local.contract

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

interface FavoredShowLocalDatasource {
    fun upsert(show: Show) : Maybe<Any>
    fun delete(show: Show) : Maybe<Any>
    fun get(query : String) : DataSource.Factory<Int, Show>
}