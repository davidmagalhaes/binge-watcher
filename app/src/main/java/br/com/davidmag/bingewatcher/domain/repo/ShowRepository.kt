package br.com.davidmag.bingewatcher.domain.repo

import io.reactivex.Maybe

interface ShowRepository {
    fun favorite(showId : Long, favorite : Boolean) : Maybe<Any>
}