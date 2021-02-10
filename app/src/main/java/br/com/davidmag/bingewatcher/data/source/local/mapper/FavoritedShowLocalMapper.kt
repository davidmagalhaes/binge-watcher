package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoredShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.domain.model.Show

object FavoritedShowLocalMapper : EntityMapper<Show, FavoredShowDb> {
    override val toDtoMapper: (Show) -> FavoredShowDb = {
        with(it){
            FavoredShowDb(ShowDb(
                id = id,
                name = name,
                time = time,
                days = days,
                genres = genres,
                mediumImage = imageMediumUrl,
                originalImage = imageOriginalUrl,
                summary = summary,
                status = status,
                premiered = premiered,
                ratingAvg = ratingAvg,
                backgroundImage = imageBackgroundUrl,
                imageList = images,
                seasonsIds = seasonsIds
            ))
        }
    }

    override val toEntityMapper: (FavoredShowDb) -> Show = {
        with(it.show){
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
                premiered = premiered,
                ratingAvg = ratingAvg,
                imageBackgroundUrl = backgroundImage,
                images = imageList,
                seasonsIds = it.show.seasonsIds
            )
        }
    }
}