package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.Embedded
import androidx.room.Entity

@Entity
data class FavoritedShowDb (
    @Embedded
    val show: ShowDb
)