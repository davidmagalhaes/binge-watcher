package br.com.davidmag.bingewatcher.domain.usecase

import br.com.davidmag.bingewatcher.domain.model.Genre
import br.com.davidmag.bingewatcher.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow

class GetGenresUseCase(
    private val genreRepository: GenreRepository
) {
    fun execute() : Flow<List<Genre>> {
        return genreRepository.get()
    }
}