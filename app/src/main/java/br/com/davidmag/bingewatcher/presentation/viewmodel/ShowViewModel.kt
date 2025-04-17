package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.domain.common.orFalse
import br.com.davidmag.bingewatcher.domain.usecase.*
import br.com.davidmag.bingewatcher.presentation.common.*
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ShowViewModel @Inject constructor(
    private val showMapper: ShowPresentationMapper,
    private val episodeMapper : EpisodePresentationMapper,
    private val getShowByIdUseCase: GetShowByIdUseCase,
    private val lookupShowUseCase: LookupShowUseCase,
    private val favoriteShowUseCase: FavoriteShowUseCase,
    private val fetchEpisodesUseCase: FetchEpisodesUseCase,
    private val getEpisodesUseCase : GetEpisodesUseCase
) : BaseViewModel() {

    private var showId : Long = 0
    private var selectedSeason = 0

    val show = mutableStateOf<List<ShowPresentation>>(emptyList())
    val episodes = mutableStateOf<PresentationResult<List<EpisodePresentation>>>(PresentationResult.ResultLoading())
    val favoriteState = mutableStateOf<PresentationResult<Boolean>>(PresentationResult.ResultLoading())
    val fatalError = mutableStateOf<ErrorPresentation?>(null)
    val error = mutableStateOf<ErrorPresentation?>(null)

    override fun init(args: Bundle?) {
        try {
            showId = args?.getLong(ARG_SHOW) ?: error("missing argument: $ARG_SHOW")

            getShowByIdUseCase.execute(showId)
                .mapToPresentation(showMapper)
                .launchAndCollect(viewModelScope) {
                    if (it is PresentationResult.ResultSuccess) {
                        favoriteState.value = PresentationResult.ResultSuccess(false)
                        show.value = it.data
                    }
                }

            getEpisodesUseCase.execute(showId)
                .mapToPresentation(episodeMapper)
                .launchAndCollect(viewModelScope, episodes)

            viewModelScope.launch {
                try {
                    lookupShowUseCase.execute(showId)
                } catch (e: Throwable) {
                    error.value = ErrorPresentation(R.string.error_internet)
                }
            }
        } catch (e : Exception) {
            Timber.e(e)
            fatalError.value =
                ErrorPresentation(
                    message = R.string.generic_fatal_error,
                    args = listOf(e.message)
                )
        }
    }

    fun selectSeason(season : Int) {
        if (selectedSeason != season){
            show.value.firstOrNull()?.seasonsIds.orEmpty().ifNotEmpty { seasonsIds ->
                selectedSeason = season
                viewModelScope.launch {
                    try {
                        fetchEpisodesUseCase.execute(showId, seasonsIds[season - 1])
                    } catch (e: Throwable) {
                        error.value = ErrorPresentation(R.string.error_internet)
                    }
                }
            }
        }
    }

    fun favorite() {
        val lastState = favoriteState.value

        favoriteState.value = PresentationResult.ResultLoading()

        viewModelScope.launch {
            favoriteState.value = try {
                val favorite = lastState.data?.let { !it }.orFalse()
                favoriteShowUseCase.execute(showId, favorite)
                PresentationResult.ResultSuccess(favorite)
            } catch (e: Throwable) {
                Timber.e(e)
                error.value = ErrorPresentation(
                    message = R.string.generic_error,
                    args = listOf(e.message)
                )
                lastState
            }
        }
    }

    companion object {
        const val ARG_SHOW = "showId"
    }
}