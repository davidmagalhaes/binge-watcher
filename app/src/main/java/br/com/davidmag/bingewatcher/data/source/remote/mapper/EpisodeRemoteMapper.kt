package br.com.davidmag.bingewatcher.data.source.remote.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.remote.dto.EpisodeResponse
import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.model.Show

object EpisodeRemoteMapper : EntityMapper<Episode, EpisodeResponse> {
    override val toDtoMapper: (Episode) -> EpisodeResponse
        get() = TODO("Not yet implemented")

    override val toEntityMapper: (EpisodeResponse) -> Episode = { episodeResponse ->
        with(episodeResponse) {
            Episode(
                id = id,
                name = name,
                premiered = premiered,
                summary = summary.orEmpty(),
                imageOriginalUrl = image?.original?.replace("http://", "https://"),
                imageMediumUrl = image?.medium?.replace("http://", "https://"),
                number = number,
                season = season
            )
        }
    }
}