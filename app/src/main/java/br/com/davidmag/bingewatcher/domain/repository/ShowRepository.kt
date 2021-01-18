package br.com.davidmag.bingewatcher.domain.repository

import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Flowable
import io.reactivex.Maybe

interface ShowRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 250
    }

    fun favorite(showId : Long, favorite : Boolean) : Maybe<Any>

    fun get(
        query: String,
        favorite : Boolean = false,
        pageSize : Int = DEFAULT_PAGE_SIZE
    ) : Flowable<PagingData<Show>>

    fun lookup(showId : Long) : Maybe<List<Show>>

    fun fetch(page : Int) : Maybe<Any>

    fun search(query : String) : Maybe<Any>
}