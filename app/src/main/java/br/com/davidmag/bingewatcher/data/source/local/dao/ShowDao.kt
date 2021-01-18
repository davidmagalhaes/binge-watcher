package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowWithJoins
import io.reactivex.Maybe

@Dao
interface ShowDao : BaseDao<ShowDb> {

	@Query("SELECT * FROM ShowDb WHERE name like :query ORDER BY _show_id")
	fun get(query : String) : DataSource.Factory<Int, ShowDb>

	@Transaction
	@Query("SELECT ShowDb.*, (CASE WHEN FavoredShowDb._show_id IS NULL THEN 0 ELSE 1 END) as favored, (select count(*) from (select distinct season FROM episodedb WHERE episode_show_id = :showId)) as seasonCount FROM ShowDb LEFT JOIN FavoredShowDb ON (ShowDb._show_id = FavoredShowDb._show_id) WHERE ShowDb._show_id = :showId")
	fun get(showId : Long) : Maybe<List<ShowWithJoins>>

	@Transaction
	fun cache(vararg shows : ShowDb) {
		deleteAll()
		insertSync(*shows)
	}

	@Query("DELETE FROM ShowDb WHERE _show_id")
	fun deleteAll() : Int
}