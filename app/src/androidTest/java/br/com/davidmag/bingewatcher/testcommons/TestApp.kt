package br.com.davidmag.bingewatcher.testcommons

import br.com.davidmag.bingewatcher.infra.App
import timber.log.Timber

class TestApp : App() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(EspressoIdlingResource)
    }
}