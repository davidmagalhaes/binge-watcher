package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoredShowDb

@Dao
interface FavoredShowDao : BaseDao<FavoredShowDb> {

    @Query("SELECT * FROM FavoredShowDb WHERE name LIKE :query ORDER BY name")
    fun get(query : String) : DataSource.Factory<Int, FavoredShowDb>
}