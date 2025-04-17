package br.com.davidmag.bingewatcher.domain.repository

import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface ShowRepository {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
        const val FIRST_PAGE = 1
    }

    fun get(
        query: String,
        favorite : Boolean = false,
        pageSize : Int = DEFAULT_PAGE_SIZE
    ) : Flow<PagingData<Show>>

    fun get(showId : Long) : Flow<List<Show>>

    suspend fun favorite(showId : Long, favorite : Boolean)

    suspend fun lookup(showId : Long)

    suspend fun fetch(page : Int = FIRST_PAGE, query: String = "") : Int
}