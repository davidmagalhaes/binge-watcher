package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityDtoMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoredShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoredShowWithJoins
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.domain.model.Show

object FavoritedShowLocalMapper : EntityDtoMapper<Show, FavoredShowWithJoins, FavoredShowDb> {
    override val toDtoMapper: (Show) -> FavoredShowDb = {
        with(it){
            FavoredShowDb(ShowDb(
                id = id,
                name = name,
                time = time,
                days = days,
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

    override val toEntityMapper: (FavoredShowWithJoins) -> Show = {
        with(it.show.show){
            Show(
                id = id,
                name = name,
                time = time,
                days = days,
                imageMediumUrl = mediumImage,
                imageOriginalUrl = originalImage,
                summary = summary,
                status = status,
                genres = GenreLocalMapper.toEntity(it.genres),
                premiered = premiered,
                ratingAvg = ratingAvg,
                imageBackgroundUrl = backgroundImage,
                images = imageList,
                seasonsIds = seasonsIds
            )
        }
    }
}