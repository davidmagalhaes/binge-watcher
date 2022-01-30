package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ShowWithJoins (
    @Embedded
    val show: ShowDb,
    @ColumnInfo(name = "favored")
    val favored : Boolean? = null,
    @ColumnInfo(name = "seasonCount")
    val seasonCount : Int? = null,

    @Relation(
        parentColumn = "_show_id",
        entityColumn = "_genre_id",
        associateBy = Junction(
            value = ShowGenreDb::class,
            parentColumn = "show_id",
            entityColumn = "genre_id"
        )
    )
    val genresDb: List<GenreDb>
)