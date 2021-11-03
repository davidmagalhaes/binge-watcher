package br.com.davidmag.bingewatcher.data.source.local.contract

import br.com.davidmag.bingewatcher.domain.model.Genre
import io.reactivex.Flowable
import io.reactivex.Maybe

interface GenreLocalDatasource {
    fun get(): Flowable<List<Genre>>
    fun append(genres: List<Genre>): Maybe<Any>
    fun cache(genres: List<Genre>): Maybe<Any>
}