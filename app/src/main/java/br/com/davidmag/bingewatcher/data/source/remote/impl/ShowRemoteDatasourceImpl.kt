package br.com.davidmag.bingewatcher.data.source.remote.impl

import br.com.davidmag.bingewatcher.data.source.remote.api.ShowApi
import br.com.davidmag.bingewatcher.data.source.remote.contract.ShowRemoteDatasource
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
        return showApi.lookup(showId).map {
            listOf(ShowRemoteMapper.toEntity(it))
        }
    }

    override fun search(query: String): Maybe<List<Show>> {
        return showApi.search(query).map {
            ShowRemoteMapper.toEntity(it)
        }
    }

    override fun seasons(showId: Long): Maybe<List<Any>> {
        return showApi.seasons(showId)
    }
}