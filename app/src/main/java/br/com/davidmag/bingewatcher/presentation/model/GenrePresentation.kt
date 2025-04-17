package br.com.davidmag.bingewatcher.presentation.model

data class GenrePresentation (
    val id: String,
    var selected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other !is GenrePresentation) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}