package br.com.davidmag.bingewatcher.domain.usecase

import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import io.reactivex.Flowable
import io.reactivex.Maybe

class FetchShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(query : String) : Maybe<Any> {
        return showRepository.fetch(query = query).map { Any() }
    }
}

class LookupShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(showId : Long) : Maybe<Any> {
        return showRepository.lookup(showId)
    }
}

class GetShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(query : String = "%%", favoritesOnly : Boolean) : Flowable<PagingData<Show>> {
        return showRepository.get(query, favoritesOnly)
    }
}

class GetShowByIdUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(showId : Long) : Flowable<List<Show>> {
        return showRepository.get(showId)
    }
}

class FavoriteShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(showId : Long, favorite : Boolean) : Maybe<Any> {
        return showRepository.favorite(showId, favorite)
    }
}