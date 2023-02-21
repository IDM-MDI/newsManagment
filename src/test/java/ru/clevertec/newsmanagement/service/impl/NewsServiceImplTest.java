package ru.clevertec.newsmanagement.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.entity.Role;
import ru.clevertec.newsmanagement.entity.User;
import ru.clevertec.newsmanagement.exception.CustomException;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.persistence.NewsRepository;
import ru.clevertec.newsmanagement.service.CommentService;
import ru.clevertec.newsmanagement.service.UserService;
import ru.clevertec.newsmanagement.util.impl.NewsMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {
    @Mock
    private NewsRepository newsRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentService commentService;

    @Mock
    private NewsMapper mapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private NewsMapper newsMapper;
    private List<News> entities;
    private List<DTO.News> dtos;

    private static final long NEWS_ID = 1L;
    private static final String USERNAME = "user1";
    private static final String TITLE = "Test Title";
    private static final String TEXT = "Test Text";
    private static final int PAGE = 0;
    private static final int SIZE = 2;
    private static final String FILTER = "id";
    private static final String DIRECTION = "asc";


    @BeforeEach
    public void before() {
        ReflectionTestUtils.setField(newsService,"commentService",commentService);
        newsMapper = new NewsMapper();
        entities = Arrays.asList(
                News.builder()
                        .id(1L)
                        .title(TITLE)
                        .text(TEXT)
                        .createdDate(Date.from(Instant.ofEpochSecond(1000000)))
                        .user(User.builder().username(USERNAME).role(Role.JOURNALIST).build())
                        .build(),
                News.builder()
                        .id(2L)
                        .title(TITLE)
                        .text(TEXT)
                        .createdDate(Date.from(Instant.ofEpochSecond(1000000)))
                        .user(User.builder().username(USERNAME).build())
                        .build()
        );
        dtos = entities.stream()
                .map(newsMapper::toDTO)
                .toList();
    }
    @Test
    void findNewsShouldBeCorrect() throws CustomException {
        // given
        when(newsRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(entities));
        IntStream.range(0, entities.size())
                .forEach(integer -> when(mapper.toDTO(entities.get(integer)))
                        .thenReturn(dtos.get(integer)));

        // when
        List<DTO.News> result = newsService.findNews(PAGE, SIZE, FILTER, DIRECTION);

        // then
        Assertions.assertThat(result).isEqualTo(dtos);
    }

    @Test
    void findNewsByIdShouldBeCorrect() throws CustomException {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));

        // when
        DTO.News result = newsService.findNews(NEWS_ID);

        // then
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
    }

    @Test
    void findNewsByIdNotFound() {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.empty());

        // when
        Assertions.assertThatThrownBy(() -> newsService.findNews(NEWS_ID))
                .isInstanceOf(CustomException.class);
    }
    @Test
    void findNewsEntityByIdFound() throws CustomException {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));

        // when
        News result = newsService.findNewsEntity(NEWS_ID);

        //then
        Assertions.assertThat(result).isEqualTo(entities.get(0));
    }
    @Test
    void findNewsEntityByIdNotFound() {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.empty());

        // when
        Assertions.assertThatThrownBy(() -> newsService.findNewsEntity(NEWS_ID))
                .isInstanceOf(CustomException.class);
    }
    @Test
    void saveNewsShouldCreateNewsEntity() throws CustomException {
        // given
        when(userService.findUser(USERNAME)).thenReturn(entities.get(0).getUser());
        when(mapper.toEntity(dtos.get(0))).thenReturn(entities.get(0));
        when(newsRepository.save(any(News.class))).thenReturn(entities.get(0));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));

        // when
        DTO.News savedNews = newsService.saveNews(USERNAME, dtos.get(0));

        // then
        Assertions.assertThat(savedNews).isEqualTo(dtos.get(0));
        verify(userService, times(1)).findUser(USERNAME);
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void findNewsWithMatchingTitleAndText() {
        // given
        when(mapper.toEntity(dtos.get(0))).thenReturn(entities.get(0));
        when(newsRepository.findAll(any(Example.class))).thenReturn(entities);
        IntStream.range(0, entities.size())
                .forEach(integer -> when(mapper.toDTO(entities.get(integer)))
                        .thenReturn(dtos.get(integer)));

        // when
        List<DTO.News> result = newsService.findNews(dtos.get(0));

        // then
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void findNewsWithNoMatchingNews() {
        // given
        when(mapper.toEntity(dtos.get(0))).thenReturn(entities.get(0));
        when(newsRepository.findAll(any(Example.class))).thenReturn(new ArrayList());
        // when
        List<DTO.News> result = newsService.findNews(dtos.get(0));

        // then
        Assertions.assertThat(result).isEmpty();
        verify(newsRepository).findAll(any(Example.class));
        verify(mapper, never()).toDTO(any(News.class));
    }

    @Test
    void saveNewsShouldThrowExceptionIfUserNotFound() throws CustomException {
        // given
        when(userService.findUser(USERNAME)).thenThrow(CustomException.class);

        // then
        Assertions.assertThatThrownBy(() -> newsService.saveNews(USERNAME, dtos.get(0)))
                .isInstanceOf(CustomException.class);
        verify(userService, times(1)).findUser(USERNAME);
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    void updateNewsShouldUpdateNewsEntity() throws CustomException {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(USERNAME)).thenReturn(entities.get(0).getUser());
        when(newsRepository.save(any(News.class))).thenReturn(entities.get(0));
        when(mapper.toDTO(entities.get(0))).thenReturn(dtos.get(0));

        // when
        DTO.News savedNews = newsService.updateNews(NEWS_ID, USERNAME, dtos.get(0));

        // then
        Assertions.assertThat(savedNews).isEqualTo(dtos.get(0));
        verify(newsRepository, times(2)).findById(NEWS_ID);
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void updateNewsShouldThrowExceptionIfUserNotFound() throws CustomException {
        // given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(USERNAME)).thenThrow(CustomException.class);

        // then
        Assertions.assertThatThrownBy(() -> newsService.updateNews(NEWS_ID ,USERNAME, dtos.get(0)))
                .isInstanceOf(CustomException.class);
        verify(userService, times(1)).findUser(USERNAME);
        verifyNoMoreInteractions(newsRepository);
    }

    @Test
    void deleteNewsWhenUserIsAuthorizedShouldDeleteNewsAndComments() throws CustomException {
        //given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(USERNAME)).thenReturn(entities.get(0).getUser());

        // when
        newsService.deleteNews(NEWS_ID, USERNAME);

        // Assert
        verify(commentService, times(1)).deleteAllComment(NEWS_ID);
        verify(newsRepository, times(1)).deleteById(NEWS_ID);
    }

    @Test
    void deleteNewsShouldThrowCustomExceptionWhenUserIsNotAuthorized() throws CustomException {
        User user = User.builder()
                .username(USERNAME + 1)
                .role(Role.SUBSCRIBER)
                .build();
        //given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.of(entities.get(0)));
        when(userService.findUser(USERNAME + 1)).thenReturn(user);

        // when
        Assertions.assertThatThrownBy(() -> newsService.deleteNews(NEWS_ID, USERNAME + 1))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void deleteNewsWhenNewsDoesNotExistShouldThrowCustomException() {
        //given
        when(newsRepository.findById(NEWS_ID)).thenReturn(Optional.empty());

        // then
        Assertions.assertThatThrownBy(() -> newsService.deleteNews(NEWS_ID, USERNAME))
                .isInstanceOf(CustomException.class);
    }
}