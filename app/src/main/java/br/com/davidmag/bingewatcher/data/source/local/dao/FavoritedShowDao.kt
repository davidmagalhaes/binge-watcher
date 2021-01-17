package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoritedShowDb

@Dao
interface FavoritedShowDao : BaseDao<FavoritedShowDb> {

    @Query("SELECT * FROM FavoritedShowDb ORDER BY name")
    fun get() : DataSource.Factory<Int, FavoritedShowDb>
}