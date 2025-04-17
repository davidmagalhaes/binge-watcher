package br.com.davidmag.bingewatcher.domain.repository

import br.com.davidmag.bingewatcher.domain.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun get(showId : Long) : Flow<List<Episode>>
    suspend fun fetch(showId : Long, seasonId : Long)
}