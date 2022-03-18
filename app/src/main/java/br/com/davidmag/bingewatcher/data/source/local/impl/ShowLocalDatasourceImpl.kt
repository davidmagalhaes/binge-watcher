package br.com.davidmag.bingewatcher.data.source.local.impl

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.GenreLocalMapper
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowLocalMapper
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowWithJoinsMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Flowable
import io.reactivex.Maybe

class ShowLocalDatasourceImpl(
    private val showDao: ShowDao
) : ShowLocalDatasource {
    override fun get(query : String) : DataSource.Factory<Int, Show> {
        return showDao.get(query).mapByPage {
            ShowWithJoinsMapper.toEntity(it)
        }
    }

    override fun get(showId: Long): Flowable<List<Show>> {
        return showDao.get(showId).map {
            ShowWithJoinsMapper.toEntity(it)
        }
    }

    override fun append(shows: List<Show>): Maybe<Any> {
        return Maybe.fromCallable {
            showDao.append(shows.map {
                Pair(
                    ShowLocalMapper.toDto(it),
                    GenreLocalMapper.toDto(it.genres)
                )
            })
        }
    }

    override fun cache(shows: List<Show>): Maybe<Any> {
        return Maybe.fromCallable {
            showDao.cache(shows.map {
                Pair(
                    ShowLocalMapper.toDto(it),
                    GenreLocalMapper.toDto(it.genres)
                )
            })
        }
    }
}