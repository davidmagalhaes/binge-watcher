package br.com.davidmag.bingewatcher.presentation.common

interface PresentationObject {
    companion object {
        const val VIEWTYPE_CONTENT = 0
        const val VIEWTYPE_EMPTY = -97
        const val VIEWTYPE_LOADING = -98
        const val VIEWTYPE_ERROR = -99
    }

    val viewType: Int
}