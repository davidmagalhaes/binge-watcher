package br.com.davidmag.bingewatcher.data.source.remote.dto

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate

data class SearchResponse (
	val score : Double,
	val show : ShowResponse
)

data class ShowResponse (
    val id : Long,
    val name : String,
    val image : Posters?,
    val schedule : ShowSchedule?,
    val genres : List<String>,
    val summary : String?,
    val status : String,
    val rating : ShowRating?,
    val premiered : LocalDate?
)

data class ShowSchedule(
    val time : String,
    val days : List<String>
)

data class ShowRating (
    val average : Double
)

data class ShowImageDto (
	val id : Long,
	val type: ImageTypeDto,
	val main : Boolean,
	val resolutions : ImageResolutions
)

data class ImageResolutions (
	val original : ImageUrl,
	val medium : ImageUrl?
)

data class ImageUrl (
	val url : String
)

data class SeasonResponse (
	val id : Long,
	val number : Int
)

enum class ImageTypeDto {
	@SerializedName("poster")
	POSTER,
	@SerializedName("background")
	BACKGROUND
}