package br.com.davidmag.bingewatcher.presentation.di

import br.com.davidmag.bingewatcher.AppGlideModule
import br.com.davidmag.bingewatcher.infra.di.ApplicationComponent
import br.com.davidmag.bingewatcher.presentation.common.PresentationMapper
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.view.EpisodeActivity
import br.com.davidmag.bingewatcher.presentation.view.HomeActivity
import br.com.davidmag.bingewatcher.presentation.view.ShowActivity
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

@PresentationScope
@Component(
    dependencies = [
        ApplicationComponent::class
    ],
    modules = [
        PresentationMapperModule::class,
        ViewModelModule::class
    ]
)
interface PresentationComponent {
    fun inject(appGlideModule: AppGlideModule)
    fun inject(homeActivity: HomeActivity)
    fun inject(showActivity: ShowActivity)
    fun inject(episodeActivity: EpisodeActivity)

    fun inject(showPresentationMapper: ShowPresentationMapper)
}