package br.com.davidmag.bingewatcher.data.source.remote.contract

import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Maybe

interface EpisodeRemoteDatasource {
    fun fetch(showId : Long, seasonId : Long) : Maybe<List<Episode>>
}