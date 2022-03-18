package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.filter
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
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
import kotlinx.coroutines.reactive.awaitLast
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val showPresentationMapper: ShowPresentationMapper,
    private val getShowUseCase: GetShowUseCase,
    private val fetchShowUseCase: FetchShowUseCase,
    private val getGenresUseCase: GetGenresUseCase
) : BaseViewModel(){

    companion object {
        const val CONTENT_SHOW = "show"
        const val CONTENT_STARTING = 1
        const val CONTENT_STARTED = 0
    }

    private var showsSource : LiveData<out PagingData<ShowPresentation>>? = null

    val query = MutableLiveData<String>()
    val shows = MediatorLiveData<PagingData<ShowPresentation>>()
    val genres = MediatorLiveData<List<GenrePresentation>>()
    val favoriteState = MutableLiveData<Boolean>()

    var showStatus = CONTENT_STARTING

    override fun init(args: Bundle?) {
        super.init(args)

        contentStarting(CONTENT_SHOW)

        submitSearch("")

        getGenresUseCase.execute()
            .map { genreList ->
                genreList.map { GenrePresentation(it.id) }.toList()
            }
            .toLiveData(genres)
    }

    fun updateShows() {
        contentLoading(CONTENT_SHOW)
        updateShowsInternal()
    }

    fun submitSearch(query: String) {
        this.query.value = query

        contentLoading(CONTENT_SHOW)

        fetchShowUseCase.execute(query)
            .doAfterSuccess {
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

    fun onSearchChange(query: String) {
        if(query.isEmpty()){
            submitSearch("")
        }
    }

    fun showFavoritesClick() {
        favoriteState.value = !favoriteState.value.orFalse()
        updateShows()
    }

    fun onLoadStatusChange(contentName: String, loadStatus: CombinedLoadStates, itemCount: Int) {
        loadStatusChanged(
            shows,
            contentName,
            ShowPresentation.loading(),
            ShowPresentation.empty(),
            loadStatus,
            itemCount
        )
    }

    override fun onContentReady(contentName: String): Boolean {
        if(contentName == CONTENT_SHOW) showStatus = CONTENT_STARTED

        return true
    }

    private fun updateShowsInternal() {
        val isFavoriteEnabled = favoriteState.value.orFalse()
        val queryString = "%${query.value.orEmpty()}%"

        showsSource?.let { shows.removeSource(it) }
        showsSource = getShowUseCase.execute(queryString, isFavoriteEnabled)
            .map { pagingData ->
                pagingData
                    .map {
                        showPresentationMapper.parse(it).first()
                    }
            }
            .toLiveData(shows)
    }
}


