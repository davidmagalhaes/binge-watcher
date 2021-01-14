package br.com.davidmag.bingewatcher.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.util.RoomConverters

@Database(
    entities = [
        ShowDb::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class LocalDatabase : RoomDatabase() {

}