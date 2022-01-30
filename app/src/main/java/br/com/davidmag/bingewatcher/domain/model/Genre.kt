package br.com.davidmag.bingewatcher.domain.model

data class Genre (
    var id : String
) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other !is Genre) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}