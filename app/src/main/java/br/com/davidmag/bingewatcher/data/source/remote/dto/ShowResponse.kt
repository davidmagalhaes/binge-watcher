package br.com.davidmag.bingewatcher.data.source.remote.dto

import org.threeten.bp.LocalDate

data class ShowResponse (
    val id : Long,
    val name : String,
    val image : Posters?,
    val schedule : ShowSchedule?,
    val genres : List<String>,
    val summary : String?,
    val status : String,
    val rating : ShowRating,
    val premiered : LocalDate?
)

data class ShowSchedule(
    val time : String,
    val days : List<String>
)

data class ShowRating (
    val average : Double
)