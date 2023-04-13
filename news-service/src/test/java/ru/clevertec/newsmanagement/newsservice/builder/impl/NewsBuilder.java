package ru.clevertec.newsmanagement.newsservice.builder.impl;

import com.google.protobuf.Timestamp;
import ru.clevertec.newsmanagement.newsservice.builder.TestDTOBuilder;
import ru.clevertec.newsmanagement.newsservice.builder.TestEntityBuilder;
import ru.clevertec.newsmanagement.newsservice.entity.News;
import ru.clevertec.newsmanagement.newsservice.model.DTO;

import java.time.Instant;
import java.util.Date;

public class NewsBuilder implements TestEntityBuilder<News>, TestDTOBuilder<DTO.News> {

    private Long id = 1L;
    private String username = "test username";
    private String title = "test title";
    private String text = "test text";
    private Date date = Date.from(Instant.ofEpochSecond(1000000));

    private NewsBuilder() {}

    public static NewsBuilder aNews() {
        return new NewsBuilder();
    }

    public NewsBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public NewsBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public NewsBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public NewsBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public NewsBuilder setDate(Date date) {
        this.date = date;
        return this;
    }

    @Override
    public News buildToEntity() {
        return News.builder()
                .id(id)
                .title(title)
                .text(text)
                .username(username)
                .createdDate(date)
                .build();
    }

    @Override
    public DTO.News buildToDTO() {
        Instant instant = date.toInstant();
        return DTO.News.newBuilder()
                .setId(id)
                .setTitle(title)
                .setText(text)
                .setUsername(username)
                .setCreatedDate(
                        Timestamp.newBuilder()
                                .setSeconds(instant.getEpochSecond())
                                .setNanos(instant.getNano())
                                .build()
                )
                .build();
    }
}
