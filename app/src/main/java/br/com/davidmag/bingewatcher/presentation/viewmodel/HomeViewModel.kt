package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.usecase.GetGenresUseCase
import br.com.davidmag.bingewatcher.domain.usecase.GetShowUseCase
import br.com.davidmag.bingewatcher.domain.usecase.FetchShowUseCase
import br.com.davidmag.bingewatcher.domain.util.NamedBooleanSwitches
import br.com.davidmag.bingewatcher.domain.util.NamedIntSwitches
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

    companion object {
        const val CONTENT_SHOW = "show"

        const val CONTENT_LOADING = 1
        const val CONTENT_READY = 0
    }

    private var showsSource : LiveData<out PagingData<ShowPresentation>>? = null
    private var intSwitches = NamedIntSwitches()

    val query = MutableLiveData<String>()
    val shows = MediatorLiveData<PagingData<ShowPresentation>>()
    val genres = MediatorLiveData<List<GenrePresentation>>()
    val favoriteState = MutableLiveData<Boolean>()

    val loadingStatus = MutableLiveData<NamedIntSwitches>()

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
        contentLoading(CONTENT_SHOW)
        updateShowsInternal()
    }

    fun submitSearch(query: String) {
        this.query.value = query

        contentLoading(CONTENT_SHOW)

        fetchShowUseCase.execute(query)
            .doFinally {
                updateShowsInternal()
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

    fun onLoadStatusChange(contentName: String, loadStatus: CombinedLoadStates, itemCount: Int) {
        val isNotLoading = loadStatus.append is LoadState.NotLoading &&
                loadStatus.prepend is LoadState.NotLoading &&
                loadStatus.refresh is LoadState.NotLoading

        if(isNotLoading) {
            if(itemCount < 1) {
                when(contentName) {
                    CONTENT_SHOW ->
                        shows.postValue(PagingData.from(listOf(ShowPresentation.empty())))
                }
            }
        }
    }

    override fun onContentStatusChange(contentName: String, loading: Boolean) {
        intSwitches.switch(contentName, if(loading) CONTENT_LOADING else CONTENT_READY )
        loadingStatus.postValue(intSwitches)
    }

    private fun updateShowsInternal() {
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
}


