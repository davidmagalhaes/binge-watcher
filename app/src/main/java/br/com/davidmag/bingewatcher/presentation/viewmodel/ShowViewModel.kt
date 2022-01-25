package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.usecase.*
import br.com.davidmag.bingewatcher.presentation.common.*
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import timber.log.Timber

class ShowViewModel(
    private val showMapper: ShowPresentationMapper,
    private val episodeMapper : EpisodePresentationMapper,
    private val getShowByIdUseCase: GetShowByIdUseCase,
    private val lookupShowUseCase: LookupShowUseCase,
    private val favoriteShowUseCase: FavoriteShowUseCase,
    private val fetchEpisodesUseCase: FetchEpisodesUseCase,
    private val getEpisodesUseCase : GetEpisodesUseCase
) : BaseViewModel() {

    companion object {
        const val ARG_SHOW = "showId"
    }

    private var showId : Long = 0
    private var selectedSeason = 0

    val show = MediatorLiveData<List<ShowPresentation>>()
    val episodes = MediatorLiveData<List<EpisodePresentation>>()
    val favoriteState = MediatorLiveData<Boolean>()
    val fatalError = MutableLiveData<ExceptionPresentation>()
    val error = MediatorLiveData<ExceptionPresentation>()

    override fun init(args: Bundle?) {
        try {
            showId = args?.getLong(ARG_SHOW) ?: error("missing argument: $ARG_SHOW")

            getShowByIdUseCase.execute(showId)
                .toPresentation(showMapper)
                .map {
                    favoriteState.postValue(it.firstOrNull()?.favored.orFalse())
                    it
                }
                .toLiveData(show)

            getEpisodesUseCase.execute(showId)
                .toPresentation(episodeMapper)
                .toLiveData(episodes)

            lookupShowUseCase.execute(showId)
                .launchOn(error) {
                    ExceptionPresentation(
                        exception = it,
                        errorMessage = R.string.error_internet
                    )
                }
        } catch (e : Exception) {
            Timber.e(e)
            fatalError.postValue(
                ExceptionPresentation(
                    exception = e,
                    errorMessage = R.string.generic_fatal_error,
                    errorArgs = listOf(e.message)
                )
            )
        }
    }

    fun selectSeason(season : Int) {
        if(selectedSeason != season){
            show.value?.firstOrNull()?.seasonsIds.orEmpty().ifNotEmpty { seasonsIds ->
                selectedSeason = season
                fetchEpisodesUseCase.execute(showId, seasonsIds[season - 1])
                    .launchOn(error) {
                        ExceptionPresentation(
                            exception = it,
                            errorMessage = R.string.error_internet
                        )
                    }
            }
        }
    }

    fun favorite() {
        val lastValue = favoriteState.value.orFalse()

        favoriteShowUseCase.execute(showId, lastValue)
            .map { !lastValue }
            .onErrorReturn { exception ->
                Timber.e(exception)
                error.postValue(ExceptionPresentation(
                    exception = exception,
                    errorMessage = R.string.generic_error,
                    errorArgs = listOf(exception.message)
                ))
                lastValue
            }
            .toLiveData(favoriteState)
    }
}