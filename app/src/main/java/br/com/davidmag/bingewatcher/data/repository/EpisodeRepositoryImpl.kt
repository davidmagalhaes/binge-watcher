package br.com.davidmag.bingewatcher.data.repository

import br.com.davidmag.bingewatcher.data.scheduler.AppSchedulers
import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import io.reactivex.Flowable
import io.reactivex.Maybe

class EpisodeRepositoryImpl(
    private val appSchedulers: AppSchedulers,
    private val episodeRemoteDatasource: EpisodeRemoteDatasource,
    private val episodeLocalDatasource : EpisodeLocalDatasource
) : EpisodeRepository {
    override fun get(showId: Long, season : Int): Flowable<List<Episode>> {
        return episodeLocalDatasource.get(showId, season)
            .subscribeOn(appSchedulers.network())
    }

    override fun fetch(showId: Long): Maybe<Any> {
        return episodeRemoteDatasource.fetch(showId)
            .subscribeOn(appSchedulers.network())
            .flatMap {
                episodeLocalDatasource.append(it)
                    .subscribeOn(appSchedulers.database())
            }
    }
}