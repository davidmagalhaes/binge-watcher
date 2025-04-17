package br.com.davidmag.bingewatcher.data.source.local.impl

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.data.source.local.contract.ShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.GenreLocalMapper
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowLocalMapper
import br.com.davidmag.bingewatcher.data.source.local.mapper.ShowWithJoinsMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShowLocalDatasourceImpl(
    private val showDao: ShowDao
) : ShowLocalDatasource {
    override fun get(query : String) : DataSource.Factory<Int, Show> {
        return showDao.get(query).mapByPage {
            ShowWithJoinsMapper. toEntity(it)
        }
    }

    override fun get(showId: Long): Flow<List<Show>> {
        return showDao.get(showId).map {
            ShowWithJoinsMapper.toEntity(it)
        }
    }

    override suspend fun append(shows: List<Show>) {
        showDao.append(shows.map {
            Pair(
                ShowLocalMapper.toDto(it),
                GenreLocalMapper.toDto(it.genres)
            )
        })
    }

    override suspend fun cache(shows: List<Show>) {
        showDao.cache(
            shows.map {
                Pair(
                    ShowLocalMapper.toDto(it),
                    GenreLocalMapper.toDto(it.genres)
                )
            }
        )
    }
}