package br.com.davidmag.bingewatcher.presentation.di

import br.com.davidmag.bingewatcher.domain.usecase.*
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.viewmodel.EpisodeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import br.com.davidmag.bingewatcher.presentation.viewmodel.ShowViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideHomeViewModel(
        showPresentationMapper: ShowPresentationMapper,
        getShowUseCase: GetShowUseCase,
        searchShowUseCase: SearchShowUseCase
    ) = HomeViewModel(
        showPresentationMapper,
        getShowUseCase,
        searchShowUseCase
    )

    @Provides
    fun provideShowViewModel(
        showPresentationMapper: ShowPresentationMapper,
        episodePresentationMapper: EpisodePresentationMapper,
        lookupShowUseCase: LookupShowUseCase,
        favoriteShowUseCase: FavoriteShowUseCase,
        fetchEpisodesUseCase: FetchEpisodesUseCase,
        getEpisodesUseCase: GetEpisodesUseCase
    ) = ShowViewModel(
        showPresentationMapper,
        episodePresentationMapper,
        lookupShowUseCase,
        favoriteShowUseCase,
        fetchEpisodesUseCase,
        getEpisodesUseCase
    )

    @Provides
    fun provideEpisodeViewModel() = EpisodeViewModel()
}