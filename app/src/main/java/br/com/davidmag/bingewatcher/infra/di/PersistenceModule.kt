package br.com.davidmag.bingewatcher.infra.di

import android.app.Application
import androidx.room.Room
import br.com.davidmag.bingewatcher.data.source.local.LocalDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
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
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}