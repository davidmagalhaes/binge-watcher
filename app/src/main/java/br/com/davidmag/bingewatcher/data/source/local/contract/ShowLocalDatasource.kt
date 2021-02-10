package br.com.davidmag.bingewatcher.data.source.local.contract

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Flowable
import io.reactivex.Maybe

interface ShowLocalDatasource {
    fun get(query : String) : DataSource.Factory<Int, Show>
    fun get(showId: Long) : Flowable<List<Show>>
    fun append(shows : List<Show>) : Maybe<Any>
    fun cache(shows : List<Show>) : Maybe<Any>
}