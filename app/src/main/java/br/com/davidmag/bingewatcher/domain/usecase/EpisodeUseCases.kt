package br.com.davidmag.bingewatcher.domain.usecase

import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import io.reactivex.Flowable
import io.reactivex.Maybe

class FetchEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    fun execute(showId : Long) : Maybe<Any> {
        return episodeRepository.fetch(showId)
    }
}

class GetEpisodesUseCase(
    private val episodeRepository: EpisodeRepository
) {
    fun execute(showId : Long, season : Int) : Flowable<List<Episode>> {
        return episodeRepository.get(showId, season)
    }
}