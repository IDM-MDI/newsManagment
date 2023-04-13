package ru.clevertec.newsmanagement.newsservice.util.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;

class NewsMapperTest {
    private NewsMapper newsMapper;

    @BeforeEach
    public void setUp() {
        newsMapper = new NewsMapper();
    }

    @Test
    void toEntityWithValidDTOShouldReturnValidEntity() {
        // Arrange
        News expected = aNews().buildToEntity();
        DTO.News newsDTO = aNews().buildToDTO();

        // Act
        News actual = newsMapper.toEntity(newsDTO);

        // Assert
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toEntityWithNullDTOShouldThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> newsMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void toDTOWithValidEntityShouldReturnValidDTO() {
        // Arrange
        DTO.News expected = aNews().buildToDTO();
        News news = aNews().buildToEntity();

        // Act
        DTO.News newsDTO = newsMapper.toDTO(news);

        // Assert
        Assertions.assertThat(newsDTO).isEqualTo(expected);
    }

    @Test
    void toDTOWithNullEntityShouldThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> newsMapper.toDTO(null))
                .isInstanceOf(NullPointerException.class);
    }
}