package br.com.davidmag.bingewatcher.data.source.local.impl

import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.EpisodeDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.EpisodeLocalMapper
import br.com.davidmag.bingewatcher.domain.model.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EpisodeLocalDatasourceImpl(
    private val episodeDao: EpisodeDao
) : EpisodeLocalDatasource {
    override fun get(showId : Long): Flow<List<Episode>> {
        return episodeDao.get(showId).map {
            EpisodeLocalMapper.toEntity(it)
        }
    }

    override suspend fun append(episodes: List<Episode>) {
        episodeDao.upsert(EpisodeLocalMapper.toDto(episodes))
    }

    override suspend fun cache(episodes: List<Episode>) {
        episodeDao.cache(
            *EpisodeLocalMapper.toDto(episodes).toTypedArray()
        )
    }
}