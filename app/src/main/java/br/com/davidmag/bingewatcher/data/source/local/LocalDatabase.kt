package br.com.davidmag.bingewatcher.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.davidmag.bingewatcher.data.source.local.dao.EpisodeDao
import br.com.davidmag.bingewatcher.data.source.local.dao.FavoredShowDao
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoredShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.util.RoomConverters

@Database(
    entities = [
        ShowDb::class,
        FavoredShowDb::class,
        EpisodeDb::class
    ],
    version = 6,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getEpisodeDao() : EpisodeDao
    abstract fun getShowDao() : ShowDao
    abstract fun getFavoritedShowDao() : FavoredShowDao
}