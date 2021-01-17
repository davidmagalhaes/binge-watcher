package br.com.davidmag.bingewatcher.data.source.local.dto

import androidx.room.Embedded
import androidx.room.Relation

data class ShowWithEpisodes (
    @Embedded val showDb: ShowDb,
    @Embedded val favoritedShowDb: FavoritedShowDb,
    @Relation(
        parentColumn = "_show_id",
        entityColumn = "episode_show_id"
    )
    val episodes : List<EpisodeDb>
)