package br.com.davidmag.bingewatcher.domain.usecase

import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.model.Show
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchShowUseCase(
    private val showRepository: ShowRepository
) {
    suspend fun execute(query : String) {
        showRepository.fetch(query = query)
    }
}

class LookupShowUseCase(
    private val showRepository: ShowRepository
) {
    suspend fun execute(showId : Long) {
        showRepository.lookup(showId)
    }
}

class GetShowUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(query : String = "%%", favoritesOnly : Boolean) : Flow<PagingData<Show>> {
        return showRepository.get(query, favoritesOnly)
    }
}

class GetShowByIdUseCase(
    private val showRepository: ShowRepository
) {
    fun execute(showId : Long) : Flow<List<Show>> {
        return showRepository.get(showId)
    }
}

class FavoriteShowUseCase(
    private val showRepository: ShowRepository
) {
    suspend fun execute(showId : Long, favorite : Boolean) {
        showRepository.favorite(showId, favorite)
    }
}