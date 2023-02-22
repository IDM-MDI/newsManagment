package ru.clevertec.newsmanagement.util.impl;

import com.google.protobuf.Timestamp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.model.DTO;

import java.time.Instant;
import java.util.Date;

class NewsMapperTest {
    private NewsMapper newsMapper;

    @BeforeEach
    public void setUp() {
        newsMapper = new NewsMapper();
    }

    @Test
    void toEntityWithValidDTOShouldReturnValidEntity() {
        // Arrange
        News expected = News.builder()
                .id(1L)
                .title("Test Title")
                .text("Test Text")
                .createdDate(Date.from(Instant.ofEpochSecond(1645176000)))
                .build();
        DTO.News newsDTO = DTO.News.newBuilder()
                .setId(1L)
                .setTitle("Test Title")
                .setText("Test Text")
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(1645176000)
                        .setNanos(0)
                        .build())
                .build();

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
        DTO.News expected = DTO.News.newBuilder()
                .setId(1L)
                .setTitle("Test Title")
                .setText("Test Text")
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(1645176000)
                        .setNanos(0)
                        .build())
                .build();
        News news = News.builder()
                .id(1L)
                .title("Test Title")
                .text("Test Text")
                .createdDate(Date.from(Instant.ofEpochSecond(1645176000)))
                .build();

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