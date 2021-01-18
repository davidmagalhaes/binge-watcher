package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.common.ExceptionWrapper
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import timber.log.Timber
import java.lang.Exception

class EpisodeViewModel : BaseViewModel() {
    companion object {
        const val ARG_EPISODE = "episode"
    }

    val episode = MutableLiveData<EpisodePresentation>()
    val fatalError = MutableLiveData<ExceptionWrapper>()

    override fun init(args: Bundle?) {
        super.init(args)

        try{
            episode.postValue(args?.getParcelable(ARG_EPISODE)
                ?: error("missing argument: $ARG_EPISODE"))
        }
        catch(e : Exception){
            Timber.e(e)
            fatalError.postValue(ExceptionWrapper(
                exception = e,
                errorMessage = R.string.generic_fatal_error
            ))
        }
    }
}