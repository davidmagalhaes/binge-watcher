package br.com.davidmag.bingewatcher.infra.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck

@Module
@InstallIn(SingletonComponent::class)
class CommonsModule {
    @Provides
    fun provideResources(app: Application) = app.resources

}