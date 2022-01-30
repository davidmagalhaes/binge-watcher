package br.com.davidmag.bingewatcher.data.source.local.impl

import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.GenreDao
import br.com.davidmag.bingewatcher.data.source.local.dto.GenreDb
import br.com.davidmag.bingewatcher.data.source.local.mapper.GenreLocalMapper
import br.com.davidmag.bingewatcher.domain.model.Genre
import io.reactivex.Flowable
import io.reactivex.Maybe

class GenreLocalDatasourceImpl(
    private val genreDao: GenreDao
): GenreLocalDatasource {
    override fun get(): Flowable<List<Genre>> {
        return genreDao.get().map { genres ->
            GenreLocalMapper.toEntity(genres)
        }
    }

    override fun cache(genres: List<Genre>): Maybe<Any> {
        return Maybe.fromCallable {
            genreDao.cache(GenreLocalMapper.toDto(genres))
        }
    }

    override fun append(genres: List<Genre>): Maybe<Any> {
        return genreDao.upsert(GenreLocalMapper.toDto(genres)).map { Any() }
    }
}