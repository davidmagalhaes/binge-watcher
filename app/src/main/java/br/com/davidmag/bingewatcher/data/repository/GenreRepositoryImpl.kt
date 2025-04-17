package br.com.davidmag.bingewatcher.data.repository

import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.domain.model.Genre
import br.com.davidmag.bingewatcher.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow

class GenreRepositoryImpl(
    private val genreLocalDatasource: GenreLocalDatasource
) : GenreRepository {
    override fun get(): Flow<List<Genre>> {
        return genreLocalDatasource.get()
    }
}