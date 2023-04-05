package ru.clevertec.newsmanagement.util.impl;

import com.google.protobuf.Timestamp;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import ru.clevertec.newsmanagement.entity.News;
import ru.clevertec.newsmanagement.model.DTO;
import ru.clevertec.newsmanagement.util.ModelMapper;

import java.time.Instant;
import java.util.Date;


/**
 * Realisation of mapper with News and DTO.News
 * @author Dayanch
 */
@Component
public class NewsMapper implements ModelMapper<News, DTO.News> {
    /**
     * {@inheritDoc}
     */
    @Override
    public News toEntity(@NotNull DTO.News newsDTO) {
        return News.builder()
                .id(newsDTO.getId())
                .title(newsDTO.getTitle())
                .text(newsDTO.getText())
                .createdDate(
                        Date.from(Instant.ofEpochSecond(newsDTO.getCreatedDate().getSeconds(),
                                newsDTO.getCreatedDate().getNanos())))
                .build();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DTO.News toDTO(@NotNull News news) {
        Instant instant = news.getCreatedDate().toInstant();
        return DTO.News
                .newBuilder()
                .setId(news.getId())
                .setTitle(news.getTitle())
                .setText(news.getText())
                .setCreatedDate(Timestamp.newBuilder()
                        .setSeconds(instant.getEpochSecond())
                        .setNanos(instant.getNano())
                        .build())
                .setUsername(news.getUser().getUsername())
                .build();
    }
}
