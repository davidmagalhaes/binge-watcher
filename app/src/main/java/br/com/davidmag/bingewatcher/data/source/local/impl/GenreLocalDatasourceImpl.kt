package br.com.davidmag.bingewatcher.data.source.local.impl

import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.GenreDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.GenreLocalMapper
import br.com.davidmag.bingewatcher.domain.model.Genre
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GenreLocalDatasourceImpl(
    private val genreDao: GenreDao
): GenreLocalDatasource {
    override fun get(): Flow<List<Genre>> {
        return genreDao.get().map { genres ->
            GenreLocalMapper.toEntity(genres)
        }
    }

    override suspend fun cache(genres: List<Genre>) {
        genreDao.cache(GenreLocalMapper.toDto(genres))
    }

    override suspend fun append(genres: List<Genre>) {
        genreDao.upsert(GenreLocalMapper.toDto(genres))
    }
}