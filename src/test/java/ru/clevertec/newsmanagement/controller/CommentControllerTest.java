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
import ru.clevertec.newsmanagement.model.PageFilter;
import ru.clevertec.newsmanagement.service.CommentService;

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
@WithMockUser(username = "username",authorities = "ROLE_SUBSCRIBER")
class CommentControllerTest {
    @MockBean
    private CommentService commentService;
    @Autowired
    private CommentController commentController;
    @Autowired
    private MockMvc mockMvc;

    private static final PageFilter PAGE = new PageFilter();
    private static final long NEWS_ID = 1L;
    private List<DTO.Comment> comments;

    @BeforeEach
    public void setup() {
        comments = Arrays.asList(
                DTO.Comment.newBuilder()
                        .setId(1L)
                        .setUsername("username")
                        .setText("test")
                        .build(),
                DTO.Comment.newBuilder()
                        .setId(2L)
                        .setUsername("username")
                        .setText("test")
                        .build(),
                DTO.Comment.newBuilder()
                        .setId(3L)
                        .setUsername("username")
                        .setText("test")
                        .build()
        );
    }
    @Test
    void getNewsCommentShouldReturnOk() throws Exception {
        when(commentService.findComments(NEWS_ID,PAGE))
                .thenReturn(comments);

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCommentByIDShouldReturnOk() throws Exception {
        when(commentService.findComments(NEWS_ID,NEWS_ID))
                .thenReturn(comments.get(0));

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment/1"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCommentBySearchShouldReturnOk() throws Exception {
        when(commentService.findComments(eq(NEWS_ID),any(DTO.Comment.class)))
                .thenReturn(comments);

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment/search?title=test"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveCommentShouldReturnOk() throws Exception {
        // Arrange
        when(commentService.saveComment(eq(NEWS_ID),anyString(),any(DTO.Comment.class))).thenReturn(comments.get(0));

        // Act
        mockMvc.perform(post("/api/v1/news/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comments.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(comments.get(0).getId()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void updateNewsShouldReturnOk() throws Exception {
        // Arrange
        when(commentService.updateComment(eq(NEWS_ID),eq(NEWS_ID),anyString(),any(DTO.Comment.class)))
                .thenReturn(comments.get(0));

        // Act
        mockMvc.perform(put("/api/v1/news/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comments.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(comments.get(0).getId()))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void deleteCommentShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(commentService).deleteComment(NEWS_ID,NEWS_ID,"username");

        // Act
        mockMvc.perform(delete("/api/v1/news/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comments.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void RestControllerNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/api/v1/notFound"))

                // Assert
                .andExpect(status().isNotFound());
    }
    @Test
    void RestControllerMethodNotAllowed() throws Exception {
        // Act
        mockMvc.perform(post("/api/v1/news/1/comment/1"))

                // Assert
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.exception").value("Request method \'POST\' is not supported"))
                .andExpect(jsonPath("$.url").value("http://localhost/api/v1/news/1/comment/1"));
    }
}