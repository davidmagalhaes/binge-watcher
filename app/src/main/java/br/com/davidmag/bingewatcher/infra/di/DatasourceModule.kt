package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.EpisodeDao
import br.com.davidmag.bingewatcher.data.source.local.dao.FavoredShowDao
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.impl.EpisodeLocalDatasourceImpl
import br.com.davidmag.bingewatcher.data.source.local.impl.FavoredShowLocalDatasourceImpl
import br.com.davidmag.bingewatcher.data.source.local.impl.ShowLocalDatasourceImpl
import br.com.davidmag.bingewatcher.data.source.remote.api.EpisodeApi
import br.com.davidmag.bingewatcher.data.source.remote.api.ShowApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.impl.EpisodeRemoteDatasourceImpl
import br.com.davidmag.bingewatcher.data.source.remote.impl.ShowRemoteDatasourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatasourceModule {
    @Singleton
    @Provides
    fun provideShowLocalDatasource(
        showDao: ShowDao
    ) : ShowLocalDatasource =
        ShowLocalDatasourceImpl(showDao)

    @Singleton
    @Provides
    fun provideFavoredShowLocalDatasource(
        favoredShowDao: FavoredShowDao
    ) : FavoredShowLocalDatasource =
        FavoredShowLocalDatasourceImpl(favoredShowDao)

    @Singleton
    @Provides
    fun provideShowRemoteDatasource(
        showApi: ShowApi
    ) : ShowRemoteDatasource =
        ShowRemoteDatasourceImpl(showApi)

    @Singleton
    @Provides
    fun provideEpisodeLocalDatasource(
        episodeDao: EpisodeDao
    ) : EpisodeLocalDatasource =
        EpisodeLocalDatasourceImpl(episodeDao)

    @Singleton
    @Provides
    fun provideEpisodeRemoteDatasource(
        episodeApi: EpisodeApi
    ) : EpisodeRemoteDatasource =
        EpisodeRemoteDatasourceImpl(episodeApi)
}