package br.com.davidmag.bingewatcher.data.source.local.contract

import br.com.davidmag.bingewatcher.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreLocalDatasource {
    fun get(): Flow<List<Genre>>
    suspend fun append(genres: List<Genre>)
    suspend fun cache(genres: List<Genre>)
}