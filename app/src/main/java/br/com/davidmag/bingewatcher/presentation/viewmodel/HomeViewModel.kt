package br.com.davidmag.bingewatcher.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import br.com.davidmag.bingewatcher.presentation.common.BaseViewModel
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation

class HomeViewModel() : BaseViewModel(){

    companion object {
        const val FAVORITE_STATE_DISABLED = 0
        const val FAVORITE_STATE_ENABLED = 1
    }

    var query = ""

    val shows = MediatorLiveData<PagedList<ShowPresentation>>()
    val favoriteState = MutableLiveData<Int>()

    fun fetchShows(){
        val isFavoriteEnabled = favoriteState.value == FAVORITE_STATE_ENABLED


    }

    fun searchShows(query: String) {
        this.query = query
    }

    fun favoriteClick(){
        if(favoriteState.value == FAVORITE_STATE_ENABLED){
            favoriteState.postValue(FAVORITE_STATE_DISABLED)
        }
        else{
            favoriteState.postValue(FAVORITE_STATE_ENABLED)
        }
    }

}