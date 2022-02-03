package br.com.davidmag.bingewatcher.presentation.common

import android.os.Bundle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import br.com.davidmag.bingewatcher.domain.common.LogParams
import br.com.davidmag.bingewatcher.domain.common.LogTags
import br.com.davidmag.bingewatcher.domain.common.i
import br.com.davidmag.bingewatcher.domain.util.NamedBooleanSwitches
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {
    val errors = MediatorLiveData<ExceptionPresentation>()

    private val contentLoading = NamedBooleanSwitches {contentName, loading ->
        Timber.i(
            LogParams(contentName, System.currentTimeMillis()),
            if(loading) LogTags.UI_CONTENT_LOADING else LogTags.UI_CONTENT_LOADED
        )
        onContentStatusChange(contentName, loading)
    }

    open fun init(args: Bundle?){}

    fun contentLoaded(contentName : String) {
        contentLoading.switch(contentName, false)
    }

    fun contentLoading(contentName: String, vararg contentNames: String){
        contentLoading.switch(contentName, true)

        contentNames.forEach {
            contentLoading.switch(it, true)
        }
    }

    open fun onContentStatusChange(contentName: String, loading: Boolean) {}
}