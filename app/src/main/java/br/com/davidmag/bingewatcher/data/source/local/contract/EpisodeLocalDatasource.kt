package br.com.davidmag.bingewatcher.data.source.local.contract

import br.com.davidmag.bingewatcher.domain.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeLocalDatasource {
    fun get(showId : Long) : Flow<List<Episode>>
    suspend fun append(episodes : List<Episode>)
    suspend fun cache(episodes : List<Episode>)
}