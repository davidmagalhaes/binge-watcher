package br.com.davidmag.bingewatcher.data.source.local.impl

import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.EpisodeDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.EpisodeLocalMapper
import br.com.davidmag.bingewatcher.data.source.remote.mapper.EpisodeRemoteMapper
import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe

class EpisodeLocalDatasourceImpl(
    private val episodeDao: EpisodeDao
) : EpisodeLocalDatasource {
    override fun get(showId : Long, season : Int): Flowable<List<Episode>> {
        return episodeDao.get(showId, season).map {
            EpisodeLocalMapper.toEntity(it)
        }
    }

    override fun append(episodes: List<Episode>): Maybe<Any> {
        return episodeDao.upsert(
            EpisodeLocalMapper.toDto(episodes)
        ).map {  }
    }
}