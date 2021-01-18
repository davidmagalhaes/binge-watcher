package br.com.davidmag.bingewatcher.domain.model

import org.threeten.bp.LocalDate

data class Show(
    val id : Long,
    val name : String,
    val imageMediumUrl : String?,
    val imageOriginalUrl : String?,
    val time : String,
    val days : List<String>,
    val genres : List<String>,
    val summary : String,
    val status : String,
    val ratingAvg : Double,
    val premiered : LocalDate?,
    var favored : Boolean = false,
    var seasonsCount : Int = 0
)
