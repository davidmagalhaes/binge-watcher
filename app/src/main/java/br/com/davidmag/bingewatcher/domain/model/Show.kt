package br.com.davidmag.bingewatcher.domain.model

data class Show(
    val id : Long,
    val name : String,
    val imageMediumUrl : String?,
    val imageOriginalUrl : String?,
    val time : String,
    val days : List<String>,
    val genres : List<String>,
    val summary : String,
    val imdb : String
)
