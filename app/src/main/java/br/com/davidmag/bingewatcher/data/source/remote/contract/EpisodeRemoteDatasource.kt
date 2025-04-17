package br.com.davidmag.bingewatcher.data.source.remote.contract

import br.com.davidmag.bingewatcher.domain.model.Episode
import kotlinx.coroutines.flow.Flow

interface EpisodeRemoteDatasource {
    suspend fun fetch(showId : Long, seasonId : Long) : List<Episode>
}