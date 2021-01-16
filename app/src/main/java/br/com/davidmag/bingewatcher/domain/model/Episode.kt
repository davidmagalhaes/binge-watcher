package br.com.davidmag.bingewatcher.domain.model

data class Episode (
    val id : Long,
    val show: Show,
    val name : String,
    val season : Int,
    val number : Int,
    val summary : String,
    val imageMediumUrl : String?,
    val imageOriginalUrl : String?
)