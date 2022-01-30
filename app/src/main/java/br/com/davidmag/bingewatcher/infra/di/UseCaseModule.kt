package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import br.com.davidmag.bingewatcher.domain.repository.GenreRepository
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import br.com.davidmag.bingewatcher.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {
    @Singleton
    @Provides
    fun provideGetShowUseCase(
        showRepository: ShowRepository
    ) = GetShowUseCase(showRepository)

    @Singleton
    @Provides
    fun provideFavoriteShowUseCase(
        showRepository: ShowRepository
    ) = FavoriteShowUseCase(showRepository)

    @Singleton
    @Provides
    fun provideSearchShowUseCase(
        showRepository: ShowRepository
    ) = FetchShowUseCase(showRepository)

    @Singleton
    @Provides
    fun provideLookupShowUseCase(
        showRepository: ShowRepository
    ) = LookupShowUseCase(showRepository)

    @Singleton
    @Provides
    fun provideFetchEpisodesUseCase(
        episodeRepository: EpisodeRepository
    ) = FetchEpisodesUseCase(episodeRepository)

    @Singleton
    @Provides
    fun provideGetEpisodesUseCase(
        episodeRepository: EpisodeRepository
    ) = GetEpisodesUseCase(episodeRepository)

    @Singleton
    @Provides
    fun provideGetShowByIdUseCase(
        showRepository: ShowRepository
    ) = GetShowByIdUseCase(showRepository)

    @Singleton
    @Provides
    fun providesGetGenresUseCase(
        genreRepository: GenreRepository
    ) = GetGenresUseCase(genreRepository)
}