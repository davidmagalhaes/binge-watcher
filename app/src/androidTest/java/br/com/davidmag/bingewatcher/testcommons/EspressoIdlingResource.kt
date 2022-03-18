package br.com.davidmag.bingewatcher.testcommons

import androidx.test.espresso.idling.CountingIdlingResource
import br.com.davidmag.bingewatcher.domain.common.LogTags
import timber.log.Timber

object EspressoIdlingResource : Timber.Tree(){

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val counting = CountingIdlingResource(RESOURCE)

    fun clearCounting() {
        while(!counting.isIdleNow) counting.decrement()
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when(tag) {
            LogTags.UI_CONTENT_LOADING -> counting.increment()
            LogTags.UI_CONTENT_READY -> if(!counting.isIdleNow) counting.decrement()
        }
    }
}