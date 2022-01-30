package br.com.davidmag.bingewatcher.data.source.remote.impl

import br.com.davidmag.bingewatcher.data.source.remote.api.ShowApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
import br.com.davidmag.bingewatcher.data.source.remote.dto.ImageTypeDto
import br.com.davidmag.bingewatcher.data.source.remote.mapper.ShowRemoteMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

class ShowRemoteDatasourceImpl(
    private val showApi: ShowApi
) : ShowRemoteDatasource {
    override fun fetch(page: Int): Maybe<List<Show>> {
        return showApi.fetch(page).map {
            ShowRemoteMapper.toEntity(it)
        }
    }

    override fun lookup(showId : Long): Maybe<List<Show>> {
        return showApi.lookup(showId)
            .flatMap { showResp ->
                val show = ShowRemoteMapper.toEntity(showResp)
                showApi.seasons(show.id)
                    .map { seasonsResp ->
                        show.seasonsIds = seasonsResp.sortedBy { it.number }.map { it.id }
                        listOf(show)
                    }
            }
            .flatMap { showList ->
                showApi.fetchImages(showId).map { showImage ->
                    showList.onEach { show ->
                        show.images = showImage.map {
                            it.resolutions.medium?.url ?: it.resolutions.original.url
                        }
                        show.imageBackgroundUrl =
                            showImage.filter { it.type == ImageTypeDto.BACKGROUND }
                                .sortedBy { it.resolutions.medium?.url }
                                .map { it.resolutions.medium?.url ?: it.resolutions.original.url }
                                .firstOrNull()?.replace("http://", "https://")
                    }
                }
            }
    }

    override fun search(query: String, page : Int): Maybe<List<Show>> {
        return showApi.search(query, page).map { searchResponses ->
            ShowRemoteMapper.toEntity(searchResponses.map { it.show })
        }
    }
}