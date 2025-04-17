package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao : BaseDao<EpisodeDb> {

    @Query("SELECT * FROM EpisodeDb WHERE episode_show_id = :showId order by number")
    fun get(showId : Long) : Flow<List<EpisodeDb>>

    @Transaction
    suspend fun cache(vararg episodes : EpisodeDb) {
        deleteAll()
        insertSync(*episodes)
    }

    @Query("DELETE FROM EpisodeDb")
    suspend fun deleteAll() : Int
}