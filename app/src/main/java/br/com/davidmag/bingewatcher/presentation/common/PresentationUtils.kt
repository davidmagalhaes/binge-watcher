package br.com.davidmag.bingewatcher.presentation.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

object PresentationUtils {

    @Suppress("NULLABLE_TYPE_PARAMETER_AGAINST_NOT_NULL_TYPE_PARAMETER")
    fun <T> wrap(
        flowable: Flowable<T>,
        mediator: MediatorLiveData<PresentationObject>,
        mediatorFailure: MutableLiveData<ExceptionPresentation>? = null,
        removeAfterFirst: Boolean = false,
        exceptionHandler : (Throwable) -> ExceptionPresentation = { ExceptionPresentation(it) }
    ) {
        val source = LiveDataReactiveStreams.fromPublisher(
            flowable.observeOn(AndroidSchedulers.mainThread())
                .map {
                    PresentationWrapper(
                        if(it == null || (it is List<*> && it.isNotEmpty()))
                            PresentationObject.VIEWTYPE_CONTENT else PresentationObject.VIEWTYPE_EMPTY,
                        it
                    ) as PresentationObject
                }
                .onErrorReturn {
                    Timber.e(it)
                    exceptionHandler(it)
                }
                .map {
                    if(it is ExceptionPresentation && mediatorFailure != null){
                        mediatorFailure.postValue(it)
                    } else {
                        mediator.postValue(it)
                    }
                }
        )

        mediator.addSource(source){
            if(removeAfterFirst) mediator.removeSource(source)
        }
    }

    fun submit(
        maybe: Maybe<Any>,
        mediator : MediatorLiveData<PresentationObject>,
        mediatorFailure: MediatorLiveData<ExceptionPresentation>?,
        exceptionHandler : (Throwable) -> ExceptionPresentation = { ExceptionPresentation(it) }
    ) {
        wrap(
            maybe.toFlowable(),
            mediator,
            mediatorFailure,
            true,
            exceptionHandler
        )
    }

    fun launchOn(
        maybe: Maybe<Any>,
        mediatorFailure: MediatorLiveData<ExceptionPresentation>,
        exceptionHandler : (Throwable) -> ExceptionPresentation = { ExceptionPresentation(it) }
    ) {
        val mediat = MediatorLiveData<PresentationObject>()

        mediatorFailure.addSource(mediat) {
            mediatorFailure.removeSource(mediat)
        }

        wrap(
            maybe.toFlowable(),
            mediat,
            mediatorFailure,
            true,
            exceptionHandler
        )
    }

    fun <T> toLiveData(
        maybe : Maybe<T>,
        mediator : MediatorLiveData<T>? = null
    ) : LiveData<T> {
        return toLiveData(maybe.toFlowable(), mediator, true)
    }

    fun <T> toLiveData(
        flowable : Flowable<T>,
        mediator : MediatorLiveData<T>? = null,
        removeAfterFirst: Boolean = false
    ) : LiveData<T> {
        val source = LiveDataReactiveStreams.fromPublisher(
            flowable.observeOn(AndroidSchedulers.mainThread())
        )

        mediator?.addSource(source) {
            mediator.postValue(it)
            if(removeAfterFirst){
                mediator.removeSource(source)
            }
        }

        return source
    }
}