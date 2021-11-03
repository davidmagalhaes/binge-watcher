package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["genre_id", "show_id"]
)
data class ShowGenreDb(
    @ColumnInfo(name = "genre_id")
    val genreId : String,
    @ColumnInfo(name = "show_id")
    val showId : Long
)