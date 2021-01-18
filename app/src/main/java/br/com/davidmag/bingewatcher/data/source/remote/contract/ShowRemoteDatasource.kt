package br.com.davidmag.bingewatcher.data.source.remote.contract

import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

interface ShowRemoteDatasource {
    fun fetch(page : Int) : Maybe<List<Show>>
    fun lookup(showId : Long) : Maybe<List<Show>>
    fun search(query : String) : Maybe<List<Show>>
    fun seasons(showId : Long) : Maybe<List<Any>>
}