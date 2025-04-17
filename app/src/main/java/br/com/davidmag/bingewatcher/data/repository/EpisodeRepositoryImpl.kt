package br.com.davidmag.bingewatcher.data.repository

import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchers
import br.com.davidmag.bingewatcher.data.source.local.contract.EpisodeLocalDatasource
import br.com.davidmag.bingewatcher.data.source.remote.contract.EpisodeRemoteDatasource
import br.com.davidmag.bingewatcher.domain.model.Episode
import br.com.davidmag.bingewatcher.domain.repository.EpisodeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EpisodeRepositoryImpl(
    private val appDispatchers: AppDispatchers,
    private val episodeRemoteDatasource: EpisodeRemoteDatasource,
    private val episodeLocalDatasource : EpisodeLocalDatasource
) : EpisodeRepository {

    override fun get(showId: Long): Flow<List<Episode>> {
        return episodeLocalDatasource.get(showId)
            .flowOn(appDispatchers.network())
    }

    override suspend fun fetch(showId: Long, seasonId : Long) {
        CoroutineScope(appDispatchers.network()).launch {
            val episodes = episodeRemoteDatasource.fetch(showId, seasonId)

            withContext(appDispatchers.database()) {
                episodeLocalDatasource.cache(episodes)
            }
        }.join()
    }
}