package ru.clevertec.newsmanagement.util.impl;

import com.google.protobuf.Timestamp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.model.DTO;

import java.time.Instant;
import java.util.Date;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void toEntityWithValidDTOShouldReturnValidEntity() {
        // Arrange
        Comment expected = Comment.builder()
                .id(1L)
                .text("Test Text")
                .createdDate(Date.from(Instant.ofEpochSecond(1645176000)))
                .build();
        DTO.Comment commentDTO = DTO.Comment.newBuilder()
                .setId(1L)
                .setText("Test Text")
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(1645176000)
                        .setNanos(0)
                        .build())
                .build();

        // Act
        Comment actual = commentMapper.toEntity(commentDTO);

        // Assert
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void toEntityWithNullDTOShouldThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> commentMapper.toEntity(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void toDTOWithValidEntityShouldReturnValidDTO() {
        // Arrange
        DTO.Comment expected = DTO.Comment.newBuilder()
                .setId(1L)
                .setUsername("Test username")
                .setText("Test Text")
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(1645176000)
                        .setNanos(0)
                        .build())
                .build();
        Comment comment = Comment.builder()
                .id(1L)
                .user(User.builder().username("Test username").build())
                .text("Test Text")
                .createdDate(Date.from(Instant.ofEpochSecond(1645176000)))
                .build();

        // Act
        DTO.Comment CommentDTO = commentMapper.toDTO(comment);

        // Assert
        Assertions.assertThat(CommentDTO).isEqualTo(expected);
    }

    @Test
    void toDTOWithNullEntityShouldThrowException() {
        // Assert
        Assertions.assertThatThrownBy(() -> commentMapper.toDTO(null))
                .isInstanceOf(NullPointerException.class);
    }
}