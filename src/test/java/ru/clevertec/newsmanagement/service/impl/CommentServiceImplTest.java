package ru.clevertec.newsmanagement.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.newsmanagement.entity.Comment;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.model.PageFilter;
import ru.clevertec.newsmanagement.persistence.CommentRepository;
import ru.clevertec.newsmanagement.service.NewsService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.util.impl.CommentMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.newsmanagement.exception.ExceptionStatus.NO_ACCESS;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    private static final PageFilter PAGE = new PageFilter();
    private static long NEWS_ID = 1L;
    private static long COMMENT_ID = 1L;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserService userService;
    @Mock
    private NewsService newsService;
    @Mock
    private CommentMapper mapper;
    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentMapper commentMapper;
    private List<Comment> entities;
    private List<DTO.Comment> dtos;
    @BeforeEach
    public void before() {
        ReflectionTestUtils.setField(commentService,"newsService",newsService);
        commentMapper = new CommentMapper();
        entities = Arrays.asList(
                Comment.builder()
                        .id(1L)
                        .text("Test comment 1")
                        .createdDate(Date.from(Instant.ofEpochSecond(1000000)))
                        .user(User.builder().username("test user 1").build())
                        .news(News.builder().id(1L).build())
                        .build(),
                Comment.builder()
                        .id(2L)
                        .text("Test comment 2")
                        .createdDate(Date.from(Instant.ofEpochSecond(1000000)))
                        .user(User.builder().username("test user 2").build())
                        .news(News.builder().id(2L).build())
                        .build()
        );
        dtos = entities.stream()
                .map(commentMapper::toDTO)
                .toList();
    }
    @Test
    void findCommentsShouldBeCorrect() throws CustomException {
        when(commentRepository.findCommentsByNews_Id(eq(NEWS_ID), any(PageRequest.class)))
                .thenReturn(new ArrayList<>(entities));

        IntStream.range(0, entities.size() - 1)
                        .forEach(integer -> when(mapper.toDTO(entities.get(integer)))
                                .thenReturn(dtos.get(integer)));

        // Act
        List<DTO.Comment> result = commentService.findComments(NEWS_ID,PAGE);

        // Assert
        Assertions.assertThat(result).hasSize(2);
        verify(commentRepository).findCommentsByNews_Id(eq(NEWS_ID), any(PageRequest.class));
    }

    @Test
    void findCommentShouldBeCorrect() throws CustomException {
        // Arrange
        when(commentRepository.findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID))
                .thenReturn(Optional.of(entities.get(0)));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));

        // Act
        DTO.Comment result = commentService.findComments(NEWS_ID, COMMENT_ID);

        // Assert

        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID);
    }

    @Test
    void findCommentsWithFilter() throws CustomException {
        // Arrange
        when(mapper.toEntity(dtos.get(0)))
                .thenReturn(entities.get(0));
        when(newsService.findNewsEntity(NEWS_ID))
                .thenReturn(entities.get(0).getNews());
        when(commentRepository.findAll(any(Example.class)))
                .thenReturn(entities);
        IntStream.range(0, entities.size() - 1)
                .forEach(integer -> when(mapper.toDTO(entities.get(integer)))
                        .thenReturn(dtos.get(integer)));

        // Act
        dtos = commentService.findComments(NEWS_ID, dtos.get(0));

        // Assert
        Assertions.assertThat(dtos).hasSize(2);

        verify(commentRepository).findAll(any(Example.class));
    }

    @Test
    void saveCommentShouldBeCorrect() throws CustomException {
        // Arrange
        when(newsService.findNewsEntity(NEWS_ID)).thenReturn(entities.get(0).getNews());
        when(userService.findUser(entities.get(0).getUser().getUsername())).thenReturn(entities.get(0).getUser());
        when(mapper.toEntity(dtos.get(0))).thenReturn(entities.get(0));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));
        when(commentRepository.save(entities.get(0))).thenReturn(entities.get(0));

        // Act
        DTO.Comment result = commentService.saveComment(NEWS_ID, dtos.get(0).getUsername(), dtos.get(0));

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).save(entities.get(0));
    }

    @Test
    void updateCommentShouldBeCorrect() throws CustomException {
        // Arrange
        when(userService.findUser(dtos.get(0).getUsername()))
                .thenReturn(entities.get(0).getUser());
        when(commentRepository.findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID))
                .thenReturn(Optional.of(entities.get(0)));
        when(commentRepository.save(entities.get(0)))
                .thenReturn(entities.get(0));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));

        // Act
        DTO.Comment result = commentService.updateComment(NEWS_ID, COMMENT_ID, dtos.get(0).getUsername(), dtos.get(0));

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
        verify(commentRepository).save(entities.get(0));
    }

    @Test
    void deleteCommentShouldBeCorrect() throws CustomException {
        // Arrange
        when(commentRepository.findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID))
                .thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(dtos.get(0).getUsername()))
                .thenReturn(entities.get(0).getUser());
        doNothing().when(commentRepository).deleteById(COMMENT_ID);
        // Act
        commentService.deleteComment(COMMENT_ID, NEWS_ID, dtos.get(0).getUsername());

        // Assert
        verify(commentRepository).deleteById(COMMENT_ID);
    }
    @Test
    void deleteCommentShouldThrowNoAccess() throws CustomException {
        // Arrange
        User user = User.builder()
                .username("test")
                .role(Role.SUBSCRIBER)
                .build();
        when(commentRepository.findCommentByIdAndNews_Id(COMMENT_ID, NEWS_ID))
                .thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(dtos.get(0).getUsername()))
                .thenReturn(user);

        Assertions.assertThatThrownBy(() -> commentService.deleteComment(COMMENT_ID, NEWS_ID, dtos.get(0).getUsername()))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(NO_ACCESS.toString());
    }
    @Test
    void deleteAllCommentShouldBeCorrect() {
        //given
        List<Long> longs = entities.stream()
                .map(Comment::getId)
                .toList();
        when(commentRepository.findByNews_Id(NEWS_ID))
                .thenReturn(longs);
        longs.forEach(l -> doNothing().when(commentRepository).deleteById(l));

        //when
        commentService.deleteAllComment(NEWS_ID);

        // Assert
        verify(commentRepository).findByNews_Id(NEWS_ID);
        verify(commentRepository, times(2)).deleteById(anyLong());
    }
}