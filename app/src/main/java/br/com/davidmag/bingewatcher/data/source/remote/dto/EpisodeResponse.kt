package br.com.davidmag.bingewatcher.data.source.remote.dto

import java.time.LocalDate

data class EpisodeResponse (
    val id : Long,
    val name : String,
    val season : Int,
    val number : Int,
    val summary : String?,
    val image : Posters?,
    val premiered : LocalDate?
)