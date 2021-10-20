package br.com.davidmag.bingewatcher.presentation.common

abstract class PresentationMapper<Entity, DtoPresentation> {
    abstract val contentMapper : (List<Entity>) -> List<DtoPresentation>
    abstract val errorMapper : (Throwable) -> DtoPresentation

    fun parse(item : Entity) : List<DtoPresentation> {
        return contentMapper(listOf(item))
    }

    fun parse(items: List<Entity>) : List<DtoPresentation> {
        return contentMapper(items)
    }
}