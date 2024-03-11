package br.com.davidmag.bingewatcher.infra

import android.app.Application
import br.com.davidmag.bingewatcher.app.BuildConfig
import br.com.davidmag.bingewatcher.infra.di.ApplicationComponent
import br.com.davidmag.bingewatcher.infra.di.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class App  : Application() {

    companion object  {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

            Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build()
            )
        }

        RxJavaPlugins.setErrorHandler {
            Timber.e(it, "RxJava error handled on Global Handler!")
        }

        applicationComponent = DaggerApplicationComponent.builder()
            .applicationBind(this)
            .build()

        super.onCreate()
    }
}