package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FavoredShowWithJoins (
    @Embedded
    val show: FavoredShowDb,

    @Relation(
        parentColumn = "_show_id",
        entityColumn = "_genre_id",
        associateBy = Junction(
            value = ShowGenreDb::class,
            parentColumn = "show_id",
            entityColumn = "genre_id"
        )
    )
    val genres: List<GenreDb>
)