package br.com.davidmag.bingewatcher.domain.model

import java.time.LocalDate

data class Show(
    val id : Long,
    val name : String,
    var imageBackgroundUrl : String? = null,
    val imageMediumUrl : String?,
    val imageOriginalUrl : String?,
    val time : String,
    val days : List<String>,
    var genres : List<Genre> = emptyList(),
    var seasonsIds : List<Long>,
    val summary : String,
    val status : String,
    val ratingAvg : Double,
    val premiered : LocalDate?,
    var favored : Boolean = false,
    var images : List<String> = emptyList()
)
