package br.com.davidmag.bingewatcher.data.common

interface EntityDtoMapper<Entity, DtoInput, DtoOutput> {
    val toDtoMapper : (Entity) -> DtoOutput
    val toEntityMapper : (DtoInput) -> Entity

    fun toDto(entity : Entity) : DtoOutput {
        return toDtoMapper(entity)
    }

    fun toDto(entities : List<Entity>) : List<DtoOutput> {
        return entities.map { toDtoMapper(it) }
    }

    fun toDto(vararg entities : Entity) : List<DtoOutput> {
        return entities.map { toDtoMapper(it) }
    }

    fun toEntity(dto : DtoInput) : Entity {
        return toEntityMapper(dto)
    }

    fun toEntity(dtos : List<DtoInput>) : List<Entity> {
        return dtos.map { toEntityMapper(it) }
    }

    fun toEntity(vararg dtos : DtoInput) : List<Entity> {
        return dtos.map { toEntityMapper(it) }
    }
}