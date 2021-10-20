package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.domain.model.Episode

object EpisodeLocalMapper : EntityMapper<Episode, EpisodeDb> {
    override val toDtoMapper: (Episode) -> EpisodeDb = { entity ->
        with(entity){
            EpisodeDb(
                id = id,
                showId = showId,
                name = name,
                summary = summary,
                season = season,
                number = number,
                imageOriginalUrl = imageOriginalUrl,
                imageMediumUrl = imageMediumUrl,
                premiered = premiered
            )
        }
    }

    override val toEntityMapper: (EpisodeDb) -> Episode = { dto ->
        with(dto){
            Episode(
                id = id,
                showId = showId,
                name = name,
                summary = summary,
                season = season,
                number = number,
                imageOriginalUrl = imageOriginalUrl,
                imageMediumUrl = imageMediumUrl,
                premiered = premiered
            )
        }
    }
}