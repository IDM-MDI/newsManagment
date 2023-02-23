package ru.clevertec.newsmanagement.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsmanagement.SystemNewsManagementApplication;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.service.NewsService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.newsmanagement.util.DtoUtil.toJson;

@SpringBootTest(classes = SystemNewsManagementApplication.class)
@AutoConfigureMockMvc
@WithMockUser(username = "username",authorities = "ROLE_JOURNALIST")
class NewsControllerTest {

    @MockBean
    private NewsService newsService;
    @Autowired
    private NewsController newsController;
    @Autowired
    private MockMvc mockMvc;
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String FILTER = "id";
    private static final String DIRECTION = "asc";
    private static final long NEWS_ID = 1L;
    private List<DTO.News> newsList;

    @BeforeEach
    public void setup() {
        newsList = Arrays.asList(
                DTO.News.newBuilder()
                        .setId(1L)
                        .setUsername("username")
                        .setText("test")
                        .build(),
                DTO.News.newBuilder()
                        .setId(2L)
                        .setUsername("username")
                        .setText("test")
                        .build(),
                DTO.News.newBuilder()
                        .setId(3L)
                        .setUsername("username")
                        .setText("test")
                        .build()
        );
    }
    @Test
    void getNewsShouldReturnOk() throws Exception {
        when(newsService.findNews(PAGE,SIZE,FILTER,DIRECTION))
                .thenReturn(newsList);

        // Act
        mockMvc.perform(get("/api/v1/news"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getNewsByIDShouldReturnOk() throws Exception {
        when(newsService.findNews(NEWS_ID))
                .thenReturn(newsList.get(0));

        // Act
        mockMvc.perform(get("/api/v1/news/1"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void getNewsBySearchShouldReturnOk() throws Exception {
        when(newsService.findNews(any(DTO.News.class)))
                .thenReturn(newsList);

        // Act
        mockMvc.perform(get("/api/v1/news/search?title=test"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveNewsShouldReturnOk() throws Exception {
        // Arrange
        when(newsService.saveNews(anyString(),any(DTO.News.class)))
                .thenReturn(newsList.get(0));

        // Act
        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void updateNewsShouldReturnOk() throws Exception {
        // Arrange
        when(newsService.updateNews(eq(NEWS_ID),anyString(),any(DTO.News.class)))
                .thenReturn(newsList.get(0));

        // Act
        mockMvc.perform(put("/api/v1/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(newsList.get(0).getId()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void deleteNewsShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(newsService).deleteNews(NEWS_ID,"username");

        // Act
        mockMvc.perform(delete("/api/v1/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(newsList.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}