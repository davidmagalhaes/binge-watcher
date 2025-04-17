package br.com.davidmag.bingewatcher.data.source.remote.contract

import br.com.davidmag.bingewatcher.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface ShowRemoteDatasource {
    suspend fun fetch(page : Int) : List<Show>
    suspend fun lookup(showId : Long) : Show
    suspend fun search(query : String, page: Int) : List<Show>
}