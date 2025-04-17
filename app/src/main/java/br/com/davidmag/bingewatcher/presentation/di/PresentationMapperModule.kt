package br.com.davidmag.bingewatcher.presentation.di

import android.app.Application
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class PresentationMapperModule {
    @Provides
    fun provideShowPresentationMapper(
        application: Application
    ) = ShowPresentationMapper(application.resources)

    @Provides
    fun provideEpisodePresentationMapper(
        application: Application
    ) = EpisodePresentationMapper(application.resources)
}