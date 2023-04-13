package ru.clevertec.newsmanagement.newsservice.util.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

import static ru.clevertec.newsmanagement.newsservice.builder.impl.CommentBuilder.aComment;

class CommentMapperTest {

    private CommentMapper commentMapper;

    @BeforeEach
    public void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    void toEntityWithValidDTOShouldReturnValidEntity() {
        // Arrange
        Comment expected = aComment().buildToEntity();
        DTO.Comment commentDTO = aComment().buildToDTO();

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
        DTO.Comment expected = aComment().buildToDTO();
        Comment comment = aComment().buildToEntity();

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