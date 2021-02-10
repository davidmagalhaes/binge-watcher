package br.com.davidmag.bingewatcher.presentation.common

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import io.reactivex.Flowable
import io.reactivex.Maybe
import timber.log.Timber

inline fun <reified T: ViewModel> AppCompatActivity.initViewModel(crossinline factory: () -> T): T =
    _initViewModel(this, intent.extras, factory)

inline fun <reified T: ViewModel> Fragment.initViewModel(crossinline factory: () -> T): T =
    _initViewModel(this, arguments, factory)

fun Context.longToast(message : String?) {
    Toast.makeText(this, message.orEmpty(), Toast.LENGTH_LONG).show()
}

fun Context.getString(messageRes : Int?, vararg args : Any?) : String? {
    return messageRes?.let { getString(it, *args) }
}

fun Context.getString(wrapper : ExceptionWrapper) : String? {
    return getString(
        wrapper.errorMessage,
        *wrapper.errorArgs.toTypedArray()
    ) ?: wrapper.exception.message
}

fun <T : Collection<*>> T.ifNotEmpty(block : (T) -> Unit) : T {
    if(isNotEmpty()) block(this)
    return this
}

inline fun <reified T: ViewModel> _initViewModel(
    owner: ViewModelStoreOwner,
    args: Bundle?,
    crossinline factory: () -> T
): T = T::class.java.let { clazz ->
    ViewModelProvider(owner, object: ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass == clazz) {
                @Suppress("UNCHECKED_CAST")
                val viewModel = factory() as T
                if(viewModel is BaseViewModel){
                    viewModel.init(args)
                }
                return viewModel
            }
            throw IllegalArgumentException("Unexpected argument: $modelClass")
        }
    }).get(clazz)
}

@Suppress("UNCHECKED_CAST")
fun Maybe<*>.submit(
    mediator : MediatorLiveData<PresentationObject>,
    mediatorFailure: MediatorLiveData<ExceptionWrapper>? = null,
    exceptionHandler : (Throwable) -> ExceptionWrapper = { ExceptionWrapper(it) }
) = PresentationUtils.submit(this as Maybe<Any>, mediator, mediatorFailure, exceptionHandler)

@Suppress("UNCHECKED_CAST")
fun Maybe<*>.launchOn(
    mediatorFailure: MediatorLiveData<ExceptionWrapper>,
    exceptionHandler : (Throwable) -> ExceptionWrapper = { ExceptionWrapper(it) }
) = PresentationUtils.launchOn(this as Maybe<Any>, mediatorFailure, exceptionHandler)

fun <Entity , Dto: PresentationObject> Flowable<List<Entity>>.toPresentation(mapper : PresentationMapper<Entity, Dto>) : Flowable<List<Dto>> {
    return this.map(mapper.contentMapper).onErrorReturn { listOf(mapper.errorMapper(it)) }
}

fun <Entity , Dto: PresentationObject> Maybe<List<Entity>>.toPresentation(mapper : PresentationMapper<Entity, Dto>) : Maybe<List<Dto>> {
    return this.map(mapper.contentMapper).onErrorReturn { listOf(mapper.errorMapper(it)) }
}

@Suppress("UNCHECKED_CAST")
fun <T> Maybe<T>.toLiveData(mediator : MediatorLiveData<T>? = null) : LiveData<out T> {
    return PresentationUtils.toLiveData(this, mediator)
}

@Suppress("UNCHECKED_CAST")
fun <T> Flowable<T>.toLiveData(mediator : MediatorLiveData<T>? = null) : LiveData<out T> {
    return PresentationUtils.toLiveData(this, mediator)
}






