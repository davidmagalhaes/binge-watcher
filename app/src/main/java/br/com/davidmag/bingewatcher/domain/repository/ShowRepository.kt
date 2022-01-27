package br.com.davidmag.bingewatcher.domain.repository

import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Flowable
import io.reactivex.Maybe

interface ShowRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val FIRST_PAGE = 1
    }

    fun favorite(showId : Long, favorite : Boolean) : Maybe<Any>

    fun get(
        query: String,
        favorite : Boolean = false,
        pageSize : Int = DEFAULT_PAGE_SIZE
    ) : Flowable<PagingData<Show>>

    fun get(showId : Long) : Flowable<List<Show>>

    fun lookup(showId : Long) : Maybe<Any>

    fun fetch(page : Int = FIRST_PAGE, query: String = "") : Maybe<Int>
}