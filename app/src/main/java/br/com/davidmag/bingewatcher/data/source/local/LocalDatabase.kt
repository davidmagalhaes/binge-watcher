package br.com.davidmag.bingewatcher.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.davidmag.bingewatcher.data.source.local.dao.EpisodeDao
import br.com.davidmag.bingewatcher.data.source.local.dao.FavoritedShowDao
import br.com.davidmag.bingewatcher.data.source.local.dao.ShowDao
import br.com.davidmag.bingewatcher.data.source.local.dto.EpisodeDb
import br.com.davidmag.bingewatcher.data.source.local.dto.FavoritedShowDb
import br.com.davidmag.bingewatcher.data.source.local.dto.ShowDb
import br.com.davidmag.bingewatcher.data.source.local.util.RoomConverters

@Database(
    entities = [
        ShowDb::class,
        FavoritedShowDb::class,
        EpisodeDb::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(RoomConverters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getEpisodeDao() : EpisodeDao
    abstract fun getShowDao() : ShowDao
    abstract fun getFavoritedShowDao() : FavoritedShowDao
}