package br.com.davidmag.bingewatcher.data.source.remote.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.remote.dto.ShowResponse
import br.com.davidmag.bingewatcher.domain.model.Genre
import br.com.davidmag.bingewatcher.domain.model.Show

object ShowRemoteMapper : EntityMapper<Show, ShowResponse> {
    override val toDtoMapper: (Show) -> ShowResponse
        get() = error("Not implemented")

    override val toEntityMapper: (ShowResponse) -> Show = { resp ->
        with(resp){
            Show(
                id = id,
                name = name,
                summary = summary.orEmpty(),
                //By default, Android doesn't allow HTTP calls,
                imageMediumUrl = image?.medium?.replace("http://", "https://"),
                imageOriginalUrl = image?.original?.replace("http://", "https://"),
                genres = genres.map { Genre(it) },
                days = schedule?.days.orEmpty(),
                time = schedule?.time.orEmpty(),
                status = status,
                ratingAvg = rating?.average ?: 0.0,
                premiered = premiered,
                imageBackgroundUrl = null,
                seasonsIds = emptyList()
            )
        }
    }
}