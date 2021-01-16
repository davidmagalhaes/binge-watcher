package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowWithEpisodes
import io.reactivex.Maybe

@Dao
interface ShowDao : BaseDao<ShowDb> {
	@Query("SELECT * FROM ShowDb ORDER BY name")
	fun get() : DataSource.Factory<Int, ShowDb>

	@Query("SELECT * FROM ShowDb LEFT JOIN EpisodeDb ON (ShowDb._show_id = EpisodeDb.episode_show_id) WHERE ShowDb._show_id = :showId")
	fun get(showId : Long) : Maybe<ShowWithEpisodes>

	@Transaction
	fun cache(vararg shows : ShowDb) {
		deleteAll()
		insertSync(*shows)
	}

	@Query("DELETE FROM ShowDb")
	fun deleteAll() : Int
}