package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GenreDb (
    @PrimaryKey
    @ColumnInfo(name = "_genre_id")
    val id: String
)