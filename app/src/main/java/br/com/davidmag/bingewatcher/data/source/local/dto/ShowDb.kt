package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class ShowDb (
    @PrimaryKey
    @ColumnInfo(name = "_show_id")
    val id : Long,
    val name : String,
    val time : String,
    val days : List<String>,
    val backgroundImage : String?,
    val originalImage : String?,
    val mediumImage : String?,
    val seasonsIds : List<Long>,
    val summary : String,
    val status : String,
    val ratingAvg : Double,
    val premiered : LocalDate?,
    val imageList : List<String> = emptyList()
)