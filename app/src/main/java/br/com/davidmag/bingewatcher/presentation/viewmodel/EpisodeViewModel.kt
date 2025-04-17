package br.com.davidmag.bingewatcher.presentation.viewmodel

import android.os.Bundle
import androidx.compose.runtime.mutableStateOf
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.common.ErrorPresentation
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation
import timber.log.Timber
import java.lang.Exception

class EpisodeViewModel : BaseViewModel() {
    companion object {
        const val ARG_EPISODE = "episode"
    }

    val episode = mutableStateOf<EpisodePresentation?>(null)
    val fatalError = mutableStateOf<ErrorPresentation?>(null)

    override fun init(args: Bundle?) {
        super.init(args)

        try{
            episode.value = args?.getParcelable(ARG_EPISODE) ?: error("missing argument: $ARG_EPISODE")
        }
        catch(e : Exception){
            Timber.e(e)
            fatalError.value = ErrorPresentation(
                message = R.string.generic_fatal_error,
                args = listOf(e.message)
            )
        }
    }
}