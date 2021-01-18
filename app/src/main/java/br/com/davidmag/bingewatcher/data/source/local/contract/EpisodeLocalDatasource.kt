package br.com.davidmag.bingewatcher.data.source.local.contract

import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe

interface EpisodeLocalDatasource {
    fun get(showId : Long, season : Int) : Flowable<List<Episode>>
    fun append(episodes : List<Episode>) : Maybe<Any>
}