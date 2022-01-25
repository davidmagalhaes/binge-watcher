package br.com.davidmag.bingewatcher.presentation.model

import br.com.davidmag.bingewatcher.presentation.common.PresentationObject

data class GenrePresentation (
    val id: String,
    var selected: Boolean = false,
    override val viewType: Int = PresentationObject.VIEWTYPE_CONTENT
): PresentationObject {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other !is GenrePresentation) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}