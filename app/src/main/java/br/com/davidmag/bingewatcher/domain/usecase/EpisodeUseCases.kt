package br.com.davidmag.bingewatcher.domain.usecase

import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import kotlinx.coroutines.flow.Flow

class FetchEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    suspend fun execute(showId : Long, season : Long)  {
        return episodeRepository.fetch(showId, season)
    }
}

class GetEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    fun execute(showId : Long) : Flow<List<Episode>> {
        return episodeRepository.get(showId)
    }
}