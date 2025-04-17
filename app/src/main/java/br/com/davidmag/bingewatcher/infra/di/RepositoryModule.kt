package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.data.repository.EpisodeRepositoryImpl
import br.com.davidmag.bingewatcher.data.repository.GenreRepositoryImpl
import br.com.davidmag.bingewatcher.data.repository.ShowRepositoryImpl
import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchers
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
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideShowRepository(
        appDispatchers: AppDispatchers,
        showLocalDatasource: ShowLocalDatasource,
        favoredShowLocalDatasource: FavoredShowLocalDatasource,
        showRemoteDatasource: ShowRemoteDatasource,
        genreLocalDatasource: GenreLocalDatasource
    ) : ShowRepository = ShowRepositoryImpl(
        appDispatchers,
        showLocalDatasource,
        favoredShowLocalDatasource,
        showRemoteDatasource,
        genreLocalDatasource
    )

    @Singleton
    @Provides
    fun provideEpisodeRepository(
        appDispatchers: AppDispatchers,
        episodeRemoteDatasource: EpisodeRemoteDatasource,
        episodeLocalDatasource: EpisodeLocalDatasource
    ) : EpisodeRepository = EpisodeRepositoryImpl(
        appDispatchers,
        episodeRemoteDatasource,
        episodeLocalDatasource
    )

    @Singleton
    @Provides
    fun provideGenreRepository(
        genreLocalDatasource: GenreLocalDatasource
    ) : GenreRepository = GenreRepositoryImpl(genreLocalDatasource)
}