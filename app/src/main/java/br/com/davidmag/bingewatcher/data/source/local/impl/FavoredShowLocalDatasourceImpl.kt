package br.com.davidmag.bingewatcher.data.source.local.impl

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.FavoredShowDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.FavoritedShowLocalMapper
import br.com.davidmag.bingewatcher.domain.model.Show
import io.reactivex.Maybe

class FavoredShowLocalDatasourceImpl(
    private val favoredShowDao: FavoredShowDao
) : FavoredShowLocalDatasource {
    override fun get(query : String): DataSource.Factory<Int, Show> {
        return favoredShowDao.get(query).mapByPage {
            FavoritedShowLocalMapper.toEntity(it)
        }
    }

    override fun upsert(show: Show): Maybe<Any> {
        return favoredShowDao.upsert(
            FavoritedShowLocalMapper.toDto(show)
        ).map { Any() }
    }

    override fun delete(show: Show): Maybe<Any> {
        return favoredShowDao.delete(
            FavoritedShowLocalMapper.toDto(show)
        ).map { Any() }
    }
}