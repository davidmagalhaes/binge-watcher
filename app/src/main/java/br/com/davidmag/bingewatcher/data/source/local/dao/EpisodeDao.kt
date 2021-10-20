package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.remote.dto.EpisodeResponse
import br.com.davidmag.bingewatcher.domain.model.Episode
import io.reactivex.Flowable

@Dao
interface EpisodeDao : BaseDao<EpisodeDb> {

    @Query("SELECT * FROM EpisodeDb WHERE episode_show_id = :showId order by number")
    fun get(showId : Long) : Flowable<List<EpisodeDb>>

    @Transaction
    fun cache(vararg episodes : EpisodeDb) {
        deleteAll()
        insertSync(*episodes)
    }

    @Query("DELETE FROM EpisodeDb")
    fun deleteAll() : Int
}