package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation

data class ShowWithJoins (
    @Embedded
    val show: ShowDb,
    @ColumnInfo(name = "favored")
    val favored : Boolean,
    @ColumnInfo(name = "seasonCount")
    val seasonCount : Int
)