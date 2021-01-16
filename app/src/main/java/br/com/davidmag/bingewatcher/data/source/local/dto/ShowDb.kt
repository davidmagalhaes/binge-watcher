package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ShowDb (
    @PrimaryKey
    @ColumnInfo(name = "_show_id")
    val id : Long,
    val name : String,
    val time : String,
    val days : List<String>,
    val originalImage : String?,
    val mediumImage : String?,
    val genres : List<String>,
    val summary : String
)