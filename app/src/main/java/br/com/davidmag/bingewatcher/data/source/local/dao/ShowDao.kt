package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.GenreDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowGenreDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowWithJoins
import io.reactivex.Flowable

@Dao
interface ShowDao : BaseDao<ShowDb> {

	@Query("SELECT * FROM ShowDb WHERE name like :query ORDER BY _show_id")
	fun get(query : String) : DataSource.Factory<Int, ShowWithJoins>

	@Transaction
	@Query("SELECT ShowDb.*, (CASE WHEN FavoredShowDb._show_id IS NULL THEN 0 ELSE 1 END) as favored, (select count(*) from (select distinct season FROM episodedb WHERE episode_show_id = :showId)) as seasonCount FROM ShowDb LEFT JOIN FavoredShowDb ON (ShowDb._show_id = FavoredShowDb._show_id) WHERE ShowDb._show_id = :showId")
	fun get(showId : Long) : Flowable<List<ShowWithJoins>>


	@Transaction
	fun cache(shows : List<Pair<ShowDb, List<GenreDb>>>) {
		_deleteAllRelationsSync()
		deleteAll()
		insertSync(*shows.map { it.first }.toTypedArray())

		val relations = shows.map { pair ->
			pair.second.map {
				ShowGenreDb(
					showId = pair.first.id,
					genreId = it.id
				)
			}
		}

		_relationInsertSync(*relations.flatten().toTypedArray())
	}

	@Transaction
	fun append(shows : List<Pair<ShowDb, List<GenreDb>>>){
		val showList = shows.map { it.first }.toTypedArray()

		_deleteAllRelationsByIdSync(showList.map { it.id })
		deleteSync(*showList)
		insertSync(*showList)

		val relations = shows.map { pair ->
			pair.second.map {
				ShowGenreDb(
					showId = pair.first.id,
					genreId = it.id
				)
			}
		}

		_relationInsertSync(*relations.flatten().toTypedArray())
	}

	@Query("DELETE FROM ShowDb WHERE _show_id")
	fun deleteAll() : Int

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun _relationInsertSync(vararg jtable : ShowGenreDb)


	@Query("DELETE FROM ShowGenreDb")
	fun _deleteAllRelationsSync()

	@Query("DELETE FROM ShowGenreDb WHERE show_id in (:show_id)")
	fun _deleteAllRelationsByIdSync(show_id: List<Long>)
}