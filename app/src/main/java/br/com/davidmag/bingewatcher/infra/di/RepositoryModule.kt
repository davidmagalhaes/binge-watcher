package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.data.repository.EpisodeRepositoryImpl
import br.com.davidmag.bingewatcher.data.repository.GenreRepositoryImpl
import br.com.davidmag.bingewatcher.data.repository.ShowRepositoryImpl
import br.com.davidmag.bingewatcher.data.scheduler.AppSchedulers
import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.GenreLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import br.com.davidmag.bingewatcher.domain.repository.GenreRepository
import br.com.davidmag.bingewatcher.domain.repository.ShowRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideShowRepository(
        appSchedulers: AppSchedulers,
        showLocalDatasource: ShowLocalDatasource,
        favoredShowLocalDatasource: FavoredShowLocalDatasource,
        showRemoteDatasource: ShowRemoteDatasource,
        genreLocalDatasource: GenreLocalDatasource
    ) : ShowRepository = ShowRepositoryImpl(
        appSchedulers,
        showLocalDatasource,
        favoredShowLocalDatasource,
        showRemoteDatasource,
        genreLocalDatasource
    )

    @Singleton
    @Provides
    fun provideEpisodeRepository(
        appSchedulers: AppSchedulers,
        episodeRemoteDatasource: EpisodeRemoteDatasource,
        episodeLocalDatasource: EpisodeLocalDatasource
    ) : EpisodeRepository = EpisodeRepositoryImpl(
        appSchedulers,
        episodeRemoteDatasource,
        episodeLocalDatasource
    )

    @Singleton
    @Provides
    fun provideGenreRepository(
        genreLocalDatasource: GenreLocalDatasource
    ) : GenreRepository = GenreRepositoryImpl(genreLocalDatasource)
}