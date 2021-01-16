package br.com.davidmag.bingewatcher.presentation.common

interface PresentationMapper<Entity, DtoPresentation: PresentationObject> {
    val contentMapper : (List<Entity>) -> List<DtoPresentation>
    val errorMapper : (Throwable) -> DtoPresentation
}