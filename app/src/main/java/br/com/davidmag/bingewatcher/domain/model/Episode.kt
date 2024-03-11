package br.com.davidmag.bingewatcher.domain.model

import java.time.LocalDate

data class Episode (
    val id : Long,
    var showId : Long = 0,
    val name : String,
    val season : Int,
    val number : Int,
    val summary : String,
    val premiered : LocalDate?,
    val imageMediumUrl : String?,
    val imageOriginalUrl : String?
)