package ru.clevertec.newsmanagement.exceptionservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsmanagement.exceptionservice.ExceptionServiceApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = ExceptionServiceApplication.class)
@AutoConfigureMockMvc
class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void restControllerNotFound() throws Exception {
        // Act
        mockMvc.perform(get("/api/v1/notFound"))

                // Assert
                .andExpect(status().isNotFound());
    }
}