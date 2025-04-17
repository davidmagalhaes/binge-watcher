package br.com.davidmag.bingewatcher.data.source.remote.impl

import br.com.davidmag.bingewatcher.data.scheduler.AppDispatchers
import br.com.davidmag.bingewatcher.data.source.remote.api.ShowApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.dto.ImageTypeDto
import br.com.davidmag.bingewatcher.data.source.remote.mapper.ShowRemoteMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class ShowRemoteDatasourceImpl(
    private val appDispatchers: AppDispatchers,
    private val showApi: ShowApi
) : ShowRemoteDatasource {
    override suspend fun fetch(page: Int): List<Show> {
        return showApi.fetch(page).map { ShowRemoteMapper.toEntity(it) }
    }

    override suspend fun lookup(showId : Long): Show {
        val scope = CoroutineScope(appDispatchers.network())

        val showDeferred = scope.async { showApi.lookup(showId) }
        val seasons = scope.async { showApi.seasons(showId) }
        val images = scope.async { showApi.fetchImages(showId) }

        awaitAll(showDeferred, seasons, images)

        val show = showDeferred.await()

        show.seasonsIds = seasons.await().sortedBy { it.number }.map { it.id }

        show.images = images.await().map { it.resolutions.medium?.url ?: it.resolutions.original.url }
        show.imageBackgroundUrl = images.await()
            .filter { it.type == ImageTypeDto.BACKGROUND }
            .sortedBy { it.resolutions.medium?.url }
            .map { it.resolutions.medium?.url ?: it.resolutions.original.url }
            .firstOrNull()?.replace("http://", "https://")

        return ShowRemoteMapper.toEntity(show)
    }

    override suspend fun search(query: String, page : Int): List<Show> {
        return ShowRemoteMapper.toEntity(
            showApi.search(query, page).map { it.show }
        )
    }
}