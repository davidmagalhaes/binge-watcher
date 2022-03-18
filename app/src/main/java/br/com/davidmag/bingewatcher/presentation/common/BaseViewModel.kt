package br.com.davidmag.bingewatcher.presentation.common

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import br.com.davidmag.bingewatcher.domain.common.LogParams
import br.com.davidmag.bingewatcher.domain.common.LogTags
import br.com.davidmag.bingewatcher.domain.common.i
import br.com.davidmag.bingewatcher.domain.util.BooleanSwitches
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    companion object {
        private const val STATUS_CONTENT_LOAD = 1
        private const val STATUS_CONTENT_START = 2
    }

    private val contentLoaded: BooleanSwitches = BooleanSwitches (::onLoadSwitchTriggered)
    private val contentStarted: BooleanSwitches = BooleanSwitches (::onStartSwitchTriggered)

    val errors = MediatorLiveData<ExceptionPresentation>()
    val contentReady = MediatorLiveData<Unit>()

    open fun init(args: Bundle?){

    }

    fun contentStarting(contentName: String, vararg contentNames: String) {
        changeLoadStatus(STATUS_CONTENT_START, contentName, false)

        contentNames.forEach {
            changeLoadStatus(STATUS_CONTENT_START, it, false)
        }
    }

    fun contentStarted(contentName: String) {
        changeLoadStatus(STATUS_CONTENT_START, contentName, true)
    }

    fun isContentReady(contentName: String) =
        contentStarted.get(contentName) && contentLoaded.get(contentName)

    fun contentLoaded(contentName : String) {
        changeLoadStatus(STATUS_CONTENT_LOAD, contentName, true)
    }

    fun contentLoading(contentName: String, vararg contentNames: String){
        changeLoadStatus(STATUS_CONTENT_LOAD, contentName, false)

        contentNames.forEach {
            changeLoadStatus(STATUS_CONTENT_LOAD, it, false)
        }
    }

    fun <T : PresentationObject> loadStatusChanged(
        liveData: MutableLiveData<PagingData<T>>,
        contentName: String,
        emptyPlaceholder: T,
        loadingPlaceHolder: T,
        loadStatus: CombinedLoadStates,
        itemCount: Int) {

        when {
            loadStatus.append is LoadState.Loading -> {
                when(contentName) {
                    HomeViewModel.CONTENT_SHOW ->
                        if(liveData.value != null) {
                            liveData.postValue(liveData.value?.insertFooterItem(
                                TerminalSeparatorType.SOURCE_COMPLETE,
                                loadingPlaceHolder)
                            )
                        }
                }

                contentLoading(contentName)
            }
            loadStatus.refresh is LoadState.Loading -> {
                when(contentName) {
                    HomeViewModel.CONTENT_SHOW ->
                        if(liveData.value != null) {
                            liveData.postValue(
                                liveData.value?.insertHeaderItem(
                                    TerminalSeparatorType.SOURCE_COMPLETE,
                                    loadingPlaceHolder
                                )
                            )
                        }
                }

                contentLoading(contentName)
            }
            loadStatus.prepend is LoadState.Loading -> contentLoading(contentName)
            else -> {
                if(itemCount < 1) {
                    when (contentName) {
                        HomeViewModel.CONTENT_SHOW ->
                            liveData.postValue(PagingData.from(listOf(emptyPlaceholder)))
                    }
                }

                contentLoaded(contentName)
            }
        }
    }

    open fun onContentReady(contentName: String): Boolean {
        return true
    }

    open fun onContentLoading(contentName: String) {

    }

    private fun changeLoadStatus(contentStatus: Int, contentName: String, value: Boolean) {
        when(contentStatus) {
            STATUS_CONTENT_LOAD -> contentLoaded.switch(contentName, value)
            STATUS_CONTENT_START -> contentStarted.switch(contentName, value)
        }
    }

    private fun onStartSwitchTriggered(
        contentName: String,
        @Suppress("UNUSED_PARAMETER") any : Any
    ) {
        if(isContentReady(contentName) && onContentReady(contentName)) {
            contentReady.postValue(Unit)
            Timber.i(LogParams(contentName, System.currentTimeMillis()), LogTags.UI_CONTENT_READY)
        }
    }

    private fun onLoadSwitchTriggered(contentName: String, value : Boolean) {
        val isContentReady = isContentReady(contentName)

        when {
            isContentReady -> if(onContentReady(contentName)) { contentReady.postValue(Unit) }
            contentLoaded.get(contentName).not() -> onContentLoading(contentName)
        }

        if(isContentReady || !value) {
            Timber.i(
                LogParams(contentName, System.currentTimeMillis()),
                if(isContentReady) LogTags.UI_CONTENT_READY else LogTags.UI_CONTENT_LOADING
            )
        }
    }
}