package br.com.davidmag.bingewatcher.presentation.di

import br.com.davidmag.bingewatcher.AppGlideModule
import br.com.davidmag.bingewatcher.infra.di.ApplicationComponent
import br.com.davidmag.bingewatcher.presentation.mapper.ShowPresentationMapper
import br.com.davidmag.bingewatcher.presentation.view.MainActivity
import dagger.Component
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
    fun inject(showPresentationMapper: ShowPresentationMapper)
    fun inject(mainActivity: MainActivity)
}