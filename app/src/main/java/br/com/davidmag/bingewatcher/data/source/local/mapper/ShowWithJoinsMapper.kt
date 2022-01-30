package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowWithJoins
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.model.Show

object ShowWithJoinsMapper : EntityMapper<Show, ShowWithJoins> {
    override val toEntityMapper: (ShowWithJoins) -> Show = { showAggr ->
        ShowLocalMapper.toEntity(showAggr.show).apply {
            favored = showAggr.favored.orFalse()
            genres = GenreLocalMapper.toEntity(showAggr.genresDb)
        }
    }

    override val toDtoMapper: (Show) -> ShowWithJoins
        get() = TODO("Not yet implemented")
}