package br.com.davidmag.bingewatcher.data.source.local.impl

import androidx.paging.DataSource
import br.com.davidmag.bingewatcher.data.source.local.contract.FavoredShowLocalDatasource
import br.com.davidmag.bingewatcher.data.source.local.dao.FavoredShowDao
import br.com.davidmag.bingewatcher.data.source.local.mapper.FavoritedShowLocalMapper
import br.com.davidmag.bingewatcher.domain.model.Show

class FavoredShowLocalDatasourceImpl(
    private val favoredShowDao: FavoredShowDao
) : FavoredShowLocalDatasource {
    override fun get(query : String): DataSource.Factory<Int, Show> {
        return favoredShowDao.get(query)
            .mapByPage {
                FavoritedShowLocalMapper.toEntity(it)
            }
    }

    override suspend fun upsert(show: Show) {
        favoredShowDao.upsert(FavoritedShowLocalMapper.toDto(show))
    }

    override suspend fun delete(show: Show) {
        favoredShowDao.delete(FavoritedShowLocalMapper.toDto(show))
    }
}