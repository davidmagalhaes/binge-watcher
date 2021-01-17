package br.com.davidmag.bingewatcher.domain.usecase

import br.com.davidmag.bingewatcher.domain.repo.ShowRepository
import io.reactivex.Maybe

class FavoriteShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(showId : Long, favorite : Boolean) : Maybe<Any> {
        return showRepository.favorite(showId, favorite)
    }
}