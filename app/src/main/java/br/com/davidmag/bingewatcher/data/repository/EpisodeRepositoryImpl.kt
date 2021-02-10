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
    override fun get(showId: Long): Flowable<List<Episode>> {
        return episodeLocalDatasource.get(showId)
            .subscribeOn(appSchedulers.network())
    }

    override fun fetch(showId: Long, seasonId : Long): Maybe<Any> {
        return episodeRemoteDatasource.fetch(showId, seasonId)
            .subscribeOn(appSchedulers.network())
            .flatMap {
                episodeLocalDatasource.cache(it)
                    .subscribeOn(appSchedulers.database())
            }
    }
}