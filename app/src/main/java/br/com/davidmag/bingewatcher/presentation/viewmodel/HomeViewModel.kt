package br.com.davidmag.bingewatcher.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.usecase.GetShowUseCase
import br.com.davidmag.bingewatcher.domain.usecase.SearchShowUseCase
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.common.ExceptionWrapper
import br.com.davidmag.bingewatcher.presentation.common.launchOn
import br.com.davidmag.bingewatcher.presentation.common.toLiveData
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation

class HomeViewModel(
    private val showPresentationMapper: ShowPresentationMapper,
    private val getShowUseCase: GetShowUseCase,
    private val searchShowUseCase: SearchShowUseCase
) : BaseViewModel(){

    companion object {
        const val FAVORITE_STATE_DISABLED = 0
        const val FAVORITE_STATE_ENABLED = 1
    }

    private var showsSource : LiveData<out PagingData<ShowPresentation>>? = null

    val query = MutableLiveData<String>()
    val shows = MediatorLiveData<PagingData<ShowPresentation>>()
    val favoriteState = MutableLiveData<Int>()
    val errors = MediatorLiveData<ExceptionWrapper>()

    fun updateShows(){
        val isFavoriteEnabled = favoriteState.value == FAVORITE_STATE_ENABLED
        val queryString = "%${query.value.orEmpty()}%"

        showsSource?.let { shows.removeSource(it) }
        showsSource = getShowUseCase.execute(queryString, isFavoriteEnabled)
            .map { pagingData ->
                pagingData.map {
                    showPresentationMapper.parse(it).first()
                }
            }
            .toLiveData(shows)
    }

    fun submitSearch(query: String) {
        this.query.value = query

        searchShowUseCase.execute(query)
            .launchOn(errors) {
                ExceptionWrapper(
                    exception = it,
                    errorMessage = R.string.generic_error,
                    errorArgs = listOf(it.message)
                )
            }

        updateShows()
    }

    fun onSearchChange(query: String){
        if(query.isEmpty()){
            this.query.value = ""
            updateShows()
        }
    }

    fun showFavoritesClick(){
        favoriteState.value = when(favoriteState.value){
            FAVORITE_STATE_ENABLED -> FAVORITE_STATE_DISABLED
            else -> FAVORITE_STATE_ENABLED
        }

        updateShows()
    }
}