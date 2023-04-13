package ru.clevertec.newsmanagement.newsservice.service.impl;

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
import ru.clevertec.newsmanagement.exceptionservice.exception.CustomException;
import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;
import ru.clevertec.newsmanagement.newsservice.persistence.NewsRepository;
import ru.clevertec.newsmanagement.newsservice.service.CommentService;
import ru.clevertec.newsmanagement.newsservice.util.impl.NewsMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {
    private static final long NEWS_ID = 1L;
    @Mock
    private NewsRepository newsRepository;
    @Mock
    private CommentService commentService;

    @Mock
    private NewsMapper mapper;

    @InjectMocks
    private NewsServiceImpl newsService;

    private List<News> entities;
    private List<DTO.News> dtos;


    @BeforeEach
    public void before() {
        newsService.setCommentService(commentService);
        NewsMapper newsMapper = new NewsMapper();
        entities = Arrays.asList(
                aNews().buildToEntity(),
                aNews().buildToEntity()
        );
        dtos = entities.stream()
                .map(newsMapper::toDTO)
                .toList();
    }
    @Test
    void findNewsShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(new PageImpl<>(entities))
                .when(newsRepository).findAll(any(PageRequest.class));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        List<DTO.News> result = newsService.findNews(PageRequest.of(1,1));

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos);
    }

    @Test
    void findNewsByIdShouldBeCorrect() throws CustomException {
        // Arrange
        doReturn(Optional.of(entities.get(0)))
                .when(newsRepository).findById(NEWS_ID);
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        DTO.News result = newsService.findNews(NEWS_ID);

        // Assert
        Assertions.assertThat(result).isEqualTo(dtos.get(0));
    }

    @Test
    void findNewsByIdNotFound() {
        // Arrange
        doReturn(Optional.empty())
                .when(newsRepository).findById(NEWS_ID);

        // Act
        Assertions.assertThatThrownBy(() -> newsService.findNews(NEWS_ID))
                .isInstanceOf(CustomException.class);
    }
    @Test
    void findNewsEntityByIdFound() throws CustomException {
        // Arrange
        doReturn(Optional.of(entities.get(0)))
                .when(newsRepository).findById(NEWS_ID);

        // Act
        News result = newsService.findNewsEntity(NEWS_ID);

        //then
        Assertions.assertThat(result).isEqualTo(entities.get(0));
    }
    @Test
    void findNewsEntityByIdNotFound() {
        // Arrange
        doReturn(Optional.empty())
                .when(newsRepository).findById(NEWS_ID);

        // Act
        Assertions.assertThatThrownBy(() -> newsService.findNewsEntity(NEWS_ID))
                .isInstanceOf(CustomException.class);
    }
    @Test
    void saveNewsShouldCreateNewsEntity() throws CustomException {
        // Arrange
        doReturn(entities.get(0))
                .when(mapper).toEntity(dtos.get(0));
        doReturn(entities.get(0))
                .when(newsRepository).save(any(News.class));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        DTO.News savedNews = newsService.saveNews(dtos.get(0),aUser().buildToDTO());

        // Assert
        Assertions.assertThat(savedNews).isEqualTo(dtos.get(0));
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void findNewsWithMatchingTitleAndText() {
        // Arrange
        doReturn(entities.get(0))
                .when(mapper).toEntity(dtos.get(0));
        doReturn(entities)
                .when(newsRepository).findAll(any(Example.class));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(any(News.class));

        // Act
        List<DTO.News> result = newsService.findNews(dtos.get(0));

        // Assert
        Assertions.assertThat(result).hasSize(2);
    }

    @Test
    void findNewsWithNoMatchingNews() {
        // Arrange
        doReturn(entities.get(0))
                .when(mapper).toEntity(dtos.get(0));
        doReturn(new ArrayList())
                .when(newsRepository).findAll(any(Example.class));

        // Act
        List<DTO.News> result = newsService.findNews(dtos.get(0));

        // Assert
        Assertions.assertThat(result).isEmpty();
        verify(newsRepository).findAll(any(Example.class));
        verify(mapper, never()).toDTO(any(News.class));
    }

    @Test
    void updateNewsShouldUpdateNewsEntity() throws CustomException {
        // Arrange
        doReturn(Optional.of(entities.get(0)))
                .when(newsRepository).findById(NEWS_ID);
        doReturn(entities.get(0))
                .when(newsRepository).save(any(News.class));
        doReturn(dtos.get(0))
                .when(mapper).toDTO(entities.get(0));

        // Act
        DTO.News savedNews = newsService.updateNews(NEWS_ID, dtos.get(0), aUser().buildToDTO());

        // Assert
        Assertions.assertThat(savedNews).isEqualTo(dtos.get(0));
        verify(newsRepository, times(2)).findById(NEWS_ID);
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void deleteNewsWhenUserIsAuthorizedShouldDeleteNewsAndComments() throws CustomException {
        //given
        doReturn(Optional.of(entities.get(0)))
                .when(newsRepository).findById(NEWS_ID);

        // Act
        newsService.deleteNews(NEWS_ID, aUser().buildToDTO());

        // Assert
        verify(commentService, times(1)).deleteAllComment(NEWS_ID);
        verify(newsRepository, times(1)).deleteById(NEWS_ID);
    }

    @Test
    void deleteNewsShouldThrowCustomExceptionWhenUserIsNotAuthorized() throws CustomException {
        UserDTO user = UserDTO.builder()
                .username("USERNAME + 1")
                .role("SUBSCRIBER")
                .build();
        //given
        doReturn(Optional.of(entities.get(0)))
                .when(newsRepository).findById(NEWS_ID);

        // Act
        Assertions.assertThatThrownBy(() -> newsService.deleteNews(NEWS_ID, user))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void deleteNewsWhenNewsDoesNotExistShouldThrowCustomException() {
        //given
        doReturn(Optional.empty())
                .when(newsRepository).findById(NEWS_ID);

        // Assert
        Assertions.assertThatThrownBy(() -> newsService.deleteNews(NEWS_ID, aUser().buildToDTO()))
                .isInstanceOf(CustomException.class);
    }
}