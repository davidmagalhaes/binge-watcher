package br.com.davidmag.bingewatcher.domain.usecase

import br.com.davidmag.bingewatcher.domain.model.Genre
import br.com.davidmag.bingewatcher.domain.repository.GenreRepository
import io.reactivex.Flowable

class GetGenresUseCase(
    private val genreRepository: GenreRepository
) {
    fun execute() : Flowable<List<Genre>> {
        return genreRepository.get()
    }
}