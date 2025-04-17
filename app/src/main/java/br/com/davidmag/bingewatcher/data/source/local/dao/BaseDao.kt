package br.com.davidmag.bingewatcher.data.source.local.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<T> {
	@Insert(onConflict = OnConflictStrategy.ABORT)
	suspend fun insert(vararg item : T) : List<Long>

	@Insert(onConflict = OnConflictStrategy.ABORT)
	suspend fun insert(data : List<T>) : List<Long>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(vararg item : T) : List<Long>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertSync(vararg data : T) : List<Long>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsert(data : List<T>) : List<Long>

	@Delete
	suspend fun delete(vararg item : T) : Int

	@Delete
	suspend fun deleteSync(vararg item : T): Int

	@Update
	suspend fun update(vararg item : T) : Int
}