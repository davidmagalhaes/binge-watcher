package br.com.davidmag.bingewatcher.presentation.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import io.reactivex.Flowable
import io.reactivex.Maybe

inline fun <reified T: ViewModel> AppCompatActivity.initViewModel(crossinline factory: () -> T): T =
    _initViewModel(this, intent.extras, factory)

inline fun <reified T: ViewModel> Fragment.initViewModel(crossinline factory: () -> T): T =
    _initViewModel(this, arguments, factory)

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

fun <T> LiveData<T>.observe(owner : LifecycleOwner, func: (T) -> Unit) {
    this.observe({owner.lifecycle}, func)
}

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

@Suppress("UNCHECKED_CAST")
fun Maybe<*>.submit(
    mediator : MediatorLiveData<PresentationObject>,
    mediatorFailure: MediatorLiveData<ExceptionWrapper>? = null
) = PresentationUtils.wrapSubmit(this as Maybe<Any>, mediator, mediatorFailure)

@Suppress("UNCHECKED_CAST")
fun Maybe<*>.launchOn(
    mediatorFailure: MediatorLiveData<ExceptionWrapper>
) = PresentationUtils.launchOn(this as Maybe<Any>, mediatorFailure)

@Suppress("UNCHECKED_CAST")
fun <T> Flowable<T>.wrapIntoLiveData(
    mediator : MediatorLiveData<PresentationObject>,
    mediatorFailure : MediatorLiveData<ExceptionWrapper>? = null
) = PresentationUtils.wrap(this, mediator, mediatorFailure)

@Suppress("UNCHECKED_CAST")
fun <T> Maybe<T>.wrapIntoLiveData(
    mediator : MediatorLiveData<PresentationObject>,
    mediatorFailure : MediatorLiveData<ExceptionWrapper>? = null
) = PresentationUtils.wrap(this.toFlowable(), mediator, mediatorFailure)






