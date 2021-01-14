package br.com.davidmag.bingewatcher.infra.di

import android.app.Application
import br.com.davidmag.bingewatcher.infra.App
import br.com.davidmag.bingewatcher.infra.di.UseCaseModule
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

    fun inject(application : App)
}