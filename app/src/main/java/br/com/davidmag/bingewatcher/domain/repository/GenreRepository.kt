package br.com.davidmag.bingewatcher.domain.repository

import br.com.davidmag.bingewatcher.domain.model.Genre
import io.reactivex.Flowable

interface GenreRepository {
    fun get() : Flowable<List<Genre>>
}