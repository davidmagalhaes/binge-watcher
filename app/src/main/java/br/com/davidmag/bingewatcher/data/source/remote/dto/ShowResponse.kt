package br.com.davidmag.bingewatcher.data.source.remote.dto

data class ShowResponse (
    val id : Long,
    val name : String,
    val images : Posters,
    val schedule : ShowSchedule,
    val genres : List<String>,
    val summary : String,
    val externals : OtherIdentifiers
)

data class ShowSchedule(
    val time : String,
    val days : List<String>
)

data class OtherIdentifiers(
    val imdb : String
)