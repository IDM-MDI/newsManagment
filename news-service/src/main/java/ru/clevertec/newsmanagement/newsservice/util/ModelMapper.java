package ru.clevertec.newsmanagement.newsservice.util;

/**
 * Interface for a generic model mapper.
 * @param <ENTITY> the type of the entity to map to/from
 * @param <DTO> the type of the DTO to map to/from
 * @author Dayanch
 */
public interface ModelMapper<ENTITY,DTO> {

    /**
     * Maps a DTO object to an entity object.
     * @param dto the DTO object to map
     * @return the entity object
     */
    ENTITY toEntity(DTO dto);

    /**
     * Maps an entity object to a DTO object.
     * @param entity the entity object to map
     * @return the DTO object
     */
    DTO toDTO(ENTITY entity);
}
