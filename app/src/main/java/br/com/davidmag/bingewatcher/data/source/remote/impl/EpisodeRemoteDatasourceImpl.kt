package br.com.davidmag.bingewatcher.data.source.remote.impl

import br.com.davidmag.bingewatcher.data.source.remote.api.EpisodeApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.mapper.EpisodeRemoteMapper
import br.com.davidmag.bingewatcher.domain.model.Episode

class EpisodeRemoteDatasourceImpl(
    private val episodeApi: EpisodeApi
) : EpisodeRemoteDatasource {
    override suspend fun fetch(showId : Long, seasonId: Long): List<Episode> {
        return EpisodeRemoteMapper.toEntity(
            episodeApi.fetch(seasonId).onEach { it.showId = showId }
        )
    }
}