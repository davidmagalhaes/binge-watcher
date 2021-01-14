package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class ShowDb {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_show_id")
    var id : Long? = null
}