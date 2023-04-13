package ru.clevertec.newsmanagement.newsservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsmanagement.newsservice.NewsServiceApplication;
import ru.clevertec.newsmanagement.newsservice.container.PostgresTestContainer;
import ru.clevertec.newsmanagement.newsservice.model.DTO;
import ru.clevertec.newsmanagement.newsservice.model.UserDTO;
import ru.clevertec.newsmanagement.newsservice.service.NewsService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.newsmanagement.exceptionservice.util.JsonUtil.toJson;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.NewsBuilder.aNews;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

@SpringBootTest(classes = NewsServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NewsControllerTest extends PostgresTestContainer {
    @MockBean
    private NewsService newsService;
    @Mock
    private HttpServletRequest request;
    @Autowired
    private NewsController newsController;
    @Autowired
    private MockMvc mockMvc;
    private static final long NEWS_ID = 1L;
    private List<DTO.News> newsList;

    @BeforeEach
    public void setup() {
        newsList = Arrays.asList(
                aNews().buildToDTO(),
                aNews().setId(2L).buildToDTO(),
                aNews().setId(3L).buildToDTO()
        );
    }
    @Test
    void getNewsShouldReturnOk() throws Exception {
        doReturn(newsList)
                .when(newsService).findNews(any(Pageable.class));

        // Act
        mockMvc.perform(get("/api/v1/news"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getNewsByIDShouldReturnOk() throws Exception {
        doReturn(newsList.get(0))
                .when(newsService).findNews(NEWS_ID);

        // Act
        mockMvc.perform(get("/api/v1/news/1"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value("test username"));
    }

    @Test
    void getNewsBySearchShouldReturnOk() throws Exception {
        doReturn(newsList)
                .when(newsService).findNews(any(DTO.News.class));

        // Act
        mockMvc.perform(get("/api/v1/news/search?title=test"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveNewsShouldReturnOk() throws Exception {
        // Arrange
        UserDTO expected = aUser().buildToDTO();
        doReturn(expected.getUsername())
                .when(request).getHeader("username");
        doReturn(expected.getRole())
                .when(request).getHeader("role");
        doReturn(expected.getJwt())
                .when(request).getHeader("auth-token");
        doReturn(newsList.get(0))
                .when(newsService).saveNews(any(DTO.News.class), any(UserDTO.class));

        // Act
        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value(expected.getUsername()));
    }

    @Test
    void updateNewsShouldReturnOk() throws Exception {
        // Arrange
        UserDTO expected = aUser().buildToDTO();
        doReturn(expected.getUsername())
                .when(request).getHeader("username");
        doReturn(expected.getRole())
                .when(request).getHeader("role");
        doReturn(expected.getJwt())
                .when(request).getHeader("auth-token");
        doReturn(newsList.get(0))
                .when(newsService).updateNews(eq(NEWS_ID),any(DTO.News.class), any(UserDTO.class));

        // Act
        mockMvc.perform(put("/api/v1/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value(expected.getUsername()));
    }

    @Test
    void deleteNewsShouldReturnOk() throws Exception {
        // Arrange
        UserDTO expected = aUser().buildToDTO();
        doReturn(expected.getUsername())
                .when(request).getHeader("username");
        doReturn(expected.getRole())
                .when(request).getHeader("role");
        doReturn(expected.getJwt())
                .when(request).getHeader("auth-token");
        doNothing()
                .when(newsService).deleteNews(NEWS_ID,expected);

        // Act
        mockMvc.perform(delete("/api/v1/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}