package br.com.davidmag.bingewatcher.presentation.model

import android.os.Parcel
import android.os.Parcelable
import br.com.davidmag.bingewatcher.presentation.common.PresentationObject

data class ShowPresentation(
    override val viewType: Int,
    val id : Long,
    val name : String? = null,
    val time : String? = null,
    val days : List<String>? = null,
    val originalImage : String? = null,
    val mediumImage : String? = null,
    val genres : List<String> = emptyList(),
    val summary : String? = null,
    val status : String? = null,
    val ratingAvg : Float = 0.0F,
    val premiered : String? = null,
    val subtitle : String? = null,
    val seasonsTitles : List<String> = emptyList()
) : PresentationObject