package br.com.davidmag.bingewatcher.infra.di

import android.app.Application
import androidx.room.Room
import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchers
import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchersImpl
import br.com.davidmag.bingewatcher.data.source.local.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Singleton
    @Provides
    fun provideDatabase(
        application: Application
    ) : LocalDatabase {
        return Room.databaseBuilder(
            application,
            LocalDatabase::class.java,
            "bingewatcher.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAppSchedulers() =
        AppDispatchersImpl as AppDispatchers

    @Provides
    fun provideShowDao(
        database: LocalDatabase
    ) = database.getShowDao()

    @Provides
    fun provideFavoritedShowDao(
        database: LocalDatabase
    ) = database.getFavoritedShowDao()

    @Provides
    fun provideEpisodeDao(
        database: LocalDatabase
    ) = database.getEpisodeDao()

    @Provides
    fun provideGenreDao(
        database: LocalDatabase
    ) = database.getGenreDao()
}