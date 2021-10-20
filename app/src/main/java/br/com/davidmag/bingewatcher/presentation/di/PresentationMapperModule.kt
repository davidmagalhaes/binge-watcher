package br.com.davidmag.bingewatcher.presentation.di

import android.app.Application
import br.com.davidmag.bingewatcher.presentation.mapper.EpisodePresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import dagger.Module
import dagger.Provides

@Module
class PresentationMapperModule {
    @PresentationScope
    @Provides
    fun provideShowPresentationMapper(
        application: Application
    ) = ShowPresentationMapper(application.resources)

    @PresentationScope
    @Provides
    fun provideEpisodePresentationMapper(
        application: Application
    ) = EpisodePresentationMapper(application.resources)
}