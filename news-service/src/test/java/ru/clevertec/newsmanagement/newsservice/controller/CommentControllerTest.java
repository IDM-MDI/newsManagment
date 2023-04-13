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
import ru.clevertec.newsmanagement.newsservice.service.CommentService;

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
import static ru.clevertec.newsmanagement.newsservice.builder.impl.CommentBuilder.aComment;
import static ru.clevertec.newsmanagement.newsservice.builder.impl.UserBuilder.aUser;

@SpringBootTest(classes = NewsServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest extends PostgresTestContainer {
    @MockBean
    private CommentService commentService;
    @Mock
    private HttpServletRequest request;
    @Autowired
    private CommentController commentController;
    @Autowired
    private MockMvc mockMvc;

    private static final long NEWS_ID = 1L;
    private List<DTO.Comment> comments;

    @BeforeEach
    public void setup() {
        comments = Arrays.asList(
                aComment().buildToDTO(),
                aComment().setId(2L).buildToDTO(),
                aComment().setId(3L).buildToDTO()
        );
    }
    @Test
    void getNewsCommentShouldReturnOk() throws Exception {
        doReturn(comments)
                .when(commentService).findComments(eq(NEWS_ID),any(Pageable.class));

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCommentByIDShouldReturnOk() throws Exception {
        doReturn(comments.get(0))
                .when(commentService).findComments(NEWS_ID,NEWS_ID);

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment/1"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getCommentBySearchShouldReturnOk() throws Exception {
        doReturn(comments)
                .when(commentService).findComments(eq(NEWS_ID),any(DTO.Comment.class));

        // Act
        mockMvc.perform(get("/api/v1/news/1/comment/search?title=test"))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveCommentShouldReturnOk() throws Exception {
        // Arrange
        UserDTO expected = aUser().buildToDTO();
        doReturn(expected.getUsername())
                .when(request).getHeader("username");
        doReturn(expected.getRole())
                .when(request).getHeader("role");
        doReturn(expected.getJwt())
                .when(request).getHeader("auth-token");
        doReturn(comments.get(0))
                .when(commentService).saveComment(eq(NEWS_ID), any(DTO.Comment.class), any(UserDTO.class));

        // Act
        mockMvc.perform(post("/api/v1/news/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comments.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(comments.get(0).getId()))
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
        doReturn(comments.get(0))
                .when(commentService).updateComment(eq(NEWS_ID),eq(NEWS_ID),any(DTO.Comment.class),any(UserDTO.class));

        // Act
        mockMvc.perform(put("/api/v1/news/1/comment/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(comments.get(0))))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(comments.get(0).getId()))
                .andExpect(jsonPath("$.username").value(expected.getUsername()));
    }

    @Test
    void deleteCommentShouldReturnOk() throws Exception {
        // Arrange
        doNothing()
                .when(commentService).deleteComment(NEWS_ID,NEWS_ID,aUser().buildToDTO());

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