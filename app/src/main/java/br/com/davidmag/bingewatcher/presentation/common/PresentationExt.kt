package br.com.davidmag.bingewatcher.presentation.common

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
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

fun Context.getString(errorObj : ErrorPresentation) : String? {
    return getString(
        errorObj.message,
        *errorObj.args.toTypedArray()
    )
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
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
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

//fun <Entity , Dto> Flowable<List<Entity>>.toPresentation(mapper : PresentationMapper<Entity, Dto>) : Flowable<List<Dto>> {
//    return this.map(mapper.contentMapper).onErrorReturn { listOf(mapper.errorMapper(it)) }
//}
//
//fun <Entity , Dto> Maybe<List<Entity>>.toPresentation(mapper : PresentationMapper<Entity, Dto>) : Maybe<List<Dto>> {
//    return this.map(mapper.contentMapper).onErrorReturn { listOf(mapper.errorMapper(it)) }
//}

@OptIn(ExperimentalCoroutinesApi::class)
fun <Entity, Dto> Flow<List<Entity>>.mapToPresentation(mapper : PresentationMapper<Entity, Dto>): Flow<PresentationResult<List<Dto>>> {
    val originalFlow = this

    return flow {
        val resultFlow = this
        emit(PresentationResult.ResultLoading())
        flatMapConcat {
            originalFlow
                .map { resultFlow.emit(PresentationResult.ResultSuccess(mapper.contentMapper(it))) }
                .catch { resultFlow.emit(PresentationResult.ResultError(it, listOf(mapper.errorMapper(it)))) }
                .onEmpty { resultFlow.emit(PresentationResult.ResultEmpty()) }
        }
    }
}

fun <Entity: Any, Dto: Any> Flow<PagingData<Entity>>.toPresentationPage(
    mapper : PresentationMapper<Entity, Dto>
): Flow<PagingData<Dto>> {
    return map { flow ->
        flow.map { mapper.parse(it).first() }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.mapToResult(): Flow<PresentationResult<T>> {
    val originalFlow = this
    return flow {
        val resultFlow = this
        emit(PresentationResult.ResultLoading())
        flatMapConcat {
            originalFlow
                .map { resultFlow.emit(PresentationResult.ResultSuccess(it)) }
                .catch { resultFlow.emit(PresentationResult.ResultError(it)) }
                .onEmpty { resultFlow.emit(PresentationResult.ResultEmpty()) }
        }
    }
}

fun <T> Flow<T>.launchAndCollect(scope: CoroutineScope, block: ((T) -> Unit)? = null) {
    scope.launch {
        collect { block?.invoke(it) }
    }
}

fun <T> Flow<T>.launchAndCollect(
    scope: CoroutineScope,
    success: MutableState<T>,
    error: MutableState<Throwable>? = null
) {
    scope.launch {
        val flow = error?.let { catch {
            Timber.e(it)
            error.value = it
        } } ?: this@launchAndCollect
        flow.collect { success.value = it }
    }
}

fun <T> Flow<PresentationResult<T>>.launchAndCollect(
    scope: CoroutineScope,
    state: MutableState<PresentationResult<T>>
) {
    scope.launch {
        collect { state.value = it }
    }
}

fun <T> Flow<PresentationResult<T>>.launchAndCollect(
    scope: CoroutineScope,
    success: MutableState<T>? = null,
    error: MutableState<Throwable>? = null,
    empty: MutableState<Any>? = null,
    loading: MutableState<Any>? = null
) {
    scope.launch {
        collect {
            when(it) {
                is PresentationResult.ResultSuccess -> success?.value = it.data
                is PresentationResult.ResultError -> {
                    Timber.e(it.error)
                    error?.value = it.error
                }
                is PresentationResult.ResultEmpty -> empty?.value = Any()
                is PresentationResult.ResultLoading -> loading?.value = Any()
            }
        }
    }
}

