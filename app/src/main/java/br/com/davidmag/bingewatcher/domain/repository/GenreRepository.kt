package br.com.davidmag.bingewatcher.domain.repository

import br.com.davidmag.bingewatcher.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun get() : Flow<List<Genre>>
}