package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.map
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.usecase.GetGenresUseCase
import br.com.davidmag.bingewatcher.domain.usecase.GetShowUseCase
import br.com.davidmag.bingewatcher.domain.usecase.FetchShowUseCase
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.common.ExceptionPresentation
import br.com.davidmag.bingewatcher.presentation.common.launchOn
import br.com.davidmag.bingewatcher.presentation.common.toLiveData
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.GenrePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation

class HomeViewModel(
    private val showPresentationMapper: ShowPresentationMapper,
    private val getShowUseCase: GetShowUseCase,
    private val fetchShowUseCase: FetchShowUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel(){

    private var showsSource : LiveData<out PagingData<ShowPresentation>>? = null

    val query = MutableLiveData<String>()
    val shows = MediatorLiveData<PagingData<ShowPresentation>>()
    val genres = MediatorLiveData<List<GenrePresentation>>()
    val favoriteState = MutableLiveData<Boolean>()
    val errors = MediatorLiveData<ExceptionPresentation>()

    override fun init(args: Bundle?) {
        super.init(args)

        submitSearch("")

        getGenresUseCase.execute()
            .map { genreList ->
                genreList.map { GenrePresentation(it.id) }.toList()
            }
            .toLiveData(genres)
    }

    fun updateShows(){
        val isFavoriteEnabled = favoriteState.value.orFalse()
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

        fetchShowUseCase.execute(query)
            .doFinally {
                updateShows()
            }
            .launchOn(errors) {
                ExceptionPresentation(
                    exception = it,
                    errorMessage = R.string.generic_error,
                    errorArgs = listOf(it.message)
                )
            }
    }

    fun onSearchChange(query: String){
        if(query.isEmpty()){
            this.query.value = ""
            updateShows()
        }
    }

    fun showFavoritesClick(){
        favoriteState.value = !favoriteState.value.orFalse()
        updateShows()
    }
}


