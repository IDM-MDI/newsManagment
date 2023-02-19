package ru.clevertec.newsmanagement.util;

public interface ModelMapper<ENTITY,DTO> {
    ENTITY toEntity(DTO dto);
    DTO toDTO(ENTITY entity);
}
