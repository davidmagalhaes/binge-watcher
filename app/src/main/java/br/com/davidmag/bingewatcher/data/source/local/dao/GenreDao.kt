package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.com.davidmag.bingewatcher.data.source.local.dto.GenreDb
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao : BaseDao<GenreDb> {
    @Query("SELECT * FROM GenreDb")
    fun get() : Flow<List<GenreDb>>

    @Transaction
    suspend fun cache(genres : List<GenreDb>): List<Long> {
        deleteAll()
        return insertSync(*genres.toTypedArray())
    }

    @Query("DELETE FROM GenreDb")
    suspend fun deleteAll() : Int
}