package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.usecase.FavoriteShowUseCase
import br.com.davidmag.bingewatcher.domain.usecase.FetchEpisodesUseCase
import br.com.davidmag.bingewatcher.domain.usecase.GetEpisodesUseCase
import br.com.davidmag.bingewatcher.domain.usecase.LookupShowUseCase
import br.com.davidmag.bingewatcher.presentation.common.*
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import timber.log.Timber

class ShowViewModel(
    private val showMapper: ShowPresentationMapper,
    private val episodeMapper : EpisodePresentationMapper,
    private val lookupShowUseCase: LookupShowUseCase,
    private val favoriteShowUseCase: FavoriteShowUseCase,
    private val fetchEpisodesUseCase: FetchEpisodesUseCase,
    private val getEpisodesUseCase : GetEpisodesUseCase
) : BaseViewModel() {

    companion object {
        const val ARG_SHOW = "showId"
        const val FAVORITE_STATE_DISABLED = 0
        const val FAVORITE_STATE_ENABLED = 1
        const val FAVORITE_STATE_LOADING = 2
    }

    private var showId : Long = 0
    private var selectedSeason = 1

    val show = MediatorLiveData<List<ShowPresentation>>()
    val episodes = MediatorLiveData<List<EpisodePresentation>>()
    val favoriteState = MutableLiveData<Int>()
    val fatalError = MutableLiveData<ExceptionWrapper>()
    val error = MediatorLiveData<ExceptionWrapper>()

    override fun init(args: Bundle?) {
        try{
            showId = args?.getLong(ARG_SHOW) ?: error("missing argument: $ARG_SHOW")

            fetchEpisodesUseCase.execute(showId)
                .launchOn(error)

            lookupShowUseCase.execute(showId)
                .map {
                    it.onEach { show ->
                        favoriteState.postValue(
                            if(show.favored) FAVORITE_STATE_ENABLED
                            else FAVORITE_STATE_DISABLED
                        )
                    }
                }
                .toPresentation(showMapper)
                .toLiveData(show)

            selectSeason(1)
        }
        catch (e : Exception){
            Timber.e(e)
            fatalError.postValue(
                ExceptionWrapper(
                    exception = e,
                    errorMessage = R.string.generic_fatal_error,
                    errorArgs = listOf(e.message)
                )
            )
        }
    }

    fun selectSeason(season : Int){
        selectedSeason = season
        getEpisodesUseCase.execute(showId, selectedSeason)
            .firstElement()
            .toPresentation(episodeMapper)
            .map {
               episodes.postValue(it)
            }
            .launchOn(error)
    }

    fun favorite() {
        if(favoriteState.value != FAVORITE_STATE_LOADING){
            val lastValue = favoriteState.value
            favoriteState.postValue(FAVORITE_STATE_LOADING)

            favoriteShowUseCase.execute(showId, lastValue != FAVORITE_STATE_ENABLED)
                .map {
                    if(lastValue == FAVORITE_STATE_ENABLED){
                        favoriteState.postValue(FAVORITE_STATE_DISABLED)
                    }
                    else{
                        favoriteState.postValue(FAVORITE_STATE_ENABLED)
                    }
                }
                .doOnError { exception ->
                    Timber.e(exception)
                    lastValue?.let {
                        favoriteState.postValue(it)
                    }
                }
                .launchOn(error)
        }
    }
}