package ru.clevertec.newsmanagement.newsservice.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.newsservice.entity.Comment;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.persistence.CommentRepository;
import ru.clevertec.newsmanagement.newsservice.service.NewsService;
import ru.clevertec.newsmanagement.newsservice.util.impl.CommentMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.newsmanagement.exceptionservice.exception.ExceptionStatus.NO_ACCESS;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.CommentBuilder.aComment;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    private static final long NEWS_ID = 1L;
    private static final long COMMENT_ID = 1L;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private NewsService newsService;
    @Mock
    private CommentMapper mapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    private List<Comment> entities;
    private List<DTO.Comment> dtos;
    @BeforeEach
    public void before() {
        commentService.setNewsService(newsService);
        CommentMapper commentMapper = new CommentMapper();
        entities = Arrays.asList(
                aComment().buildToEntity(),
                aComment().buildToEntity(),
                aComment().buildToEntity()
        );
        dtos = entities.stream()
                .map(commentMapper::toDTO)
                .toList();
    }
    @Test
    void findCommentsShouldBeCorrect() throws CustomException {
        doReturn(new ArrayList<>(entities))
                .when(commentRepository).findCommentsByNews_Id(eq(NEWS_ID), any(PageRequest.class));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        List<DTO.Comment> result = commentService.findComments(NEWS_ID,PageRequest.of(1,1));

        // Assert
        Assertions.assertThat(result).hasSize(3);
        verify(commentRepository).findCommentsByNews_Id(eq(NEWS_ID), any(PageRequest.class));
    }

    @Test
    void findCommentShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(Optional.of(entities.get(0)))
                .when(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID)
                ;
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        DTO.Comment result = commentService.findComments(NEWS_ID, COMMENT_ID);

        // Assert

        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID);
    }

    @Test
    void findCommentsWithFilter() throws CustomException {
        // Arrange
        doReturn(entities.get(0))
                .when(mapper).toEntity(dtos.get(0));
        doReturn(entities.get(0).getNews())
                .when(newsService).findNewsEntity(NEWS_ID);
        doReturn(entities)
                .when(commentRepository).findAll(any(Example.class));

        IntStream.range(0, entities.size() - 1)
                .forEach(integer -> doReturn(dtos.get(integer))
                        .when(mapper).toDTO(entities.get(integer)));

        // Act
        dtos = commentService.findComments(NEWS_ID, dtos.get(0));

        // Assert
        Assertions.assertThat(dtos).hasSize(3);

        verify(commentRepository).findAll(any(Example.class));
    }

    @Test
    void saveCommentShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(entities.get(0).getNews())
                .when(newsService).findNewsEntity(NEWS_ID);
        doReturn(entities.get(0))
                .when(mapper).toEntity(dtos.get(0));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));
        doReturn(entities.get(0))
                .when(commentRepository).save(entities.get(0));

        // Act
        DTO.Comment result = commentService.saveComment(NEWS_ID, dtos.get(0), aUser().buildToDTO());

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).save(entities.get(0));
    }

    @Test
    void updateCommentShouldBeCorrect() throws CustomException {
        doReturn(Optional.of(entities.get(0)))
                .when(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID);
        doReturn(entities.get(0))
                .when(commentRepository).save(entities.get(0));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        DTO.Comment result = commentService.updateComment(NEWS_ID, COMMENT_ID, dtos.get(0), aUser().buildToDTO());

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).save(entities.get(0));
    }

    @Test
    void deleteCommentShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(Optional.of(entities.get(0)))
                .when(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID);
        doNothing()
                .when(commentRepository).deleteById(COMMENT_ID);
        // Act
        commentService.deleteComment(COMMENT_ID, NEWS_ID, aUser().buildToDTO());

        // Assert
        verify(commentRepository).deleteById(COMMENT_ID);
    }
    @Test
    void deleteCommentShouldThrowNoAccess() throws CustomException {
        doReturn(Optional.of(entities.get(0)))
                .when(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID);

        Assertions.assertThatThrownBy(() -> commentService.deleteComment(COMMENT_ID, NEWS_ID, aUser().setUsername("TEST").buildToDTO()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(NO_ACCESS.toString());
    }
    @Test
    void deleteAllCommentShouldBeCorrect() {
        //given
        List<Long> longs = entities.stream()
                .map(Comment::getId)
                .toList();
        doReturn(longs)
                .when(commentRepository).findByNews_Id(NEWS_ID);
        doNothing()
                .when(commentRepository).deleteById(1L);

        //when
        commentService.deleteAllComment(NEWS_ID);

        // Assert
        verify(commentRepository).findByNews_Id(NEWS_ID);
        verify(commentRepository, times(3)).deleteById(anyLong());
    }
}