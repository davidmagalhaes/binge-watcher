package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.Embedded
import androidx.room.Entity

@Entity(
    primaryKeys = ["_show_id"]
)
class FavoredShowDb(
    @Embedded
    val show : ShowDb
)