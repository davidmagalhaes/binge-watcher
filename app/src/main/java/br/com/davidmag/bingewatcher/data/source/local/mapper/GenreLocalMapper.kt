package br.com.davidmag.bingewatcher.data.source.local.mapper

import br.com.davidmag.bingewatcher.data.common.EntityMapper
import br.com.davidmag.bingewatcher.data.source.local.dto.GenreDb
import br.com.davidmag.bingewatcher.domain.model.Genre

object GenreLocalMapper : EntityMapper<Genre, GenreDb> {
    override val toDtoMapper: (Genre) -> GenreDb = {
        GenreDb(it.id)
    }

    override val toEntityMapper: (GenreDb) -> Genre = {
        Genre(it.id)
    }
}