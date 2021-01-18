package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.data.repository.EpisodeRepositoryImpl
import br.com.davidmag.bingewatcher.data.repository.ShowRepositoryImpl
import br.com.davidmag.bingewatcher.data.scheduler.AppSchedulers
import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
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
        showRemoteDatasource: ShowRemoteDatasource
    ) : ShowRepository = ShowRepositoryImpl(
        appSchedulers,
        showLocalDatasource,
        favoredShowLocalDatasource,
        showRemoteDatasource
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
}