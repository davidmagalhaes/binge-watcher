package br.com.davidmag.bingewatcher.presentation.model

import android.text.Spanned
import br.com.davidmag.bingewatcher.presentation.common.PresentationObject
import java.util.Collections.emptyList

data class ShowPresentation(
    override val viewType: Int,
    val id : Long,
    val name : String? = null,
    val time : String? = null,
    val days : List<String>? = null,
    val originalImage : String? = null,
    val mediumImage : String? = null,
    val backgroundImage : String? = null,
    val genres : List<String> = emptyList(),
    val summary : Spanned? = null,
    val status : String? = null,
    val ratingAvg : Float = 0.0F,
    val premiered : String? = null,
    val subtitle : String? = null,
    val favored : Boolean = false,
    val seasonsTitles : List<String> = emptyList(),
    val seasonsIds : List<Long> = emptyList()
) : PresentationObject {
    companion object {
        fun loading() = ShowPresentation(
            viewType = PresentationObject.VIEWTYPE_LOADING,
            id = PresentationObject.VIEWTYPE_LOADING.toLong()
        )

        fun empty() = ShowPresentation(
            viewType = PresentationObject.VIEWTYPE_EMPTY,
            id = PresentationObject.VIEWTYPE_EMPTY.toLong()
        )
    }
}