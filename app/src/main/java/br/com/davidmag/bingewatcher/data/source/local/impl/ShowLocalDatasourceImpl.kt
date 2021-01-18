package br.com.davidmag.bingewatcher.data.source.local.impl

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowLocalMapper
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowWithJoinsMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

class ShowLocalDatasourceImpl(
    private val showDao: ShowDao
) : ShowLocalDatasource {
    override fun get(query : String) : DataSource.Factory<Int, Show> {
       return showDao.get(query).mapByPage {
           ShowLocalMapper.toEntity(it)
       }
    }

    override fun lookup(showId: Long): Maybe<List<Show>> {
        return showDao.get(showId).map {
            ShowWithJoinsMapper.toEntity(it)
        }
    }

    override fun append(shows: List<Show>): Maybe<Any> {
        return showDao.upsert(
            ShowLocalMapper.toDto(shows)
        ).map { Any() }
    }

    override fun cache(shows: List<Show>): Maybe<Any> {
        return Maybe.fromCallable {
            showDao.cache(
                *ShowLocalMapper.toDto(shows).toTypedArray()
            )
        }
    }
}