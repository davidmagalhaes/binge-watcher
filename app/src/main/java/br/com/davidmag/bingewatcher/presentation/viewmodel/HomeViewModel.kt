package br.com.davidmag.bingewatcher.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.usecase.GetGenresUseCase
import br.com.davidmag.bingewatcher.domain.usecase.GetShowUseCase
import br.com.davidmag.bingewatcher.domain.usecase.FetchShowUseCase
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.common.ErrorPresentation
import br.com.davidmag.bingewatcher.presentation.common.PresentationResult
import br.com.davidmag.bingewatcher.presentation.common.launchAndCollect
import br.com.davidmag.bingewatcher.presentation.common.toPresentationPage
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.GenrePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val showPresentationMapper: ShowPresentationMapper,
    private val getShowUseCase: GetShowUseCase,
    private val fetchShowUseCase: FetchShowUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel(){

    val query = mutableStateOf("")
    val shows = mutableStateOf<PresentationResult<Flow<PagingData<ShowPresentation>>>>(PresentationResult.ResultLoading())
    val genres = mutableStateOf<List<GenrePresentation>>(emptyList())
    val favoriteState = mutableStateOf<PresentationResult<Boolean>>(PresentationResult.ResultSuccess(false))
    val errors = mutableStateOf<ErrorPresentation?>(null)

    init {
        getGenresUseCase.execute()
            .map { genreList ->
                genreList.map { GenrePresentation(it.id) }.toList()
            }
            .launchAndCollect(viewModelScope, genres)

        updateShows()
    }

    fun updateShows(){
        shows.value = PresentationResult.ResultSuccess(paginateShows())
    }

    fun submitSearch() {
        shows.value = PresentationResult.ResultLoading()
        viewModelScope.launch {
            fetchShowUseCase.execute(query.value)
            updateShows()
        }
    }

    fun onSearchChange(query: String){
        this.query.value = query
    }

    fun showFavoritesClick(){
        val lastVal = favoriteState.value.data.orFalse()
        favoriteState.value = PresentationResult.ResultLoading()
        shows.value = PresentationResult.ResultSuccess(
            paginateShows().map {
                favoriteState.value = PresentationResult.ResultSuccess(!lastVal)
                it
            }
        )
    }

    private fun paginateShows(): Flow<PagingData<ShowPresentation>> {
        val isFavoriteEnabled = favoriteState.value.data.orFalse()
        val queryString = "%${query.value}%"

        return getShowUseCase.execute(queryString, isFavoriteEnabled)
            .toPresentationPage(showPresentationMapper)
    }
}


