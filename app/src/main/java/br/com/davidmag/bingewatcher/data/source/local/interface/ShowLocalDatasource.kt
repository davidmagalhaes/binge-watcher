package br.com.davidmag.bingewatcher.data.source.local.`interface`

import br.com.davidmag.bingewatcher.domain.aggr.ShowAggregate
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

interface ShowLocalDatasource {
    fun favorite(showId : Long) : Maybe<Any>
    fun unfavorite(showId : Long) : Maybe<Any>
    fun get(onlyFavorites : Boolean)
    fun lookup(showId: Long) : Maybe<List<ShowAggregate>>
    fun append(shows : List<Show>) : Maybe<Any>
    fun cache(shows : List<Show>) : Maybe<Any>
}