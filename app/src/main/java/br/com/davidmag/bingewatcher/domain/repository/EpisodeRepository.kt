package br.com.davidmag.bingewatcher.domain.repository

import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe

interface EpisodeRepository {
    fun get(showId : Long) : Flowable<List<Episode>>
    fun fetch(showId : Long, seasonId : Long) : Maybe<Any>
}