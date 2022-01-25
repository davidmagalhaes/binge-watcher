package br.com.davidmag.bingewatcher.infra.di

import android.app.Application
import br.com.davidmag.bingewatcher.domain.usecase.*
import br.com.davidmag.bingewatcher.infra.App
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    PersistenceModule::class,
    NetworkModule::class,
    DatasourceModule::class,
    RepositoryModule::class,
    UseCaseModule::class
])
interface ApplicationComponent {

    @Component.Builder
    interface Builder {

        fun build(): ApplicationComponent

        @BindsInstance
        fun applicationBind(application: Application): Builder
    }

    fun application() : Application

    fun okHttpUrlLoaderFactory() : OkHttpUrlLoader.Factory
    fun getShowUseCase() : GetShowUseCase
    fun searchShowUseCase(): SearchShowUseCase
    fun getShowByIdUseCase(): GetShowByIdUseCase
    fun lookupShowUseCase() : LookupShowUseCase
    fun favoriteShowUseCase() : FavoriteShowUseCase
    fun fetchEpisodesUseCase()  : FetchEpisodesUseCase
    fun getEpisodesUseCase() : GetEpisodesUseCase
    fun getGenresUseCase() : GetGenresUseCase

    fun inject(application : App)
}