package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb

@Dao
interface EpisodeDao : BaseDao<EpisodeDb> {
	@Transaction
	fun cache(show : ShowDb, vararg items : EpisodeDb){
		deleteAllFromShow(show.id)
		insertSync(*items)
	}

	@Query("DELETE FROM EpisodeDb WHERE episode_show_id = :showId")
	fun deleteAllFromShow(showId : Long) : Int
}