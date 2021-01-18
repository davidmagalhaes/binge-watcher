package br.com.davidmag.bingewatcher.data.source.remote.impl

import br.com.davidmag.bingewatcher.data.source.remote.api.EpisodeApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.mapper.EpisodeRemoteMapper
import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Maybe

class EpisodeRemoteDatasourceImpl(
    private val episodeApi: EpisodeApi
) : EpisodeRemoteDatasource {
    override fun fetch(showId: Long): Maybe<List<Episode>> {
        return episodeApi.fetch(showId).map { responseList ->
            EpisodeRemoteMapper.toEntity(responseList).onEach {
                it.showId = showId
            }
        }
    }
}