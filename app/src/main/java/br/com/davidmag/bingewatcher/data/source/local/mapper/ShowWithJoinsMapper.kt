package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowWithJoins
import br.com.davidmag.bingewatcher.domain.model.Show

object ShowWithJoinsMapper : EntityMapper<Show, ShowWithJoins> {
    override val toEntityMapper: (ShowWithJoins) -> Show = {
         ShowLocalMapper.toEntity(it.show).apply {
             favored = it.favored
             seasonsCount = it.seasonCount
         }
    }

    override val toDtoMapper: (Show) -> ShowWithJoins
        get() = TODO("Not yet implemented")
}