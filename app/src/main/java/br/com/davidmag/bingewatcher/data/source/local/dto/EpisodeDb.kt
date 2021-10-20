package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.davidmag.bingewatcher.data.source.remote.dto.Posters
import org.threeten.bp.LocalDate

@Entity
data class EpisodeDb (
    @PrimaryKey
    @ColumnInfo(name = "_episode_id")
    val id : Long,
    @ColumnInfo(name = "episode_show_id")
    val showId : Long,
    val name : String,
    val season : Int,
    val number : Int,
    val summary : String,
    val premiered : LocalDate?,
    val imageOriginalUrl : String? = null,
    val imageMediumUrl : String? = null
)