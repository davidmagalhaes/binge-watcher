package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.domain.model.Show

object ShowLocalMapper : EntityMapper<Show, ShowDb> {
    override val toDtoMapper: (Show) -> ShowDb = {
        with(it){
            ShowDb(
                id = id,
                name = name,
                time = time,
                days = days,
                genres = genres,
                mediumImage = imageMediumUrl,
                originalImage = imageOriginalUrl,
                summary = summary,
                status = status,
                ratingAvg = ratingAvg,
                premiered = premiered
            )
        }
    }

    override val toEntityMapper: (ShowDb) -> Show = {
        with(it){
            Show(
                id = id,
                name = name,
                time = time,
                days = days,
                genres = genres,
                imageMediumUrl = mediumImage,
                imageOriginalUrl = originalImage,
                summary = summary,
                status = status,
                ratingAvg = ratingAvg,
                premiered = premiered
            )
        }
    }
}