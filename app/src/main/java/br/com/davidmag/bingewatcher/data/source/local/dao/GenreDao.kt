package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.data.source.local.dto.GenreDb
import io.reactivex.Flowable

@Dao
interface GenreDao : BaseDao<GenreDb> {
    @Query("SELECT * FROM GenreDb")
    fun get() : Flowable<List<GenreDb>>

    @Transaction
    fun cache(genres : List<GenreDb>) : List<Long> {
        deleteAll()
        return insertSync(*genres.toTypedArray())
    }

    @Query("DELETE FROM GenreDb")
    fun deleteAll() : Int
}